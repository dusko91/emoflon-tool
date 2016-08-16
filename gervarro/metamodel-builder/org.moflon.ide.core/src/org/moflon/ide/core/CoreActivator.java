package org.moflon.ide.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IBuildConfiguration;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.PluginRegistry;
import org.gervarro.eclipse.workspace.util.WorkspaceObservationLifecycleManager;
import org.gervarro.eclipse.workspace.util.WorkspaceTask;
import org.moflon.codegen.eclipse.CodeGeneratorPlugin;
import org.moflon.core.utilities.EMoflonPlugin;
import org.moflon.core.utilities.MoflonUtil;
import org.moflon.core.utilities.UncheckedCoreException;
import org.moflon.core.utilities.WorkspaceHelper;
import org.osgi.framework.BundleContext;

/**
 * The Activator controls the plug-in life cycle and contains state and functionality that can be used throughout the
 * plugin. Constants used in various places in the plugin should also be defined in the Activator.
 * 
 * Core (non gui) functionality that can be useful for other Moflon eclipse plugins should be implemented here.
 */
public class CoreActivator extends EMoflonPlugin
{
   private static final Logger logger = Logger.getLogger(CoreActivator.class);

   // The plug-in ID

   // Nature and builder IDs
   /**
    * @deprecated Use JavaCore.NATURE_ID directly (since eMoflon 2.2.1)
    */
   @Deprecated 
   public static final String JAVA_NATURE_ID = JavaCore.NATURE_ID;

   public static final String REPOSITORY_BUILDER_ID = "org.moflon.ide.core.runtime.builders.RepositoryBuilder";

   /**
    * @deprecated Use WorkspaceHelper.METAMODEL_NATURE_ID directly
    */
   @Deprecated 
   public static final String METAMODEL_NATURE_ID = "org.moflon.ide.core.runtime.natures.MetamodelNature";

   public static final String METAMODEL_BUILDER_ID = "org.moflon.ide.core.runtime.builders.MetamodelBuilder";

   public static final String INTEGRATION_BUILDER_ID = "org.moflon.ide.core.runtime.builders.IntegrationBuilder";

   public static final String ANTLR_NATURE_ID = "org.moflon.ide.core.runtime.natures.AntlrNature";

   public static final String ANTLR_BUILDER_ID = "org.moflon.ide.core.runtime.builders.AntlrBuilder";

   public static final String JAVA_WORKING_SET_ID = "org.eclipse.jdt.ui.JavaWorkingSetPage";

   private Map<String, Boolean> isDirty = new HashMap<>();

   private List<DirtyProjectListener> dirtyProjectListeners;
   
   private NatureMigrator natureMigrator;

   public static CoreActivator getDefault() {
      CoreActivator plugin = getPlugin(CoreActivator.class);
      if (plugin == null)
         throw new IllegalStateException("Plugin has not yet been set!");
      return plugin;
   }

   public static final String getModuleID()
   {
      return getDefault().getPluginId();
   }

   /**
    * Executed during plugin startup.
    * 
    * @see org.eclipse.core.runtime.Plugin#start(org.osgi.framework.BundleContext)
    */
   @Override
   public void start(final BundleContext context) throws Exception
   {
      super.start(context);

      dirtyProjectListeners = new ArrayList<>();

      natureMigrator = new NatureMigrator();
      WorkspaceTask.execute(new WorkspaceObservationLifecycleManager(natureMigrator), false);
   }

   @Override
   public void stop(final BundleContext context) throws Exception
   {
	   WorkspaceTask.execute(new WorkspaceObservationLifecycleManager(), false);
      dirtyProjectListeners = null;
      super.stop(context);
   }

   /**
    * Used when the plugin has to store resources on the client machine and eclipse installation + current workspace.
    * This location reserved for the plugin is called the "state location" and is usually in
    * pathToWorkspace/.metadata/pluginName
    * 
    * @param filename
    *           Appended to the state location. This is the name of the resource to be saved.
    * @return path to location reserved for the plugin which can be used to store resources
    */
   public IPath getPathInStateLocation(final String filename)
   {
      return getStateLocation().append(filename);
   }

   public boolean isDirty(final IProject project)
   {
      return project != null && isDirty(project.getName());
   }

   public boolean isDirty(final String projectName)
   {
      return isDirty.containsKey(projectName) ? isDirty.get(projectName) : false;
   }

