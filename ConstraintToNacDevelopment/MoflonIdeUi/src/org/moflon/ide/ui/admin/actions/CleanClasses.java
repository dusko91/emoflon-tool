package org.moflon.ide.ui.admin.actions;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.moflon.util.WorkspaceHelper;
import org.moflon.ide.ui.UIActivator;

public class CleanClasses extends AbstractCleanAndBuildClasses
{
	   private static final Logger logger = Logger.getLogger(CleanClasses.class);
	   
	   @Override
	   public void run(IAction action)
	   {
	      Job job = new Job("Cleaning") {

	         @Override
	         protected IStatus run(IProgressMonitor monitor)
	         { 	        	 
	            IStatus status = null;
	            try
	            {
	               if (selectedResource != null)
	               {
	                  IProject project = selectedResource.getProject();

	                  // Open monitor and start task
	                  monitor.beginTask("Cleaning " + project.getName(), 10 * WorkspaceHelper.PROGRESS_SCALE);

	                  // Clean for the selected resource
	                  logger.debug("cleaning project - triggered manually!");
                  
	                  // Clean project
	                  project.build(IncrementalProjectBuilder.CLEAN_BUILD, WorkspaceHelper.createSubMonitor(monitor));
	                  
	                  logger.debug("clean complete");
	               } else
	               {
	                  logger.debug("No resource selected");
	               }
	               
	               status= new Status(IStatus.OK, UIActivator.PLUGIN_ID, IStatus.OK, "", null);
	            } catch (OperationCanceledException e)
	            {
	               status = new Status(IStatus.CANCEL, UIActivator.PLUGIN_ID, IStatus.OK, "", null);
	            } catch (CoreException e)
	            {
	               status = new Status(IStatus.ERROR, UIActivator.PLUGIN_ID, IStatus.OK, "", e);
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
}
