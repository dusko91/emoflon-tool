package org.moflon.codegen.eclipse;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.moflon.core.utilities.WorkspaceHelper;
import org.moflon.dependency.PackageRemappingDependency;
import org.moflon.eclipse.job.IMonitoredJob;

public final class NewMonitoredMetamodelLoader implements IMonitoredJob {
	private static final String TASK_NAME = "Metamodel loading";

	private final ResourceSet resourceSet;
	private final IFile file;
	private Resource resource;
	private List<Resource> resources; 

	public NewMonitoredMetamodelLoader(final ResourceSet resourceSet, final IFile file) {
		this.resourceSet = resourceSet;
		this.file = file;
	}

	@Override
	public final IStatus run(final IProgressMonitor monitor) {
		try {
			monitor.beginTask(TASK_NAME + " task", 25);
			final IProject project = file.getProject();
			monitor.subTask("Loading metamodel for project " + project.getName());
			monitor.worked(5);

			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}

			try {
				// Prepare plugin to resource URI mapping
				CodeGeneratorPlugin.createPluginToResourceMapping(resourceSet,
						WorkspaceHelper.createSubMonitor(monitor, 5));
			} catch (final CoreException e) {
				return new Status(IStatus.ERROR, CodeGeneratorPlugin.getModuleID(), e.getMessage(), e);
			}

			// Create (unloaded) resources for all possibly dependent metamodels in Moflon-specific workspace projects
			createResourcesForWorkspaceProjects(WorkspaceHelper.createSubMonitor(monitor, 10));
			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}
			
			// Always load Ecore metamodel
			PackageRemappingDependency ecoreMetamodelDependency = new PackageRemappingDependency(
					URI.createURI("platform:/plugin/org.eclipse.emf.ecore/model/Ecore.ecore"), true, true);
			ecoreMetamodelDependency.getResource(resourceSet, true, false);

			// Load the file
			URI projectURI = URI.createPlatformResourceURI(project.getName() + "/", true);
			URI uri = URI.createURI(file.getProjectRelativePath().toString()).resolve(projectURI);
			resource = resourceSet.getResource(uri, true);

			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}

			// Resolve cross-references
			final CrossReferenceResolver crossReferenceResolver =
					new CrossReferenceResolver(resource);
			crossReferenceResolver.run(WorkspaceHelper.createSubMonitor(monitor, 5));
			resources = crossReferenceResolver.getResources();
			
            // Remove unloaded resources from resource set
            final List<Resource> resources = resourceSet.getResources();
            for (int i = 0; i < resources.size(); i++) {
            	final Resource resource = resources.get(i);
            	if (!resource.isLoaded()) {
            		resources.remove(i--);
            	}
            }

			return CodeGeneratorPlugin.validateResourceSet(resourceSet, TASK_NAME,
					WorkspaceHelper.createSubMonitor(monitor, 5));
		} finally {
			monitor.done();
		}
	}
	
	public final List<Resource> getResources() {
		return resources;
	}

	public final Resource getMainResource() {
		return resource;
	}

	@Override
	public final String getTaskName() {
		return TASK_NAME;
	}

	protected void createResourcesForWorkspaceProjects(final IProgressMonitor monitor) {
		try {
			final IProject[] workspaceProjects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
			monitor.beginTask("Loading workspace projects", workspaceProjects.length);
			for (final IProject workspaceProject : workspaceProjects) {
				if (isAccessible(workspaceProject)) {
					final URI projectURI = CodeGeneratorPlugin.lookupProjectURI(workspaceProject);
					final URI metamodelURI = 
							CodeGeneratorPlugin.getDefaultProjectRelativeEcoreFileURI(workspaceProject).resolve(projectURI);
					new PackageRemappingDependency(metamodelURI, false, false).getResource(resourceSet, false, true);
				}
				monitor.worked(1);
			}
		} finally {
			monitor.done();
		}
	}

	protected boolean isAccessible(IProject project) {
		try {
			return project.isAccessible() && (project.hasNature(WorkspaceHelper.REPOSITORY_NATURE_ID) ||
					project.hasNature(WorkspaceHelper.INTEGRATION_NATURE_ID));
		} catch (final CoreException e) {
			return false;
		}
	}
}
