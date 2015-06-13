package autobuildplugin;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.ResourcesPlugin;

public class WorkspaceAutoBuildHandler
{

   static IWorkspace workspace = ResourcesPlugin.getWorkspace();

   static IWorkspaceDescription description = workspace.getDescription();

   /**
    * Disables Eclipse Auto Build Process
    */
   public static void turnOffAutoBuild()
   {
      if (description.isAutoBuilding())
      {
         description.setAutoBuilding(false);
         try
         {
            workspace.setDescription(description);
         } catch (Exception e)
         {
            e.printStackTrace();
         }
      }
   }

   /**
    * Enables Eclipse Auto Build Process: Eclipse buildes automatically the project, whenever files have changed
    */
   public static void turnOnAutoBuild()
   {
      if (!description.isAutoBuilding())
      {
         description.setAutoBuilding(true);
         try
         {
            workspace.setDescription(description);
         } catch (Exception e)
         {
            e.printStackTrace();
         }
      }
   }

}
