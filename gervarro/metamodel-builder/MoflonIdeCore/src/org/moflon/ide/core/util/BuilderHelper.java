package org.moflon.ide.core.util;

import java.util.Collection;
import java.util.Collections;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspace.ProjectOrder;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.jobs.Job;
import org.moflon.MoflonDependenciesPlugin;
import org.moflon.util.WorkspaceHelper;

public class BuilderHelper
{
   private static final Logger logger = Logger.getLogger(BuilderHelper.class);

   public static boolean turnOffAutoBuild()
   {
      try
      {
         switchAutoBuilding(false, Collections.<String> emptyList());
      } catch (CoreException e)
      {
         logger.error("Unable to turn off auto build.");
         logger.error(MoflonDependenciesPlugin.displayExceptionAsString(e));

         return false;
      }

      return true;
   }

   public static void turnOnAutoBuild(Collection<String> pluginIDs)
   {
      try
      {
         switchAutoBuilding(true, pluginIDs);
      } catch (CoreException e)
      {
         logger.error("Unable to turn on auto build.");
         logger.error(MoflonDependenciesPlugin.displayExceptionAsString(e));
      }
   }

   private static void switchAutoBuilding(final boolean on, Collection<String> pluginIDs) throws CoreException
   {
      IWorkspace workspace = ResourcesPlugin.getWorkspace();
      IWorkspaceDescription description = workspace.getDescription();

      if (on)
      {
         if (!workspace.isAutoBuilding())
         {
            description.setAutoBuilding(true);
         }
      } else
      {
         if (workspace.isAutoBuilding())
            description.setAutoBuilding(false);
      }

      workspace.setDescription(description);
   }

   public static void generateCodeInOrder(final IProgressMonitor monitor, Collection<IProject> projects)
   {
//      try
//      {
//         Job.getJobManager().join(ResourcesPlugin.FAMILY_AUTO_BUILD, monitor);
//         ProjectOrder order = ResourcesPlugin.getWorkspace().computeProjectOrder(projects.stream().toArray(IProject[]::new));
//         for (IProject project : order.projects)
//         {
//            if (project.hasNature(WorkspaceHelper.INTEGRATION_NATURE_ID))
//               new IntegrationCodeGenerator(project).generateCode(monitor);
//            else if (project.hasNature(WorkspaceHelper.REPOSITORY_NATURE_ID))
//               new RepositoryCodeGenerator(project).generateCode(monitor);
//         }
//      } catch (CoreException | OperationCanceledException | InterruptedException e)
//      {
//         e.printStackTrace();
//      }
   }
}
