package org.moflon.ide.core.tasks;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.moflon.core.utilities.LogUtils;
import org.moflon.core.utilities.UtilityClassNotInstantiableException;

/**
 * This class contains utility methods related Eclipse workspace tasks.
 * 
 * @author Roland Kluge - Initial implementation
 */
public class TaskUtilities
{

   private static final Logger logger = Logger.getLogger(TaskUtilities.class);

   private TaskUtilities()
   {
      new UtilityClassNotInstantiableException();
   }

   /**
    * Processes the given queue of jobs (in order) by scheduling one job after the other.
    * 
    * Autobuilding is switched off before processing the queue and reset to its original state after processing the queue has completed
    * @param jobs
    * @throws CoreException
    */
   public static void processJobQueue(final List<Job> jobs) throws CoreException
   {
      if (jobs.size() > 0)
      {
         final boolean isAutoBuilding = TaskUtilities.switchAutoBuilding(false);
         final JobChangeAdapter jobExecutor = new JobChangeAdapter() {

            @Override
            public void done(final IJobChangeEvent event)
            {
               final IStatus result = event.getResult();
               if (result.isOK() && !jobs.isEmpty())
               {
                  final Job nextJob = jobs.remove(0);
                  nextJob.addJobChangeListener(this);
                  nextJob.schedule();
                  return;
               }
               try
               {
                  // Only do something if auto-building flags differ
                  if (isAutoBuilding ^ ResourcesPlugin.getWorkspace().isAutoBuilding())
                  {
                     TaskUtilities.switchAutoBuilding(isAutoBuilding);
                  }
               } catch (CoreException e)
               {
                  LogUtils.error(logger, e);
               }
            }
         };
         final Job firstJob = jobs.remove(0);
         firstJob.addJobChangeListener(jobExecutor);
         firstJob.schedule();
      }
   }

   /**
    * Tries to set the Auto Build flag of the workspace to newAutoBuildValue.
    * 
    * @param newAutoBuildValue the desired new auto-building flag state
    * @return the previous auto-building flag
    * @throws CoreException
    */
   public static final boolean switchAutoBuilding(final boolean newAutoBuildValue) throws CoreException
   {
      final IWorkspace workspace = ResourcesPlugin.getWorkspace();
      final IWorkspaceDescription description = workspace.getDescription();
      final boolean oldAutoBuildValue = description.isAutoBuilding();
      if (oldAutoBuildValue ^ newAutoBuildValue)
      {
         description.setAutoBuilding(newAutoBuildValue);
         workspace.setDescription(description);
      }
      return oldAutoBuildValue;
   }
}
