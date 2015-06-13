package org.moflon.autotest.core;

import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.team.internal.ui.wizards.ImportProjectSetOperation;
import org.eclipse.ui.IWorkingSet;
import org.moflon.Activator;

public class AutoTestHelper
{
   private static final Logger logger = Logger.getLogger(AutoTestHelper.class);

   public static boolean turnOffAutoBuild()
   {
      try
      {
         switchAutoBuilding(false);
      } catch (CoreException e)
      {
        logger.error("Unable to turn off auto build.");
        logger.error(Activator.displayExceptionAsString(e)); 
        
        return false;
      }
      
      return true;
   }

   public static void turnOnAutoBuild()
   {
      try
      {
         switchAutoBuilding(true);
      } catch (CoreException e)
      {
         logger.error("Unable to turn on auto build.");
         logger.error(Activator.displayExceptionAsString(e));
      }
   }

   private static void switchAutoBuilding(boolean on) throws CoreException
   {
      IWorkspace workspace = ResourcesPlugin.getWorkspace();
      IWorkspaceDescription description = workspace.getDescription();

      if (on)
      {
         if (!workspace.isAutoBuilding())
            description.setAutoBuilding(true);
      } else
      {
         if (workspace.isAutoBuilding())
            description.setAutoBuilding(false);
      }

      workspace.setDescription(description);
   }

   /*
    * 
    * 
    * PSF files
    */

   @SuppressWarnings("restriction")
   public static void importProjectSet(IProgressMonitor monitor, String absolutePathToPSFFile) throws InvocationTargetException, InterruptedException
   {
      ImportProjectSetOperation op = new ImportProjectSetOperation(null, absolutePathToPSFFile, new IWorkingSet[0]);
      op.run(monitor);
   }
}
