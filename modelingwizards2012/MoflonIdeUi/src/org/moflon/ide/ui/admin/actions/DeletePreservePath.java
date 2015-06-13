package org.moflon.ide.ui.admin.actions;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
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
import org.moflon.ide.core.admin.MoflonProperties;
import org.moflon.ide.core.admin.WorkspaceHelper;
import org.moflon.ide.core.runtime.builders.AbstractEcoreBuilder;
import org.moflon.ide.ui.UIActivator;

public class DeletePreservePath implements IWorkbenchWindowActionDelegate
{
	   private static final Logger logger = Logger.getLogger(DeletePreservePath.class);
	   
	   IResource selectedResource;

	   @Override
	   public void selectionChanged(IAction action, ISelection selection) {
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
	   public void run(IAction action)
	   {
	      Job job = new Job("Delete from preserve paths") {

	         @Override
	         protected IStatus run(IProgressMonitor monitor)
	         { 	        	 
	            IStatus status = null;
	            try
	            {
	               if (selectedResource != null)
	               {
	                  IProject project = selectedResource.getProject();
	                  IPath resourcePath = selectedResource.getFullPath().makeRelativeTo(project.getParent().getFullPath());
	                  MoflonProperties moflonProperties = new MoflonProperties(project, new SubProgressMonitor(monitor, 1 * WorkspaceHelper.PROGRESS_SCALE));
	                  
	                 
	                  // Open monitor and start task
	                  monitor.beginTask("Remove " + resourcePath, 10 * WorkspaceHelper.PROGRESS_SCALE);
	                 
	                  
	                  // Clean for the selected resource
	                  
	                  logger.debug("Delete from preserve path: " + resourcePath.toString());
                  
	                  moflonProperties.removeFromPreservePath("/" + resourcePath.toString());
	                  
	                  logger.debug("Delete complete");
	               } else
	               {
	                  logger.debug("No resource selected");
	               }
	               
	               status= new Status(IStatus.OK, UIActivator.PLUGIN_ID, IStatus.OK, "", null);
	            } catch (OperationCanceledException e)
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
      public void dispose()
      {
         // TODO Auto-generated method stub
         
      }

      @Override
      public void init(IWorkbenchWindow window)
      {
         // TODO Auto-generated method stub
         
      }
}
