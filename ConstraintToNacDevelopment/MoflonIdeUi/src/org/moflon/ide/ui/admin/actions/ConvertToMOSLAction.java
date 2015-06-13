package org.moflon.ide.ui.admin.actions;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.moflon.ide.core.CoreActivator;
import org.moflon.ide.core.runtime.builders.MOSLBuilder;
import org.moflon.ide.ui.UIActivator;
import org.moflon.util.WorkspaceHelper;

public class ConvertToMOSLAction extends AbstractCleanAndBuildClasses 
{
	   private static final Logger logger = Logger.getLogger(ConvertToMOSLAction.class);
	   
	   @Override
	   public void run(IAction action)
	   {
         if (!MessageDialog.openConfirm(Display.getCurrent().getActiveShell(), "Beta status", "This action creates the MOSL project in the folder '_MOSL'. The EAP file is not deleted and the project nature is not yet converted, since this functionaliy is not stable! Continue?")) {
            return;
         }
         
	      Job job = new Job("Converting") {

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
	                  monitor.beginTask("Converting " + project.getName(), 10 * WorkspaceHelper.PROGRESS_SCALE);

                     MOSLBuilder.convertEAPProjectToMOSL(project);
                     
                     project.refreshLocal(IProject.DEPTH_INFINITE, monitor);  
                     WorkspaceHelper.addNature(project, CoreActivator.MOSL_NATURE_ID, monitor);
	                  logger.debug("Convert done");
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
