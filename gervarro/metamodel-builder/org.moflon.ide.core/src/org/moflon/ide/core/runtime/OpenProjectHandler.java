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
import org.moflon.TGGLanguageActivator;
import org.moflon.core.moca.tree.MocaTreePlugin;
import org.moflon.core.utilities.MoflonUtil;
import org.moflon.core.utilities.WorkspaceHelper;
import org.moflon.sdm.language.SDMLanguagePlugin;
import org.moflon.tgg.runtime.TGGRuntimePlugin;
import org.moflon.util.plugins.MetamodelProperties;
import org.moflon.util.plugins.manifest.ManifestFileUpdater;

import MoflonPropertyContainer.Dependencies;
import MoflonPropertyContainer.MoflonPropertiesContainer;
import MoflonPropertyContainer.MoflonPropertyContainerFactory;
import MoflonPropertyContainer.PropertiesValue;

public class OpenProjectHandler extends WorkspaceTask {
	private static final Logger logger = Logger.getLogger(OpenProjectHandler.class);

	private IProject project;
	private MetamodelProperties metamodelProperties;
	private MoflonPropertiesContainer moflonProperties;

	public OpenProjectHandler(final IProject project,
			final MetamodelProperties metamodelProperties,
			final MoflonPropertiesContainer moflonProperties) {
		this.project = project;
		this.metamodelProperties = metamodelProperties;
		this.moflonProperties = moflonProperties;
	}

	@Override
	public void run(final IProgressMonitor monitor) throws CoreException {
		try {
			updatePluginDependencies(project, metamodelProperties, monitor);

			moflonProperties.getDependencies().clear();
			metamodelProperties.getDependenciesAsURIs().stream().forEach(dep -> addMetamodelDependency(moflonProperties, dep));

			// These two metamodels are usually used directly or indirectly by most projects
			addMetamodelDependency(moflonProperties, MoflonUtil.getDefaultURIToEcoreFileInPlugin(WorkspaceHelper.PLUGIN_ID_ECORE));     

			if (metamodelProperties.isIntegrationProject()) {
				addMetamodelDependency(moflonProperties, MoflonUtil.getDefaultURIToEcoreFileInPlugin(TGGRuntimePlugin.getDefault().getPluginId()));
				addMetamodelDependency(moflonProperties, MoflonUtil.getDefaultURIToEcoreFileInPlugin(SDMLanguagePlugin.getDefault().getPluginId()));
				addMetamodelDependency(moflonProperties, MoflonUtil.getDefaultURIToEcoreFileInPlugin(TGGLanguageActivator.getDefault().getPluginId()));
				addMetamodelDependency(moflonProperties, MoflonUtil.getDefaultURIToEcoreFileInPlugin(MocaTreePlugin.getDefault().getPluginId()));
			}
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

	public void addMetamodelDependency(final MoflonPropertiesContainer moflonProperties, final URI metamodelUri) {
		Dependencies dep = MoflonPropertyContainerFactory.eINSTANCE.createDependencies();
		dep.setValue(metamodelUri.toString());
		if (!alreadyContainsDependency(moflonProperties.getDependencies(), dep)) {
			moflonProperties.getDependencies().add(dep);
		}
	}

	private boolean alreadyContainsDependency(final Collection<? extends PropertiesValue> dependencies, final PropertiesValue addDep) {
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
