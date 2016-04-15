package org.moflon.ide.core.runtime;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

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
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.gervarro.eclipse.workspace.autosetup.JavaProjectConfigurator;
import org.gervarro.eclipse.workspace.autosetup.PluginProjectConfigurator;
import org.gervarro.eclipse.workspace.autosetup.ProjectConfigurator;
import org.gervarro.eclipse.workspace.autosetup.ProjectNatureAndBuilderConfiguratorTask;
import org.gervarro.eclipse.workspace.autosetup.WorkspaceAutoSetupModule;
import org.gervarro.eclipse.workspace.util.ProjectUtil;
import org.gervarro.eclipse.workspace.util.WorkspaceTask;
import org.moflon.core.utilities.WorkspaceHelper;
import org.moflon.ide.core.CoreActivator;
import org.moflon.properties.MoflonPropertiesContainerHelper;
import org.moflon.util.plugins.BuildPropertiesFileBuilder;
import org.moflon.util.plugins.MetamodelProperties;
import org.moflon.util.plugins.manifest.ManifestFileUpdater;
import org.moflon.util.plugins.manifest.ManifestFileUpdater.AttributeUpdatePolicy;
import org.moflon.util.plugins.manifest.PluginManifestConstants;

import MoflonPropertyContainer.MoflonPropertiesContainer;
import MoflonPropertyContainer.SDMCodeGeneratorIds;

public class MoflonProjectCreator extends WorkspaceTask implements ProjectConfigurator {
	private static final Logger logger = Logger.getLogger(MoflonProjectCreator.class);

	private IProject workspaceProject;
	private MetamodelProperties metamodelProperties;

	public MoflonProjectCreator(final IProject project, final MetamodelProperties projectProperties) {
		this.workspaceProject = project;
		this.metamodelProperties = projectProperties;
	}

