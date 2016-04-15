package org.moflon.ide.core.runtime;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.gervarro.eclipse.workspace.util.WorkspaceTask;
import org.moflon.core.utilities.WorkspaceHelper;
import org.moflon.properties.MoflonPropertiesContainerHelper;
import org.moflon.util.plugins.BuildPropertiesFileBuilder;
import org.moflon.util.plugins.MetamodelProperties;
import org.moflon.util.plugins.manifest.ManifestFileUpdater;
import org.moflon.util.plugins.manifest.ManifestFileUpdater.AttributeUpdatePolicy;
import org.moflon.util.plugins.manifest.PluginManifestConstants;

import MoflonPropertyContainer.MoflonPropertiesContainer;
import MoflonPropertyContainer.SDMCodeGeneratorIds;

public class MoflonProjectCreator extends WorkspaceTask {
	private static final Logger logger = Logger.getLogger(MoflonProjectCreator.class);
	private static final String SCHEMA_BUILDER_NAME = "org.eclipse.pde.SchemaBuilder";
	private static final String MANIFEST_BUILDER_NAME = "org.eclipse.pde.ManifestBuilder";

	private IProject workspaceProject;
	private MetamodelProperties properties;

	public MoflonProjectCreator(final IProject project, final MetamodelProperties projectProperties) {
		this.workspaceProject = project;
		this.properties = projectProperties;
	}

