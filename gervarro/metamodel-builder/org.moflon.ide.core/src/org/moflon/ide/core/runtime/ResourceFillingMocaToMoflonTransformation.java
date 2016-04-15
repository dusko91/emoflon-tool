package org.moflon.ide.core.runtime;

import java.util.Collection;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.gervarro.eclipse.workspace.util.WorkspaceTask;
import org.moflon.TGGLanguageActivator;
import org.moflon.core.moca.tree.MocaTreePlugin;
import org.moflon.core.utilities.MoflonUtil;
import org.moflon.core.utilities.UncheckedCoreException;
import org.moflon.core.utilities.WorkspaceHelper;
import org.moflon.ide.core.CoreActivator;
import org.moflon.ide.core.runtime.builders.NewMetamodelBuilder;
import org.moflon.properties.MoflonPropertiesContainerHelper;
import org.moflon.sdm.language.SDMLanguagePlugin;
import org.moflon.tgg.runtime.TGGRuntimePlugin;
import org.moflon.util.plugins.MetamodelProperties;
import org.moflon.util.plugins.PluginProducerWorkspaceRunnable;

import MocaTree.Node;
import MoflonPropertyContainer.Dependencies;
import MoflonPropertyContainer.MoflonPropertiesContainer;
import MoflonPropertyContainer.MoflonPropertyContainerFactory;
import MoflonPropertyContainer.PropertiesValue;

public class ResourceFillingMocaToMoflonTransformation extends BasicResourceFillingMocaToMoflonTransformation {
	private static final Logger logger = Logger.getLogger(ResourceFillingMocaToMoflonTransformation.class);

	private Map<String, MetamodelProperties> propertiesMap;
	private IProgressMonitor monitor;

	public ResourceFillingMocaToMoflonTransformation(final ResourceSet resourceSet,
			final NewMetamodelBuilder resourceSetProcessor,
			final IProject metamodelProject,
			final Map<String, MetamodelProperties> propertiesMap,
			final IProgressMonitor progressMonitor) {
		super(resourceSet, resourceSetProcessor, metamodelProject);
		this.monitor = progressMonitor;
		this.propertiesMap = propertiesMap;
	}

	   @Override
	   public void handleOutermostPackage(final Node node, final EPackage outermostPackage) {
		   super.handleOutermostPackage(node, outermostPackage);
		   monitor.worked(1);
	   }
	   
	   protected void handleMissingProject(final Node node, final IProject project) {
		   final MetamodelProperties properties = propertiesMap.get(project.getName());
		   final NewMoflonProjectCreator moflonProjectCreator =
				   new NewMoflonProjectCreator(project, properties);
		   try {
			   WorkspaceTask.execute(moflonProjectCreator, false);
		   } catch (CoreException e) {
			   reportError(e);
		   }
	   }
	   
	   protected void handleClosedProject(final Node node, final IProject project) {
		   try {
			   project.open(new NullProgressMonitor());
		   } catch (final CoreException e) {
			   throw new UncheckedCoreException(e);
		   }
	   }

	   // New corrected method
	   protected void handleOpenProject(final Node node, final IProject project) {
	      final MetamodelProperties properties = propertiesMap.get(project.getName());
	      final MoflonPropertiesContainer moflonProps =
	    		  createOrLoadMoflonProperties(project, properties.getMetamodelProjectName());
	      final OpenProjectHandler openProjectHandler =
	    		  new OpenProjectHandler(project, properties, moflonProps);
	      try {
	    	  WorkspaceTask.execute(openProjectHandler, false);
	      } catch (final CoreException e) {
	    	  reportError(e);
	      }
	   }

