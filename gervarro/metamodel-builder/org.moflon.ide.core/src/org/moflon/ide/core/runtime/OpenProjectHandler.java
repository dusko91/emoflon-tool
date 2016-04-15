package org.moflon.ide.core.runtime;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.emf.common.util.URI;
import org.gervarro.eclipse.workspace.util.WorkspaceTask;
import org.moflon.core.utilities.MoflonUtil;
import org.moflon.core.utilities.WorkspaceHelper;
import org.moflon.util.plugins.MetamodelProperties;
import org.moflon.util.plugins.manifest.ManifestFileUpdater;

import MoflonPropertyContainer.Dependencies;
import MoflonPropertyContainer.MoflonPropertiesContainer;
import MoflonPropertyContainer.MoflonPropertyContainerFactory;
import MoflonPropertyContainer.PropertiesValue;

public class OpenProjectHandler extends WorkspaceTask {
	private static final Logger logger = Logger.getLogger(OpenProjectHandler.class);

	private IProject project;

	private MetamodelProperties properties;
	
	private MoflonPropertiesContainer moflonProps;

	public OpenProjectHandler(final IProject project, final MetamodelProperties projectProperties, final MoflonPropertiesContainer moflonProps) {
		this.project = project;
		this.properties = projectProperties;
		this.moflonProps = moflonProps;
	}

	@Override
	public void run(final IProgressMonitor monitor) throws CoreException {
		try {
         updatePluginDependencies(project, properties, monitor);
         
         moflonProps.getDependencies().clear();
         properties.getDependenciesAsURIs().stream().forEach(dep -> addMetamodelDependency(moflonProps, dep));
          
         // These two metamodels are usually used directly or indirectly by most projects
         addMetamodelDependency(moflonProps, MoflonUtil.getDefaultURIToEcoreFileInPlugin(WorkspaceHelper.PLUGIN_ID_ECORE));     
         
         // TODO Additional standard dependencies for integration projects
//         if (properties.isIntegrationProject()) {
//            addMetamodelDependency(moflonProps, MoflonUtil.getDefaultURIToEcoreFileInPlugin(WorkspaceHelper.PLUGIN_ID_TGGRUNTIME));
//            addMetamodelDependency(moflonProps, MoflonUtil.getDefaultURIToEcoreFileInPlugin(WorkspaceHelper.PLUGIN_ID_SDMLANGUAGE));
//            addMetamodelDependency(moflonProps, MoflonUtil.getDefaultURIToEcoreFileInPlugin(WorkspaceHelper.PLUGIN_ID_TGGLANGUAGE));
//            addMetamodelDependency(moflonProps, MoflonUtil.getDefaultURIToEcoreFileInPlugin(WorkspaceHelper.PLUGIN_ID_MOCATREE));
//         }
      } catch (final IOException e) {
         e.printStackTrace();
      }
	}

	private void updatePluginDependencies(final IProject currentProject, final MetamodelProperties properties,
			final IProgressMonitor monitor) throws CoreException, IOException {
		monitor.beginTask("Updating plugin project", 5);

		logger.debug("Updating MANIFEST.MF");
		new ManifestFileUpdater().processManifest(
				currentProject,
				manifest -> {
					boolean changed = false;
					changed |= ManifestFileUpdater.updateDependencies(manifest, ManifestFileUpdater.extractDependencies(properties
							.get(MetamodelProperties.DEPENDENCIES_KEY)));

					// TODO Re-add this code
//					changed |= ManifestFileUpdater.updateDependencies(
//							manifest,
//							Arrays.asList(new String[]{
//							      WorkspaceHelper.PLUGIN_ID_ECORE,
//							      WorkspaceHelper.PLUGIN_ID_ECORE_XMI,
//									WorkspaceHelper.PLUGIN_ID_MOFLON_DEPENDENCIES
//									}));
//
//					try {
//						if (currentProject.hasNature(WorkspaceHelper.INTEGRATION_NATURE_ID))
//							changed |= ManifestFileUpdater.updateDependencies(manifest, 
//							      Arrays.asList(new String[]{
//							      WorkspaceHelper.PLUGIN_ID_MOCATREE,
//									WorkspaceHelper.PLUGIN_ID_SDMLANGUAGE, 
//									WorkspaceHelper.PLUGIN_ID_TGGLANGUAGE,
//									WorkspaceHelper.PLUGIN_ID_TGGRUNTIME}));
//					} catch (final Exception e) {
//						e.printStackTrace();
//					}
					return changed;
				});
		monitor.done();
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

	@Override
	public String getTaskName() {
		return "Handling open Moflon project";
	}

	@Override
	public ISchedulingRule getRule() {
		return project;
	}
}