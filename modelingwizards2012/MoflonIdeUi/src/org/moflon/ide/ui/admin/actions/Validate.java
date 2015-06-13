package org.moflon.ide.ui.admin.actions;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.moflon.ide.core.admin.WorkspaceHelper;
import org.moflon.ide.core.runtime.EMFCodegeneratorHelper;
import org.moflon.ide.core.runtime.builders.AbstractEcoreBuilder;
import org.moflon.ide.ui.UIActivator;

public class Validate implements IWorkbenchWindowActionDelegate
{
   public static final String SDM_ANNOTATION_SOURCE = "SDM";

   public static final String SDM_KEY = "XMI";

   private static final Logger logger = Logger.getLogger(Validate.class);

   private IProject ecoreProject;

   private IFile ecoreFile;

   @Override
   public void run(IAction action)
   {
      Job job = new Job("Validate") {
         @Override
         protected IStatus run(IProgressMonitor monitor)
         {
            IStatus status = null;
            try
            {
               if (ecoreFile != null)
               {
                  // Open monitor and start task
                  monitor.beginTask("Validation " + ecoreProject.getName(), 10 * WorkspaceHelper.PROGRESS_SCALE);

                  // Full-rebuild for the selected resource
                  logger.debug("full validation - triggered manually!");

                  ResourceSet resourceSet = new ResourceSetImpl();

                  // Load ecore file
                  URI fileURI = URI.createPlatformResourceURI(ecoreFile.getFullPath().toString(), true);
                  EMFCodegeneratorHelper.loadBuildPathDependencies(resourceSet, ecoreFile.getProject());
                  Resource ecoreResource = EMFCodegeneratorHelper.loadModel(fileURI, resourceSet).eResource();

                  status = AbstractEcoreBuilder.performValidation(monitor, ecoreResource, ecoreFile.getProject());
               } else
               {
                  logger.debug("No resource selected");
               }
               
               if(status.isOK())
                  UIActivator.showMessage("Validation Results", status.getMessage()); 
            } catch (OperationCanceledException e)
            {
               status = new Status(IStatus.CANCEL, UIActivator.PLUGIN_ID, IStatus.OK, "", null);
            } catch (CoreException e)
            {
               status = new Status(IStatus.CANCEL, UIActivator.PLUGIN_ID, IStatus.OK, "", null);
            } finally
            {
               monitor.done();
            }

            return status;
         }
      };

      job.setUser(true);
      job.schedule();
   }

   @Override
   public void selectionChanged(IAction action, ISelection selection)
   {
      try
      {
         // Check that an IFile is selected
         if (selection != null && selection instanceof StructuredSelection)
         {
            StructuredSelection fileSelection = (StructuredSelection) selection;

            // Assign files
            Object chosen = fileSelection.getFirstElement();

            if (chosen != null && chosen instanceof IFile)
            {
               ecoreFile = (IFile) fileSelection.getFirstElement();
               ecoreProject = ecoreFile.getProject();
            }
         } else
         {
            action.setEnabled(false);
            return;
         }
      } catch (Exception e)
      {
         e.printStackTrace();
      }

   }

   @Override
   public void dispose()
   {
      
   }

   @Override
   public void init(IWorkbenchWindow window)
   {
      
   }

}
