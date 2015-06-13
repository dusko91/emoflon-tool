package org.moflon.ide.core.runtime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.gervarro.eclipse.workspace.util.WorkspaceTask;
import org.moflon.ide.core.properties.MetamodelProperties;
import org.moflon.properties.MoflonPropertiesContainerHelper;
import org.moflon.util.WorkspaceHelper;
import org.moflon.util.plugins.BuildPropertiesFileBuilder;
import org.moflon.util.plugins.ManifestFileUpdater;
import org.moflon.util.plugins.PluginManifestConstants;
import org.moflon.util.plugins.ManifestFileUpdater.AttributeUpdatePolicy;

import MoflonPropertyContainer.MoflonPropertiesContainer;

public class MoflonProjectCreator extends WorkspaceTask
{
	static final Object FAMILY = new Object();
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
   public void run(final IProgressMonitor monitor) throws CoreException
   {
	   if (!workspaceProject.exists()) {
		   final String projectName = properties.getProjectName();
		   monitor.beginTask("Creating project " + projectName, 10);
		   final IProjectDescription description = ResourcesPlugin.getWorkspace().newProjectDescription(projectName);

		   workspaceProject.create(description, WorkspaceHelper.createSubmonitorWith1Tick(monitor));
		   workspaceProject.open(WorkspaceHelper.createSubmonitorWith1Tick(monitor));

		   IFolder gen = workspaceProject.getFolder(WorkspaceHelper.GEN_FOLDER);
		   gen.create(true, true, WorkspaceHelper.createSubmonitorWith1Tick(monitor));

		   workspaceProject.getFolder(WorkspaceHelper.LIB_FOLDER).create(true, true, WorkspaceHelper.createSubmonitorWith1Tick(monitor));
		   workspaceProject.getFolder(WorkspaceHelper.MODEL_FOLDER).create(true, true, WorkspaceHelper.createSubmonitorWith1Tick(monitor));
		   workspaceProject.getFolder(WorkspaceHelper.INSTANCES_FOLDER).create(true, true, WorkspaceHelper.createSubmonitorWith1Tick(monitor));

		   addMoflonProjectNature(monitor, properties.getType(), workspaceProject);

		   IJavaProject javaProject = WorkspaceHelper.setUpAsJavaProject(workspaceProject, WorkspaceHelper.createSubmonitorWith1Tick(monitor));
		   WorkspaceHelper.addContainerToBuildPath(workspaceProject, "org.eclipse.pde.core.requiredPlugins");

		   WorkspaceHelper.setAsSourceFolderInBuildpath(javaProject, new IFolder[] { gen },
				   new IClasspathAttribute[] { JavaCore.newClasspathAttribute("ignore_optional_problems", "true") },
				   WorkspaceHelper.createSubmonitorWith1Tick(monitor));

		   MoflonPropertiesContainer moflonProperties =
				   MoflonPropertiesContainerHelper.createDefaultPropertiesContainer(workspaceProject.getName(), properties.getMetamodelProjectName());
		   MoflonPropertiesContainerHelper.save(moflonProperties, WorkspaceHelper.createSubmonitorWith1Tick(monitor));

		   try {
			   addPluginFeatures(workspaceProject, properties, monitor);
		   } catch (IOException e) {
			   e.printStackTrace();
		   }

		   WorkspaceHelper.moveProjectToWorkingSet(workspaceProject, properties.get(MetamodelProperties.WORKING_SET_KEY));
	   }

	   monitor.done();
   }

   private void addMoflonProjectNature(final IProgressMonitor monitor, final String type, final IProject newProjectHandle) throws CoreException
   {
      if (type.equals(MetamodelProperties.REPOSITORY_KEY))
         WorkspaceHelper.addNature(newProjectHandle, WorkspaceHelper.REPOSITORY_NATURE_ID, WorkspaceHelper.createSubmonitorWith1Tick(monitor));
      else
         WorkspaceHelper.addNature(newProjectHandle, WorkspaceHelper.INTEGRATION_NATURE_ID, WorkspaceHelper.createSubmonitorWith1Tick(monitor));
   }
   
	private void addPluginFeatures(final IProject currentProject, final MetamodelProperties properties,
			final IProgressMonitor monitor) throws CoreException, IOException {
		monitor.beginTask("Creating plugin project", 5);

		registerPluginBuildersAndAddNature(currentProject, WorkspaceHelper.createSubmonitorWith1Tick(monitor));

		logger.debug("Adding MANIFEST.MF");
		new ManifestFileUpdater().processManifest(
				currentProject,
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

		logger.debug("Adding build.properties");
		new BuildPropertiesFileBuilder().createBuildProperties(currentProject,
				WorkspaceHelper.createSubmonitorWith1Tick(monitor));

		monitor.done();
	}

	private static void registerPluginBuildersAndAddNature(final IProject currentProject,
			final SubProgressMonitor monitor) throws CoreException {
		monitor.beginTask("Registering plugin builders and add plugin nature", 2);

		IProjectDescription description = WorkspaceHelper.getDescriptionWithAddedNature(currentProject,
				WorkspaceHelper.PLUGIN_NATURE_ID, WorkspaceHelper.createSubmonitorWith1Tick(monitor));

		List<ICommand> oldBuilders = new ArrayList<>();
		oldBuilders.addAll(Arrays.asList(description.getBuildSpec()));

		List<ICommand> newBuilders = new ArrayList<>();
		
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
	
		// Add old builders after the plugin builders
		newBuilders.addAll(oldBuilders);

		description.setBuildSpec(newBuilders.toArray(new ICommand[newBuilders.size()]));
		currentProject.setDescription(description, WorkspaceHelper.createSubmonitorWith1Tick(monitor));

		monitor.done();
	}

	private static boolean containsBuilder(final List<ICommand> builders, final String name) {
		return builders.stream().anyMatch(c -> c.getBuilderName().equals(name));
	}

	@Override
	protected String getTaskName() {
		return "Creating Moflon project";
	}

	@Override
	public ISchedulingRule getRule() {
		return ResourcesPlugin.getWorkspace().getRoot();
	}
}
