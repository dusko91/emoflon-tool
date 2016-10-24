package org.moflon2contiki.ide.core.utilities;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.moflon.codegen.eclipse.CodeGeneratorPlugin;
import org.moflon.codegen.eclipse.GenericMonitoredResourceLoader;
import org.moflon.codegen.eclipse.MonitoredMetamodelLoader;
import org.moflon.core.propertycontainer.MoflonPropertiesContainer;
import org.moflon.dependency.PackageRemappingDependency;


/**
 * Mimics {@link MonitoredMetamodelLoader}, it is needed to recreate this class because of the changed project natures.
 * In the {@link MonitoredMetamodelLoader} the isAccessible method is false for the new Natures.
 */
public class ContikiMonitoredMetamodelLoader extends GenericMonitoredResourceLoader {

	public ContikiMonitoredMetamodelLoader(ResourceSet resourceSet, IFile ecoreFile,
			MoflonPropertiesContainer moflonProperties) {
		super(resourceSet, ecoreFile/*, moflonProperties*/);
	}

	@Override
	protected void createResourcesForWorkspaceProjects(IProgressMonitor monitor) {
		   try
		      {
		         final IProject[] workspaceProjects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		         monitor.beginTask("Loading workspace projects", workspaceProjects.length);
		         for (IProject workspaceProject : workspaceProjects)
		         {
		            try
		            {
		               if (isAccessible(workspaceProject))
		               {
		                  final URI projectURI = CodeGeneratorPlugin.lookupProjectURI(workspaceProject);
		                  final URI metamodelURI = CodeGeneratorPlugin.getDefaultProjectRelativeEcoreFileURI(workspaceProject).resolve(projectURI);
		                  new PackageRemappingDependency(metamodelURI, false, false).getResource(resourceSet, false, true);
		               }
		            } catch (CoreException e)
		            {
		               // Do nothing
		            }
		            monitor.worked(1);
		         }
		      } finally
		      {
		         monitor.done();
		      }

	}

	@Override
	protected boolean isAccessible(IProject project) throws CoreException {
		return project.isAccessible();
	}

}