	@Override
	public void run(final IProgressMonitor monitor) throws CoreException {
		if (!workspaceProject.exists()) {
			final String projectName = properties.getProjectName();
			monitor.beginTask("Creating project " + projectName, 10);
			
			// (1) Create project
			final IProjectDescription description =
					ResourcesPlugin.getWorkspace().newProjectDescription(projectName);
			workspaceProject.create(description, WorkspaceHelper.createSubmonitorWith1Tick(monitor));
			workspaceProject.open(WorkspaceHelper.createSubmonitorWith1Tick(monitor));

			// (2) Create folders and files in project
			createFoldersIfNecessary(workspaceProject, WorkspaceHelper.createSubMonitor(monitor, 4));
			addGitIgnoreFiles(workspaceProject, WorkspaceHelper.createSubMonitor(monitor, 2));

			// (3) Create MANIFEST.MF file
			try {
				logger.debug("Adding MANIFEST.MF");
				new ManifestFileUpdater().processManifest(
						workspaceProject,
						manifest -> {
							boolean changed = false;
							changed |= ManifestFileUpdater.updateAttribute(manifest, PluginManifestConstants.MANIFEST_VERSION,
									"1.0", AttributeUpdatePolicy.KEEP);
							changed |= ManifestFileUpdater.updateAttribute(manifest,
									PluginManifestConstants.BUNDLE_MANIFEST_VERSION, "2", AttributeUpdatePolicy.KEEP);
							changed |= ManifestFileUpdater.updateAttribute(manifest, PluginManifestConstants.BUNDLE_NAME,
									properties.get(MetamodelProperties.NAME_KEY), AttributeUpdatePolicy.KEEP);
							changed |= ManifestFileUpdater.updateAttribute(manifest,
									PluginManifestConstants.BUNDLE_SYMBOLIC_NAME,
									properties.get(MetamodelProperties.PLUGIN_ID_KEY) + ";singleton:=true",
									AttributeUpdatePolicy.KEEP);
							changed |= ManifestFileUpdater.updateAttribute(manifest, PluginManifestConstants.BUNDLE_VERSION,
									"1.0", AttributeUpdatePolicy.KEEP);
							changed |= ManifestFileUpdater.updateAttribute(manifest, PluginManifestConstants.BUNDLE_VENDOR,
									"TU Darmstadt", AttributeUpdatePolicy.KEEP);
							changed |= ManifestFileUpdater.updateAttribute(manifest,
									PluginManifestConstants.BUNDLE_ACTIVATION_POLICY, "lazy", AttributeUpdatePolicy.KEEP);
							changed |= ManifestFileUpdater.updateAttribute(manifest,
									PluginManifestConstants.BUNDLE_EXECUTION_ENVIRONMENT,
									properties.get(MetamodelProperties.JAVA_VERION), AttributeUpdatePolicy.KEEP);
							return changed;
						});
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// (4) Create build.properties file
			logger.debug("Adding build.properties");
			new BuildPropertiesFileBuilder().createBuildProperties(workspaceProject,
					WorkspaceHelper.createSubmonitorWith1Tick(monitor));

			// TODO (5) Configure natures and builders (.project file)
			final String moflonNatureID = getNatureID(properties.getType());
			registerPluginBuildersAndAddNature(workspaceProject, WorkspaceHelper.createSubmonitorWith1Tick(monitor));

			// (6) Configure Java settings (.classpath file)
			final IJavaProject javaProject = JavaCore.create(workspaceProject);
			final IClasspathEntry srcFolderEntry =
					JavaCore.newSourceEntry(workspaceProject.getFolder("src").getFullPath());
			final IClasspathEntry genFolderEntry =
					JavaCore.newSourceEntry(workspaceProject.getFolder(WorkspaceHelper.GEN_FOLDER).getFullPath(),
					new IPath[0], new IPath[0], null,
					new IClasspathAttribute[] { JavaCore.newClasspathAttribute("ignore_optional_problems", "true") });
			final IClasspathEntry jreContainerEntry =
					JavaCore.newContainerEntry(new Path("org.eclipse.jdt.launching.JRE_CONTAINER"));
			final IClasspathEntry pdeContainerEntry =
					JavaCore.newContainerEntry(new Path("org.eclipse.pde.core.requiredPlugins"));
			javaProject.setRawClasspath(
					new IClasspathEntry[] { srcFolderEntry, genFolderEntry, jreContainerEntry, pdeContainerEntry },
					workspaceProject.getFolder("bin").getFullPath(),
					true, WorkspaceHelper.createSubmonitorWith1Tick(monitor));

			// (7) Create Moflon properties file (moflon.properties.xmi)
			MoflonPropertiesContainer moflonProperties =
					MoflonPropertiesContainerHelper.createDefaultPropertiesContainer(workspaceProject.getName(),
							properties.getMetamodelProjectName());
			setDefaultCodeGenerator(moflonProperties);
			MoflonPropertiesContainerHelper.save(moflonProperties, WorkspaceHelper.createSubmonitorWith1Tick(monitor));
		}

		monitor.done();
	}
	
	private final String getNatureID(final String type) {
		return MetamodelProperties.REPOSITORY_KEY.equals(type) ? WorkspaceHelper.REPOSITORY_NATURE_ID : WorkspaceHelper.INTEGRATION_NATURE_ID;
	}

	private void setDefaultCodeGenerator(final MoflonPropertiesContainer moflonProps) {
		if (properties.getType().equals(MetamodelProperties.INTEGRATION_KEY)) {
			moflonProps.getSdmCodegeneratorHandlerId().setValue(SDMCodeGeneratorIds.DEMOCLES_REVERSE_NAVI);
		} else {
			moflonProps.getSdmCodegeneratorHandlerId().setValue(SDMCodeGeneratorIds.DEMOCLES);
		}
	}

	private static void addGitIgnoreFiles(final IProject project, final IProgressMonitor monitor) throws CoreException {
		try {
			monitor.beginTask("Creating .gitignore files", 2);
			IFile genGitIgnore = WorkspaceHelper.getGenFolder(project).getFile(".gitignore");
			if (!genGitIgnore.exists()) {
				genGitIgnore.create(new ByteArrayInputStream("*".getBytes()), true, WorkspaceHelper.createSubmonitorWith1Tick(monitor));
			}

			IFile modelGitIgnore = WorkspaceHelper.getModelFolder(project).getFile(".gitignore");
			if (!modelGitIgnore.exists()) {
				modelGitIgnore.create(new ByteArrayInputStream("*".getBytes()), true, WorkspaceHelper.createSubmonitorWith1Tick(monitor));
			}
		} finally {
			monitor.done();
		}
	}

	public static void createFoldersIfNecessary(final IProject project, final IProgressMonitor monitor) throws CoreException {
		try {
			monitor.beginTask("Creating folders within project", 7);
			WorkspaceHelper.createFolderIfNotExists(project.getFolder("src"), WorkspaceHelper.createSubmonitorWith1Tick(monitor));
			WorkspaceHelper.createFolderIfNotExists(project.getFolder("bin"), WorkspaceHelper.createSubmonitorWith1Tick(monitor));
			WorkspaceHelper.createFolderIfNotExists(project.getFolder(WorkspaceHelper.GEN_FOLDER), WorkspaceHelper.createSubmonitorWith1Tick(monitor));
			WorkspaceHelper.createFolderIfNotExists(project.getFolder(WorkspaceHelper.LIB_FOLDER), WorkspaceHelper.createSubmonitorWith1Tick(monitor));
			WorkspaceHelper.createFolderIfNotExists(project.getFolder(WorkspaceHelper.MODEL_FOLDER), WorkspaceHelper.createSubmonitorWith1Tick(monitor));
			WorkspaceHelper.createFolderIfNotExists(project.getFolder(WorkspaceHelper.INSTANCES_FOLDER), WorkspaceHelper.createSubmonitorWith1Tick(monitor));
			WorkspaceHelper.createFolderIfNotExists(project.getFolder(WorkspaceHelper.INJECTION_FOLDER), WorkspaceHelper.createSubmonitorWith1Tick(monitor));
		} finally {
			monitor.done();
		}
	}

	private static void registerPluginBuildersAndAddNature(final IProject currentProject,
			final SubProgressMonitor monitor) throws CoreException {
		monitor.beginTask("Registering plugin builders and add plugin nature", 2);

		IProjectDescription description = WorkspaceHelper.getDescriptionWithAddedNature(currentProject,
				WorkspaceHelper.PLUGIN_NATURE_ID, WorkspaceHelper.createSubmonitorWith1Tick(monitor));

		List<ICommand> oldBuilders = new ArrayList<>();
		oldBuilders.addAll(Arrays.asList(description.getBuildSpec()));

		List<ICommand> newBuilders = new ArrayList<>();

		// Plugins builders must be appended to the end
		newBuilders.addAll(oldBuilders);

		if (!containsBuilder(oldBuilders, MANIFEST_BUILDER_NAME)) {
			final ICommand manifestBuilder = description.newCommand();
			manifestBuilder.setBuilderName(MANIFEST_BUILDER_NAME);
			newBuilders.add(manifestBuilder);
		}

		if (!containsBuilder(oldBuilders, SCHEMA_BUILDER_NAME)) {
			final ICommand schemaBuilder = description.newCommand();
			schemaBuilder.setBuilderName(SCHEMA_BUILDER_NAME);
			newBuilders.add(schemaBuilder);
		}

		description.setBuildSpec(newBuilders.toArray(new ICommand[newBuilders.size()]));
		currentProject.setDescription(description, WorkspaceHelper.createSubmonitorWith1Tick(monitor));

		monitor.done();
	}

	private static boolean containsBuilder(final List<ICommand> builders, final String name) {
		return builders.stream().anyMatch(c -> c.getBuilderName().equals(name));
	}

	@Override
	public String getTaskName() {
		return "Creating Moflon project";
	}

	@Override
	public ISchedulingRule getRule() {
		return ResourcesPlugin.getWorkspace().getRoot();
	}
}
