package org.moflon.ide.ui.admin.actions;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.moflon.ide.core.admin.WorkspaceHelper;
import org.moflon.ide.ui.UIActivator;

/**
 * This class implements the action for rebuilding a complete project. Therefore, a clean is initialized and afterwards
 * a build command is triggered.
 */
public class RebuildClasses extends AbstractCleanAndBuildClasses
{
   private static final Logger logger = Logger.getLogger(RebuildClasses.class);


   @Override
   public void run(IAction action)
   {
      Job job = new Job("Building") {

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
                  monitor.beginTask("Codegeneration for " + project.getName(), 10 * WorkspaceHelper.PROGRESS_SCALE);

                  // Full-rebuild for the selected resource
                  logger.debug("a new full rebuild from genmodel - triggered manually!");

                  // Build from metamodel
                  project.build(IncrementalProjectBuilder.FULL_BUILD, new SubProgressMonitor(monitor, 10 * WorkspaceHelper.PROGRESS_SCALE));
                  
                  // Compile Java code (necessary if auto-build is disabled)
                  project.build(IncrementalProjectBuilder.INCREMENTAL_BUILD, new SubProgressMonitor(monitor, 10 * WorkspaceHelper.PROGRESS_SCALE));
                  
                  
                  logger.debug("rebuild complete");
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