   /**
    * Sets the projects dirty state.
    * 
    * A repository project is dirty if its corresponding metamodel project has been built recently so that new code
    * needs to be generated for this project. A metamodel project is dirty if its EAP file is newer than the generated
    * Moca tree (.temp/<project-name>.moca.xmi)
    * 
    * @param project
    * @param isDirty
    */
   public void setDirty(final IProject project, final boolean isDirty)
   {
      this.isDirty.put(project.getName(), isDirty);
      this.dirtyProjectListeners.forEach(l -> l.dirtyStateChanged(project, isDirty));
   }

   public void registerDirtyProjectListener(final DirtyProjectListener listener)
   {
      this.dirtyProjectListeners.add(listener);
   }

   /**
    * Adds a plugin-to-resource mapping to the global {@link URIConverter#URI_MAP}.
    * 
    * More precisely: If the given project is a plugin project, the following mapping is added to the map
    * 
    * platform:/plugin/[project-plugin-ID]/ to platform:/resource/[project-name]
    *  
    * @param project the project to be added
    */
   @Deprecated
   public static void addMappingForProject(final IProject project) 
   {
      if (project.isAccessible())
      {
         final IPluginModelBase pluginModel = PluginRegistry.findModel(project);
         if (pluginModel != null)
         {
            String pluginID = project.getName();

            if (pluginModel.getBundleDescription() != null)
               pluginID = pluginModel.getBundleDescription().getSymbolicName();

            final URI pluginURI = URI.createPlatformPluginURI(pluginID + "/", true);
            final URI resourceURI = URI.createPlatformResourceURI(project.getName() + "/", true);
            
            URIConverter.URI_MAP.put(pluginURI, resourceURI);
            
            logger.debug("Add mapping for project " + project.getName() + ": " + pluginURI + " -> " + resourceURI);
         }
      }

   }

	public static void createProblemMarker(final IResource resource, final String message,
			final int severity, final String location) {
		try {
			IMarker marker = resource.createMarker(IMarker.PROBLEM);
			marker.setAttribute(IMarker.MESSAGE, message);
			marker.setAttribute(IMarker.SEVERITY, severity);
			marker.setAttribute(IMarker.LOCATION, location);
		} catch (CoreException e) {
			throw new UncheckedCoreException(e);
		}
	}

	public static final void deleteMarkers(final IResource resource, final String type,
			final boolean includeSubtypes, final int depth) {
		try {
			resource.deleteMarkers(type, includeSubtypes, depth);
		} catch (CoreException e) {
			throw new UncheckedCoreException(e);
		}
	}
	
	public static final int convertStatusSeverityToMarkerSeverity(final int severity) throws CoreException {
		switch (severity) {
		case IStatus.ERROR:
			return IMarker.SEVERITY_ERROR;
		case IStatus.WARNING:
			return IMarker.SEVERITY_WARNING;
		case IStatus.INFO:
			return IMarker.SEVERITY_INFO;
		default:
			break;
		}
		final IStatus invalidSeverityConversion = new Status(IStatus.ERROR, CodeGeneratorPlugin.getModuleID(), "Cannot convert severity " + severity
				+ " to a marker");
		throw new CoreException(invalidSeverityConversion);
	}
	
	public static final IFile getEcoreFile(final IProject project) {
		final String ecoreFileName = MoflonUtil.getDefaultNameOfFileInProjectWithoutExtension(project.getName());
		return project.getFolder(WorkspaceHelper.MODEL_FOLDER).getFile(ecoreFileName + WorkspaceHelper.ECORE_FILE_EXTENSION);
	}
	
	public static final IProject[] getMetamodelProjects(final IProject[] projects) {
		final List<IProject> result = new ArrayList<IProject>(projects.length);
		for (final IProject project : projects) {
			if (project.isAccessible() && WorkspaceHelper.isMetamodelProjectNoThrow(project)) {
				result.add(project);
			}
		}
		return result.toArray(new IProject[result.size()]);
	}
	
	public static final IProject[] getRepositoryAndIntegrationProjects(final IProject[] projects) {
		final List<IProject> result = new ArrayList<IProject>(projects.length);
		for (final IProject project : projects) {
			if (project.isAccessible() && WorkspaceHelper.isMoflonProjectNoThrow(project)) {
				result.add(project);
			}
		}
		return result.toArray(new IProject[result.size()]);
	}
	
	public static final IBuildConfiguration[] getDefaultBuildConfigurations(final IProject[] projects) {
		final List<IBuildConfiguration> result =
				new ArrayList<IBuildConfiguration>(projects.length);
		for (int i = 0; i < projects.length; i++) {
			try {
				result.add(projects[i].getBuildConfig(IBuildConfiguration.DEFAULT_CONFIG_NAME));
			} catch (final CoreException e) {
				// Do nothing (i.e., ignore erroneous projects)
			}
		}
		return result.toArray(new IBuildConfiguration[result.size()]);
	}
}