	   private final MoflonPropertiesContainer createOrLoadMoflonProperties(
			   final IProject project, final String metamodelProject) {
	      IFile moflonProps = project.getFile(MoflonPropertiesContainerHelper.MOFLON_CONFIG_FILE);
	      
	      if (moflonProps.exists()) {
	    	  URI projectURI = URI.createPlatformResourceURI(project.getName() + "/", true);
	    	  URI moflonPropertiesURI = URI.createURI(MoflonPropertiesContainerHelper.MOFLON_CONFIG_FILE).resolve(projectURI);
	    	  Resource moflonPropertiesResource = set.getResource(moflonPropertiesURI, true);
	    	  MoflonPropertiesContainer container = (MoflonPropertiesContainer) moflonPropertiesResource.getContents().get(0);
	    	  container.updateMetamodelProjectName(metamodelProject);
	    	  return container;
	      } else {
	    	  return MoflonPropertiesContainerHelper.createDefaultPropertiesContainer(project.getName(), metamodelProject);
	      }
	   }

	   // Outdated method
   protected void handleOpenProject(final Node node, final IProject project, final Resource resource) {
      MetamodelProperties properties = propertiesMap.get(project.getName());

      try
      {
         ResourcesPlugin.getWorkspace().run(new PluginProducerWorkspaceRunnable(project, properties), project, 0, new NullProgressMonitor());
      } catch (CoreException e)
      {
         this.reportError(e);
      }

      try
      {
         MoflonProjectCreator.createFoldersIfNecessary(project, new NullProgressMonitor());
      } catch (final CoreException e)
      {
         logger.warn("Failed to create folders: " + e.getMessage());
      }

      MoflonPropertiesContainer moflonProps;

      try
      {
         moflonProps = createOrLoadMoflonProperties(project, properties.getMetamodelProjectName());
      } catch (Exception e)
      {
         logger.fatal("Unable to load moflon.properties.xmi from " + project.getName() + ". Reason: " + e.getMessage());
         e.printStackTrace();
         return;
      }

      moflonProps.getDependencies().clear();
      properties.getDependenciesAsURIs().stream().forEach(dep -> addMetamodelDependency(moflonProps, dep));

      // These two metamodels are usually used directly or indirectly by most projects
      addMetamodelDependency(moflonProps, MoflonUtil.getDefaultURIToEcoreFileInPlugin(WorkspaceHelper.PLUGIN_ID_ECORE));

      // Additional standard dependencies for integration projects
      if (MOCA_TREE_ATTRIBUTE_INTEGRATION_PROJECT.equals(node.getName()))
      {
         resource.setURI(URI.createURI(resource.getURI().toString().replace(WorkspaceHelper.ECORE_FILE_EXTENSION, WorkspaceHelper.PRE_ECORE_FILE_EXTENSION)));

         addMetamodelDependency(moflonProps, MoflonUtil.getDefaultURIToEcoreFileInPlugin(TGGRuntimePlugin.getDefault().getPluginId()));
         addMetamodelDependency(moflonProps, MoflonUtil.getDefaultURIToEcoreFileInPlugin(SDMLanguagePlugin.getDefault().getPluginId()));
         addMetamodelDependency(moflonProps, MoflonUtil.getDefaultURIToEcoreFileInPlugin(TGGLanguageActivator.getDefault().getPluginId()));
         addMetamodelDependency(moflonProps, MoflonUtil.getDefaultURIToEcoreFileInPlugin(MocaTreePlugin.getDefault().getPluginId()));
      }

      MoflonPropertiesContainerHelper.save(moflonProps, new NullProgressMonitor());

      WorkspaceHelper.moveProjectToWorkingSet(project, properties.get(MetamodelProperties.WORKING_SET_KEY));

      CoreActivator.getDefault().setDirty(project, true);

   }

   public void addMetamodelDependency(final MoflonPropertiesContainer moflonProperties, final URI metamodelUri)
   {
      Dependencies dep = MoflonPropertyContainerFactory.eINSTANCE.createDependencies();
      dep.setValue(metamodelUri.toString());
      if (!alreadyContainsDependency(moflonProperties.getDependencies(), dep))
         moflonProperties.getDependencies().add(dep);
   }

   private boolean alreadyContainsDependency(final Collection<? extends PropertiesValue> dependencies, final PropertiesValue addDep)
   {
      return dependencies.stream().anyMatch(d -> d.getValue().equals(addDep.getValue()));
   }
}
