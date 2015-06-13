package org.moflon.ide.ui.admin.wizards.metamodel;

import static org.moflon.ide.ui.admin.wizards.util.WizardUtil.loadStringTemplateGroup;
import static org.moflon.ide.ui.admin.wizards.util.WizardUtil.renderTemplate;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.antlr.stringtemplate.StringTemplateGroup;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.workingsets.IWorkingSetIDs;
import org.eclipse.jdt.junit.JUnitCore;
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
import org.moflon.ide.ui.UIActivator;
import org.moflon.util.MoflonUtil;
import org.moflon.util.WorkspaceHelper;

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

      try
      {
         // Add directory folders MOSL and imports file
         if (projectInfo.textual())
         {
            WorkspaceHelper.addFolder(newProjectHandle, "MOSL", monitor);

            // if User opts to generate the demo: import demo, build demo, import JUnit, test demo
            if (projectInfo.eMoflonDemo())
            {
               // Import MOSL files
               String findURL = "resources/defaultFiles/MOSL/";
               IPath destination = newProjectHandle.findMember("MOSL").getLocation();
               copyResource(findURL, destination);

               // Import JUnit and Java files into user workspace
               importJUnitIntoProject(monitor, newProjectHandle);
            } else
            {
               // No demo selected: create working set folder and load empty constraints
               WorkspaceHelper.addFolder(newProjectHandle, "MOSL/MyWorkingSet", monitor);

               StringTemplateGroup stg = loadStringTemplateGroup("/resources/mosl/templates/imports.stg");

               Map<String, Object> attrs = new HashMap<String, Object>();
               String content = renderTemplate(stg, "imports", attrs);

               WorkspaceHelper.addFile(newProjectHandle, "MOSL/_imports.mconf", content, monitor);
            }
         } else
         {
            if (projectInfo.eMoflonDemo())
            {
               // copy the pre-made demo .eap into user's .eap
               WorkspaceHelper.addFile(newProjectHandle, projectName + ".eap",
                     Activator.getPathRelToPlugIn("resources/defaultFiles/Demo.eap", UIActivator.PLUGIN_ID), UIActivator.PLUGIN_ID, monitor);

               // Import JUnit and Java files into user workspace
               importJUnitIntoProject(monitor, newProjectHandle);

            } else
            {
               // no demo: generate default files
               WorkspaceHelper.addFile(newProjectHandle, projectName + ".eap",
                     Activator.getPathRelToPlugIn("resources/defaultFiles/EAEMoflon.eap", UIActivator.PLUGIN_ID), UIActivator.PLUGIN_ID, monitor);
            }
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
      if (projectInfo.textual())
         WorkspaceHelper.addNature(newProjectHandle, CoreActivator.MOSL_NATURE_ID, monitor);

      // Move project to appropriate working set
      IWorkingSetManager workingSetManager = PlatformUI.getWorkbench().getWorkingSetManager();
      IWorkingSet workingSet = workingSetManager.getWorkingSet(SPECIFICATION_WORKINGSET_NAME);
      if (workingSet == null)
      {
         workingSet = workingSetManager.createWorkingSet(SPECIFICATION_WORKINGSET_NAME, new IAdaptable[] { newProjectHandle });
         workingSet.setId(IWorkingSetIDs.JAVA);
         workingSetManager.addWorkingSet(workingSet);
      } else
      {
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

   private void importJUnitIntoProject(IProgressMonitor monitor, IProject newProjectHandle) throws CoreException,
         JavaModelException, IOException
   {
      // Refresh and build folder before deriving JUnit test
      newProjectHandle.refreshLocal(IResource.DEPTH_INFINITE, monitor);
      newProjectHandle.build(IncrementalProjectBuilder.FULL_BUILD, monitor);

      IProject newTestSuiteProjectHandle = WorkspaceHelper.createProject("DemoTestSuite", UIActivator.PLUGIN_ID, monitor);

      IJavaProject javaProj = WorkspaceHelper.setUpAsJavaProject(newTestSuiteProjectHandle, monitor);

      // setup the build path
      IJavaProject newRepositoryProject = JavaCore.create(newProjectHandle.getWorkspace().getRoot().getProject("DoubleLinkedListLanguage"));

      WorkspaceHelper.setProjectOnBuildpath(javaProj, newRepositoryProject, monitor);

      WorkspaceHelper.setContainerOnBuildPath(newTestSuiteProjectHandle, JUnitCore.JUNIT4_CONTAINER_PATH.toString());

      // Copy Java file (Junit)
      String findURL = "resources/defaultFiles/Testsuite/";
      IPath destination = newTestSuiteProjectHandle.findMember("src").getLocation();
      copyResource(findURL, destination);

      // Refresh the folder so User can see new files and JUnit can test
      newProjectHandle.refreshLocal(IResource.DEPTH_INFINITE, monitor);
      newTestSuiteProjectHandle.refreshLocal(IResource.DEPTH_INFINITE, monitor);
   }


   private void copyResource(String sourceURL, IPath destination) throws IOException
   {
      URL url = org.moflon.Activator.getPathRelToPlugIn(sourceURL, UIActivator.PLUGIN_ID);

      MoflonUtil.copyDirToDir(url, destination.toFile(), new FileFilter() {
         @Override
         public boolean accept(File pathname)
         {
            return true;
         }
      });
   }

}
