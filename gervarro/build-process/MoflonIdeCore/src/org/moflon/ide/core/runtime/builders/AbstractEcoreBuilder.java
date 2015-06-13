package org.moflon.ide.core.runtime.builders;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.moflon.codegen.eclipse.MonitoredCodeGenerator;
import org.moflon.ide.core.CoreActivator;
import org.moflon.properties.MoflonProperties;
import org.moflon.util.WorkspaceHelper;

public abstract class AbstractEcoreBuilder extends AbstractBuilder
{

   private static final Logger logger = Logger.getLogger(AbstractEcoreBuilder.class);
   
   protected boolean generateSDMs = true;

   @Override
   protected void cleanResource(IProgressMonitor monitor) throws CoreException
   {
      monitor.beginTask("Cleaning " + getProject(), 1 * WorkspaceHelper.PROGRESS_SCALE);

      // Remove all problem markers
      getProject().deleteMarkers(IMarker.PROBLEM, false, IResource.DEPTH_INFINITE);

      // Remove generated code
      cleanFolderButKeepHiddenFiles(getProject().getFolder(WorkspaceHelper.GEN_FOLDER), new SubProgressMonitor(monitor, 1 * WorkspaceHelper.PROGRESS_SCALE));

      // Remove debug data
      cleanFolderButKeepHiddenFiles(getProject().getFolder("debug"), new SubProgressMonitor(monitor, 1 * WorkspaceHelper.PROGRESS_SCALE));

      // Remove generated model files
      cleanModels(getProject().getFolder("model"), new SubProgressMonitor(monitor, 1 * WorkspaceHelper.PROGRESS_SCALE));

      monitor.done();
   }

   protected void cleanFolderButKeepHiddenFiles(IFolder folder, IProgressMonitor monitor) throws CoreException
   {
      if (!folder.exists())
         return;

      monitor.beginTask("Inspecting " + folder.getName(), folder.members().length * WorkspaceHelper.PROGRESS_SCALE);

      for (IResource resource : folder.members())
      {
         // keep SVN data
         if (!resource.getName().startsWith("."))
         {
            if (resource.getType() == IResource.FOLDER)
            {
               cleanFolderButKeepHiddenFiles((IFolder) resource, new SubProgressMonitor(monitor, 1 * WorkspaceHelper.PROGRESS_SCALE));
            }
            resource.delete(true, new SubProgressMonitor(monitor, 1 * WorkspaceHelper.PROGRESS_SCALE));
         } else
            monitor.worked(1 * WorkspaceHelper.PROGRESS_SCALE);
      }
   }

   // Delete generated models within model folder
   private void cleanModels(IFolder folder, IProgressMonitor monitor) throws CoreException
   {
      monitor.beginTask("Inspecting " + folder.getName(), folder.members().length * WorkspaceHelper.PROGRESS_SCALE);

      for (IResource resource : folder.members())
      {
         // keep SVN data
         if (!resource.getName().startsWith("."))
         {
            // only delete generated models directly in folder 'model'
            if (resource.getType() != IResource.FOLDER)
               if (new MoflonProperties(getProject(), new SubProgressMonitor(monitor, 1 * WorkspaceHelper.PROGRESS_SCALE)).getReplaceGenModel())
                  if (resource.getName().endsWith(".genmodel") || resource.getName().endsWith(WorkspaceHelper.SUFFIX_GEN_ECORE)
                        || resource.getName().endsWith(IntegrationBuilder.SUFFIX_SMA))
                     resource.delete(true, new SubProgressMonitor(monitor, 1 * WorkspaceHelper.PROGRESS_SCALE));
         } else
            monitor.worked(1 * WorkspaceHelper.PROGRESS_SCALE);
      }
   }

   @Override
   protected boolean processResource(IProgressMonitor monitor) throws CoreException
   {
      MonitoredCodeGenerator codeGenerationTask = new MonitoredCodeGenerator(getEcoreFile());

      IStatus status = codeGenerationTask.run(monitor);
      if (!status.isOK())
      {
         throw new CoreException(status);
      }

      WorkspaceHelper.addEMFDependenciesToClassPath(WorkspaceHelper.createSubMonitor(monitor), getProject());
      getEcoreFile().getProject().refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());

      CoreActivator.addMappingForProject(getProject());

      return true;
   }

   private void createMarkersForMissingEcoreFile(IFile ecoreFile) throws CoreException
   {
      logger.error("Unable to generate code: " + ecoreFile + " does not exist in project!");

      // Create marker
      IMarker marker = getProject().createMarker(IMarker.PROBLEM);
      marker.setAttribute(IMarker.MESSAGE, "Cannot find: " + ecoreFile.getProjectRelativePath().toString());
      marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
      marker.setAttribute(IMarker.LOCATION, ecoreFile.getProjectRelativePath().toString());
   }

   protected IFile getEcoreFile() throws CoreException
   {
      IFile ecoreFile = getProject().getFolder(WorkspaceHelper.MODEL_FOLDER).getFile(getProject().getName() + WorkspaceHelper.ECORE_FILE_EXTENSION);
      if (!ecoreFile.exists())
         createMarkersForMissingEcoreFile(ecoreFile);

      return ecoreFile;
   }

   @Override
   public boolean visit(IResource resource) throws CoreException
   {
      IPath pathToResource = resource.getProjectRelativePath();

      // Make sure changes are from the right ecore file according to convention
      if (pathToResource.equals(new Path(WorkspaceHelper.MODEL_FOLDER + WorkspaceHelper.SEPARATOR + resource.getProject().getName()
            + WorkspaceHelper.ECORE_FILE_EXTENSION)))
      {
         // Only generate code if resource wasn't deleted!
         if (resource.exists())
         {
            logger.info("Build due to changes to: " + resource);
            return processResource(subMonitor);
         }
      }

      return false;
   }

   @Override
   public boolean visit(IResourceDelta delta) throws CoreException
   {
      // Get changes and call visit on all
      boolean buildSuccessful = false;
      IResourceDelta[] changes = delta.getAffectedChildren();
      for (int i = 0; i < changes.length; i++)
      {
         buildSuccessful = visit(changes[i].getResource());
         visit(changes[i]);
      }
      return buildSuccessful;
   }

}
