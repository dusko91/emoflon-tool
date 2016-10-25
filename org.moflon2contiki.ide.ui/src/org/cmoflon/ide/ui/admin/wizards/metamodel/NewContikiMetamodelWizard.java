package org.cmoflon.ide.ui.admin.wizards.metamodel;

import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;
import org.cmoflon.ide.core.CMoflonCoreActivator;
import org.cmoflon.ide.core.utilities.CMoflonWorkspaceHelper;
import org.cmoflon.ide.ui.CMoflonUIActivator;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.moflon.core.utilities.MoflonUtilitiesActivator;
import org.moflon.ide.ui.UIActivator;
import org.moflon.ide.ui.admin.wizards.metamodel.NewMetamodelProjectInfoPage;

/**
 * The new metamodel wizard creates a new contiki metamodel project with default directory structure and default files.
 * @author David Giessing
 */
public class NewContikiMetamodelWizard extends Wizard implements IWorkbenchWizard
{
   // Page containing controls for taking user input
   private NewMetamodelProjectInfoPage projectInfo;

   private static Logger logger = Logger.getLogger(CMoflonUIActivator.class);

   private static final String SPECIFICATION_WORKINGSET_NAME = "Specifications";

   public NewContikiMetamodelWizard()
   {
      setNeedsProgressMonitor(true);
   }

   @Override
   public void addPages()
   {
      projectInfo = new NewMetamodelProjectInfoPage();
      addPage(projectInfo);
   }

   @Override
   public boolean performFinish()
   {
      IRunnableWithProgress op = new IRunnableWithProgress() {
         @Override
         public void run(final IProgressMonitor monitor) throws InvocationTargetException
         {
            try
            {
               doFinish(monitor);
            } catch (CoreException e)
            {
               throw new InvocationTargetException(e);
            } finally
            {
               monitor.done();
            }
         }
      };

      try
      {
         getContainer().run(true, false, op);
      } catch (InterruptedException e)
      {
         return false;
      } catch (InvocationTargetException e)
      {
         Throwable realException = e.getTargetException();
         MessageDialog.openError(getShell(), "Error", realException.getMessage());
         return false;
      }

      return true;
   }

   private void doFinish(final IProgressMonitor monitor) throws CoreException
   {
      try
      {
         monitor.beginTask("Creating metamodel project", 8);

         String projectName = projectInfo.getProjectName();
         IPath location = projectInfo.getProjectLocation();

         // Create project
         IProject newProjectHandle = CMoflonWorkspaceHelper.createProject(projectName, CMoflonUIActivator.getModuleID(), location , CMoflonWorkspaceHelper.createSubmonitorWith1Tick(monitor));

         // generate default files
         CMoflonWorkspaceHelper.addFile(newProjectHandle, projectName + ".eap",
               MoflonUtilitiesActivator.getPathRelToPlugIn("resources/defaultFiles/EAEMoflon.eap",UIActivator.getModuleID()), UIActivator.getModuleID(),
               CMoflonWorkspaceHelper.createSubmonitorWith1Tick(monitor));

         CMoflonWorkspaceHelper.addFile(newProjectHandle, ".gitignore", ".temp", CMoflonWorkspaceHelper.createSubmonitorWith1Tick(monitor));         
         
         // Add Nature and Builders
         CMoflonWorkspaceHelper.addNature(newProjectHandle, CMoflonCoreActivator.METAMODEL_NATURE_ID, CMoflonWorkspaceHelper.createSubmonitorWith1Tick(monitor));

         //Moflon2ContikiWorkspaceHelper.addNature(newProjectHandle, CoreActivator.JAVA_NATURE_ID, Moflon2ContikiWorkspaceHelper.createSubmonitorWith1Tick(monitor));
         
         CMoflonWorkspaceHelper.moveProjectToWorkingSet(newProjectHandle, SPECIFICATION_WORKINGSET_NAME);

         newProjectHandle.refreshLocal(IResource.DEPTH_INFINITE, CMoflonWorkspaceHelper.createSubmonitorWith1Tick(monitor));

      } catch (Exception e)
      {
         logger.error("Unable to add default EA project file: " + e);
         e.printStackTrace();
      } finally
      {
         monitor.done();
      }
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		
	}
}
