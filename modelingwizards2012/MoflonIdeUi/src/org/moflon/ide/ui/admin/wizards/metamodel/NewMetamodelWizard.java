package org.moflon.ide.ui.admin.wizards.metamodel;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.internal.ui.workingsets.IWorkingSetIDs;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.IWorkingSetManager;
import org.eclipse.ui.PlatformUI;
import org.moflon.Activator;
import org.moflon.ide.core.CoreActivator;
import org.moflon.ide.core.admin.WorkspaceHelper;
import org.moflon.ide.ui.UIActivator;

/**
 * The new metamodel wizard creates a new metamodel project with default directory structure and default files. 
 * 
 * @author aanjorin
 * @author (last editor) $Author$
 * @version $Revision$ $Date$
 */
@SuppressWarnings("restriction")
public class NewMetamodelWizard extends Wizard implements INewWizard
{
   // Page containing controls for taking user input
   private NewMetamodelProjectInfoPage projectInfo;

   private static Logger logger = Logger.getLogger(UIActivator.class);

   private static final String SPECIFICATION_WORKINGSET_NAME = "Specifications"; 
   
   public NewMetamodelWizard()
   {
      setNeedsProgressMonitor(true);
   }

   @Override
   public void init(IWorkbench workbench, IStructuredSelection selection)
   {
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
         public void run(IProgressMonitor monitor) throws InvocationTargetException
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

   private void doFinish(IProgressMonitor monitor) throws CoreException
   {
      String projectName = projectInfo.getProjectName();
      
      // Start task with 6 work units
      monitor.beginTask("", 6);

      // Create project
      IProject newProjectHandle = WorkspaceHelper.createProject(projectName, UIActivator.PLUGIN_ID, monitor);
      monitor.worked(2);

      // Add empty EA project file
      try
      {
         if (projectInfo.mocaSupport()) {
            WorkspaceHelper.addFile(newProjectHandle, projectName + ".eap",
                  Activator.getPathRelToPlugIn("resources/defaultFiles/EAMoca.eap", UIActivator.PLUGIN_ID), UIActivator.PLUGIN_ID, monitor);
            newProjectHandle.setPersistentProperty(WorkspaceHelper.MOCA_SUPPORT, "true");
         }
         else {
            WorkspaceHelper.addFile(newProjectHandle, projectName + ".eap",
                  Activator.getPathRelToPlugIn("resources/defaultFiles/EABase.eap", UIActivator.PLUGIN_ID), UIActivator.PLUGIN_ID, monitor);
            newProjectHandle.setPersistentProperty(WorkspaceHelper.MOCA_SUPPORT, "false");
         }
         
         if(projectInfo.textual()){
            // TODO Dont create eap if its textual
            WorkspaceHelper.addFolder(newProjectHandle, "MOSL", monitor);
            WorkspaceHelper.addFolder(newProjectHandle, "MOSL/defaultIntegration", monitor);
            WorkspaceHelper.addFile(newProjectHandle, "MOSL/defaultIntegration/schema.sch",Activator.getPathRelToPlugIn("resources/defaultFiles/schema.sch", UIActivator.PLUGIN_ID), UIActivator.PLUGIN_ID, monitor);
            WorkspaceHelper.addFolder(newProjectHandle, "MOSL/defaultIntegration/tggRules", monitor);
         }
        
               
      } catch (Exception e)
      {
         logger.error("Unable to add default EA project file: " + e);
         e.printStackTrace();
      }
      monitor.worked(2);

      // Add Nature and Builders
      WorkspaceHelper.addNature(newProjectHandle, CoreActivator.METAMODEL_NATURE_ID, monitor);
      monitor.worked(2);

      // Add Nature and Builders for MOSL
      if(projectInfo.textual())
         WorkspaceHelper.addNature(newProjectHandle, CoreActivator.MOSL_NATURE_ID, monitor);
      
      // Move project to appropriate working set
      IWorkingSetManager workingSetManager = PlatformUI.getWorkbench().getWorkingSetManager();
      IWorkingSet workingSet = workingSetManager.getWorkingSet(SPECIFICATION_WORKINGSET_NAME);
      if (workingSet == null)
      {
         workingSet = workingSetManager.createWorkingSet(SPECIFICATION_WORKINGSET_NAME, new IAdaptable[] {newProjectHandle});
         workingSet.setId(IWorkingSetIDs.JAVA);
         workingSetManager.addWorkingSet(workingSet);
      }else{
         // Add current contents of WorkingSet
         ArrayList<IAdaptable> newElements = new ArrayList<IAdaptable>();
         for (IAdaptable element : workingSet.getElements())
            newElements.add(element);
         
         // Add newly created project
         newElements.add(newProjectHandle);
         
         // Set updated contents
         IAdaptable[] newElementsArray = new IAdaptable[newElements.size()];
         workingSet.setElements(newElements.toArray(newElementsArray));
      }
   
      monitor.done();
   }
}
