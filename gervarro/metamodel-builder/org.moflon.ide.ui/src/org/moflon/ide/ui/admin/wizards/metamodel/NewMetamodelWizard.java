package org.moflon.ide.ui.admin.wizards.metamodel;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.moflon.core.utilities.LogUtils;
import org.moflon.core.utilities.MoflonUtilitiesActivator;
import org.moflon.core.utilities.WorkspaceHelper;
import org.moflon.ide.core.CoreActivator;
import org.moflon.ide.ui.UIActivator;
import org.moflon.ide.ui.WorkspaceHelperUI;

/**
 * The new metamodel wizard creates a new metamodel project with default directory structure and default files.
 * 
 */
public class NewMetamodelWizard extends AbstractMoflonWizard
{
   // Page containing controls for taking user input
   private NewMetamodelProjectInfoPage projectInfo;

   private static Logger logger = Logger.getLogger(NewMetamodelWizard.class);

   private static final String SPECIFICATION_WORKINGSET_NAME = "Specifications";

   @Override
   public void addPages()
   {
      projectInfo = new NewMetamodelProjectInfoPage();
      addPage(projectInfo);
   }

   @Override
   protected void doFinish(final IProgressMonitor monitor) throws CoreException
   {
      try
      {
         final SubMonitor subMon = SubMonitor.convert(monitor, "Creating metamodel project", 8);

         String projectName = projectInfo.getProjectName();
         IPath location = projectInfo.getProjectLocation();

         // Create project
         IProject newProjectHandle = WorkspaceHelper.createProject(projectName, UIActivator.getModuleID(), location,
               subMon.split(1));

         // generate default files
         WorkspaceHelper.addFile(newProjectHandle, projectName + ".eap",
               MoflonUtilitiesActivator.getPathRelToPlugIn("resources/defaultFiles/EAEMoflon.eap", UIActivator.getModuleID()), UIActivator.getModuleID(),
               subMon.split(1));

         WorkspaceHelper.addFile(newProjectHandle, ".gitignore", ".temp", subMon.split(1));

         // Add Nature and Builders
         WorkspaceHelper.addNature(newProjectHandle, CoreActivator.METAMODEL_NATURE_ID, subMon.split(1));

         WorkspaceHelperUI.moveProjectToWorkingSet(newProjectHandle, SPECIFICATION_WORKINGSET_NAME);

         newProjectHandle.refreshLocal(IResource.DEPTH_INFINITE, subMon.split(1));

      } catch (Exception e)
      {
         LogUtils.error(logger, e, "Unable to add default EA project file.");
      }
   }

}
