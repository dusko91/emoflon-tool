package org.moflon.ide.ui.admin.actions;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.moflon.ide.core.runtime.BayreuthMetrics;
import org.moflon.ide.ui.UIActivator;
import org.moflon.util.WorkspaceHelper;

public class AnalyzeProjectAction implements IWorkbenchWindowActionDelegate
{

   private static final Logger logger = Logger.getLogger(AnalyzeProjectAction.class);
   
   private static final String DEBUG_FILE_EXTENSION = "ctr";
   
   IResource selectedResource;
   
   @Override
   public void run(IAction action)
   {
      Job job = new Job("Calculating metrics") {         
         @Override
         protected IStatus run(IProgressMonitor monitor)
         {            
            IStatus status = null;
            try
            {                
               if (selectedResource != null && selectedResource.getFileExtension().equals(DEBUG_FILE_EXTENSION)) {
                  // Open monitor and start task
                  monitor.beginTask("Loading Fujaba project " + selectedResource.getName(), 3 * WorkspaceHelper.PROGRESS_SCALE);
                  
                  // Load model
                  BayreuthMetrics.getInstance().calculateMetrics(selectedResource, monitor);
                                                   
                  // Refresh file explorer and set status to OK
                  selectedResource.getParent().refreshLocal(IResource.DEPTH_INFINITE, new SubProgressMonitor(monitor, 1 * WorkspaceHelper.PROGRESS_SCALE));
                  status= new Status(IStatus.OK, UIActivator.PLUGIN_ID, IStatus.OK, "", null);
                  monitor.worked(1 * WorkspaceHelper.PROGRESS_SCALE);
               }else{
                  logger.info("Tried to calculate metrics for an inappropriate file.");
               }
            } catch (OperationCanceledException e)
            {
               status = new Status(IStatus.CANCEL, UIActivator.PLUGIN_ID, IStatus.OK, "", null);
            } catch (CoreException e)
            {
               logger.error("Could not refresh resource");  
               e.printStackTrace();
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
      Object obj = null;

      // Check if selection is valid (i.e., inside Package Explorer or
      // Navigator
      if (selection instanceof TreeSelection) {
         obj = ((TreeSelection) selection).getFirstElement();
      } else if (selection instanceof StructuredSelection) {
         obj = ((StructuredSelection) selection).getFirstElement();
      }

      // Get what the user selected in the workspace as a resource
      if (obj != null && obj instanceof IResource) {
         selectedResource = (IResource) obj;
      } else {
         selectedResource = null;
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