	@Override
	public void run(final IProgressMonitor monitor) throws CoreException {
		if (!workspaceProject.exists()) {
			final String projectName = metamodelProperties.getProjectName();
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
									metamodelProperties.get(MetamodelProperties.NAME_KEY), AttributeUpdatePolicy.KEEP);
							changed |= ManifestFileUpdater.updateAttribute(manifest,
									PluginManifestConstants.BUNDLE_SYMBOLIC_NAME,
									metamodelProperties.get(MetamodelProperties.PLUGIN_ID_KEY) + ";singleton:=true",
									AttributeUpdatePolicy.KEEP);
							changed |= ManifestFileUpdater.updateAttribute(manifest, PluginManifestConstants.BUNDLE_VERSION,
									"1.0", AttributeUpdatePolicy.KEEP);
							changed |= ManifestFileUpdater.updateAttribute(manifest, PluginManifestConstants.BUNDLE_VENDOR,
									"TU Darmstadt", AttributeUpdatePolicy.KEEP);
							changed |= ManifestFileUpdater.updateAttribute(manifest,
									PluginManifestConstants.BUNDLE_ACTIVATION_POLICY, "lazy", AttributeUpdatePolicy.KEEP);
							changed |= ManifestFileUpdater.updateAttribute(manifest,
									PluginManifestConstants.BUNDLE_EXECUTION_ENVIRONMENT,
									metamodelProperties.get(MetamodelProperties.JAVA_VERION), AttributeUpdatePolicy.KEEP);
							return changed;
						});
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// (4) Create build.properties file
			logger.debug("Adding build.properties");
			new BuildPropertiesFileBuilder().createBuildProperties(workspaceProject,
					WorkspaceHelper.createSubmonitorWith1Tick(monitor));

			// (5) Configure natures and builders (.project file)
			final JavaProjectConfigurator javaProjectConfigurator =
					new JavaProjectConfigurator();
			final PluginProjectConfigurator pluginProjectConfigurator =
					new PluginProjectConfigurator();
			final ProjectNatureAndBuilderConfiguratorTask natureAndBuilderConfiguratorTask =
					new ProjectNatureAndBuilderConfiguratorTask(workspaceProject, false);
			natureAndBuilderConfiguratorTask.updateNatureIDs(javaProjectConfigurator, true);
			natureAndBuilderConfiguratorTask.updateBuildSpecs(javaProjectConfigurator, true);
			natureAndBuilderConfiguratorTask.updateNatureIDs(this, true);
			natureAndBuilderConfiguratorTask.updateBuildSpecs(this, true);
			natureAndBuilderConfiguratorTask.updateNatureIDs(pluginProjectConfigurator, true);
			natureAndBuilderConfiguratorTask.updateBuildSpecs(pluginProjectConfigurator, true);
			WorkspaceTask.execute(natureAndBuilderConfiguratorTask, false);

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
							metamodelProperties.getMetamodelProjectName());
			moflonProperties.getSdmCodegeneratorHandlerId().setValue(
					getCodeGeneratorHandler(metamodelProperties.getType()));
			MoflonPropertiesContainerHelper.save(moflonProperties, WorkspaceHelper.createSubmonitorWith1Tick(monitor));
		}

		monitor.done();
	}
	
	private final SDMCodeGeneratorIds getCodeGeneratorHandler(final String type) {
		return MetamodelProperties.INTEGRATION_KEY.equals(type) ?
				SDMCodeGeneratorIds.DEMOCLES_REVERSE_NAVI : SDMCodeGeneratorIds.DEMOCLES;
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

	@Override
	public String getTaskName() {
		return "Creating Moflon project";
	}

	@Override
	public ISchedulingRule getRule() {
		return ResourcesPlugin.getWorkspace().getRoot();
	}

	public String[] updateNatureIDs(String[] natureIDs, final boolean added) throws CoreException {
		final String natureID = MetamodelProperties.REPOSITORY_KEY.equals(metamodelProperties.getType()) ? 
				WorkspaceHelper.REPOSITORY_NATURE_ID : WorkspaceHelper.INTEGRATION_NATURE_ID;
		if (added) {
			if (ProjectUtil.indexOf(natureIDs, natureID) < 0) {
				natureIDs = Arrays.copyOf(natureIDs, natureIDs.length + 1);
				natureIDs[natureIDs.length-1] = natureID;
			}
		} else {
			int naturePosition = ProjectUtil.indexOf(natureIDs, natureID);
			if (naturePosition >= 0) {
				natureIDs = WorkspaceAutoSetupModule.remove(natureIDs, naturePosition);
			}
		}
		return natureIDs;
	}
	
	public ICommand[] updateBuildSpecs(final IProjectDescription description, ICommand[] buildSpecs, final boolean added) throws CoreException {
		final String builderID = MetamodelProperties.REPOSITORY_KEY.equals(metamodelProperties.getType()) ? 
				CoreActivator.REPOSITORY_BUILDER_ID : CoreActivator.INTEGRATION_BUILDER_ID;
		if (added) {
			int javaBuilderPosition = ProjectUtil.indexOf(buildSpecs, "org.eclipse.jdt.core.javabuilder");
			int moflonBuilderPosition = ProjectUtil.indexOf(buildSpecs, builderID);
			if (moflonBuilderPosition < 0) {
				final ICommand manifestBuilder = description.newCommand();
				manifestBuilder.setBuilderName(builderID);
				buildSpecs = Arrays.copyOf(buildSpecs, buildSpecs.length + 1);
				moflonBuilderPosition = buildSpecs.length - 1;
				buildSpecs[moflonBuilderPosition] = manifestBuilder;
			} 
			if (javaBuilderPosition < moflonBuilderPosition) {
				final ICommand moflonBuilder = buildSpecs[moflonBuilderPosition];
				System.arraycopy(buildSpecs, javaBuilderPosition, buildSpecs, javaBuilderPosition+1, moflonBuilderPosition-javaBuilderPosition);
				moflonBuilderPosition = javaBuilderPosition++;
				buildSpecs[moflonBuilderPosition] = moflonBuilder;
			}
		} else {
			int moflonBuilderPosition = ProjectUtil.indexOf(buildSpecs, builderID);
			if (moflonBuilderPosition >= 0) {
				ICommand[] oldBuilderSpecs = buildSpecs;
				buildSpecs = new ICommand[oldBuilderSpecs.length - 1];
				if (moflonBuilderPosition > 0) {
					System.arraycopy(oldBuilderSpecs, 0, buildSpecs, 0, moflonBuilderPosition);
				}
				if (moflonBuilderPosition == buildSpecs.length) {
					System.arraycopy(oldBuilderSpecs, moflonBuilderPosition+1, buildSpecs, moflonBuilderPosition, buildSpecs.length-moflonBuilderPosition);
				}
			}
		}
		return buildSpecs;
	}
}
