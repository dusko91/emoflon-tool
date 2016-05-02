package org.moflon.codegen.eclipse;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.moflon.core.utilities.WorkspaceHelper;
import org.moflon.dependency.PackageRemappingDependency;

import MoflonPropertyContainer.AdditionalDependencies;
import MoflonPropertyContainer.MoflonPropertiesContainer;

public class MonitoredMetamodelLoader extends GenericMonitoredResourceLoader {
	private static final String TASK_NAME = "Metamodel loading";

	private final MoflonPropertiesContainer moflonProperties;

	public MonitoredMetamodelLoader(final ResourceSet resourceSet, final IFile ecoreFile,
			final MoflonPropertiesContainer moflonProperties) {
		super(resourceSet, ecoreFile);
//		this.resourceSet.setPackageRegistry(new EPackageRegistryImpl(EPackage.Registry.INSTANCE));
//		this.resourceSet.setResourceFactoryRegistry(new ResourceFactoryRegistryImpl());
		this.moflonProperties = moflonProperties;
	}

	@Override
	public String getTaskName() {
		return TASK_NAME;
	}
	
	protected IStatus preprocessResourceSet(final IProgressMonitor monitor) {
		monitor.beginTask("Preprocessing resource set", 40);
		final IStatus preprocessingStatus = super.preprocessResourceSet(
				WorkspaceHelper.createSubMonitor(monitor, 15));
		if (preprocessingStatus.matches(IStatus.ERROR | IStatus.CANCEL)) {
			return preprocessingStatus;
		}
		
		// Always load Ecore metamodel
		PackageRemappingDependency ecoreMetamodelDependency = new PackageRemappingDependency(
				URI.createURI("platform:/plugin/org.eclipse.emf.ecore/model/Ecore.ecore"), true, true);
		ecoreMetamodelDependency.getResource(resourceSet, true);
		monitor.worked(5);
		if (monitor.isCanceled()) {
			return Status.CANCEL_STATUS;
		}

		// Create resources for the user-defined dependent metamodels
		final List<Resource> resourcesToLoad =
				createResourcesForUserDefinedMetamodels(WorkspaceHelper.createSubMonitor(monitor, 10));
		if (monitor.isCanceled()) {
			return Status.CANCEL_STATUS;
		}

		// Load the user-defined dependent metamodels
		final IStatus userDefinedMetamodelLoaderStatus =
				loadUserDefinedMetamodels(resourcesToLoad, WorkspaceHelper.createSubMonitor(monitor, 10));
		if (!userDefinedMetamodelLoaderStatus.isOK()) {
			return userDefinedMetamodelLoaderStatus;
		}
		if (monitor.isCanceled()) {
			return Status.CANCEL_STATUS;
		}
		return preprocessingStatus;
	}
	
	private final List<Resource> createResourcesForUserDefinedMetamodels(final IProgressMonitor monitor) {
		try {
			final List<Resource> resourcesToLoad = new LinkedList<Resource>();
			final List<AdditionalDependencies> additionalDependencies =
					moflonProperties.getAdditionalDependencies();

			monitor.beginTask("Creating resources for user-defined metamodels", additionalDependencies.size());
			for (final AdditionalDependencies userDefinedMetamodel : additionalDependencies) {
				final URI uri = URI.createURI(userDefinedMetamodel.getValue());
				final PackageRemappingDependency dependency = new PackageRemappingDependency(uri, true, false);
				resourcesToLoad.add(dependency.getResource(resourceSet, false));
				monitor.worked(1);
			}
			return resourcesToLoad;
		} finally {
			monitor.done();
		}
	}

	private final IStatus loadUserDefinedMetamodels(final List<Resource> resourcesToLoad,
			final IProgressMonitor monitor) {
		try {
			final MultiStatus resourceLoadingStatus =
					new MultiStatus(CodeGeneratorPlugin.getModuleID(), IStatus.OK,
							"Resource loading status", null);
			monitor.beginTask("Loading user-defined metamodels", resourcesToLoad.size());
			for (Resource resource : resourcesToLoad) {
				try {
					resource.load(null);
				} catch (final IOException e) {
					resourceLoadingStatus.add(new Status(IStatus.ERROR, CodeGeneratorPlugin.getModuleID(),
							IStatus.ERROR, e.getMessage(), e));
				}
				monitor.worked(1);
			}
			return resourceLoadingStatus;
		} finally {
			monitor.done();
		}
	}
}
