package org.moflon.autotest.core;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.junit.launcher.JUnitLaunchShortcut;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.moflon.Activator;
import org.moflon.autotest.AutoTestActivator;
import org.moflon.ide.core.CoreActivator;
import org.moflon.ide.core.console.MoflonConsole;
import org.moflon.ide.core.runtime.builders.MOSLBuilder;
import org.moflon.util.WorkspaceHelper;
import org.moflon.util.eMoflonEMFUtil;

public class AutoTestController
{
   private static final Logger logger = Logger.getLogger(AutoTestController.class);
   
   private static final String ERROR_MESSAGE_PROBLEMS_REFRESHING = "I'm having problems refreshing the workspace.";

   private static final String ERROR_MESSAGE_LATEST_VERSIONS = "Do you have the latest version of EA and of our eMoflon EA Addin?";

   private static final String ERROR_MESSAGE_PROBLEMS_EA_EXPORT = "I'm having problems executing the EA export for ";

   private static final String ERROR_MESSAGE_PROBLEMS_SNAPSHOT = "I'm having problems copying the ecore files from the snapshot in ";

   private static final String ERROR_MESSAGE_UNABLE_TO_EXPORT_EAPS = "I was unable to export all models from the EAP files in your workspace.";

   private static final String ERROR_MESSAGE_SVNKIT = "(1) Ensure you have switched to SVNKit (Window/Preferences/Team/SVN) or make sure JavaHL is working.";

   private static final String ERROR_MESSAGE_CLEAN_WORKSPACE = "(2) If possible, start with a clean Workspace without any projects.  "
         + "Although the PSF import offers to delete the projects this does not always work, especially on Windows.";

   private static final String ERROR_MESSAGE_ACCESS = "(3) Are you sure you have acess to all the projects (if they do not support anonymous access)?";

   private static final String ERROR_MESSAGE_OUTDATED_PSF = "(4) The PSF file might be outdated - please check for an update of the test plugin";

   private static final String ERROR_MESSAGE_SERVER_DOWN = "(5) If it's quite late in the night, our server might be down performing a back-up - try again in a few hours.";

   private static final String ERROR_MESSAGE_NO_IDEA = "(6) What nothing helped?!  Please send us an email at contact@moflon.org :)";

   private static final String ERROR_MESSAGE_PROBLEMS = "If you did not explicitly cancel then please check the following (most probable first):";

   private static final String ERROR_MESSAGE_UNABLE_TO_CHECK_OUT_PROJECTS = "Sorry, I was unable to check out the projects in the PSF file.";
   
   private static final String ERROR_MESSAGE_UNABLE_TO_ADD_MOSL_NATURE = "Sorry, I was unable to add the MOSL Nature to all projects.";

   private static final String INFO_MESSAGE_FINISHED_AUTO_TEST_PROCESS = "Finished auto-test process - Good bye!";
   
   private static final String INFO_MESSAGE_BEGIN_CONVERTING_FROM_EAP_TO_MOSL = "Begin converting from exported EAP to MOSL Syntax for ";

   private static final String INFO_MESSAGE_CONVERTED_EAP_TO_MOSL = "Converted EAP to MOSL Syntax for ";
   
   private static final String INFO_MESSAGE_FINISHED_BUILDING_NOW_RUNNING_TESTS = "Finished building workspace...  Running tests if any test projects according to our naming convention exist (*TestSuite*)";

   private static final String INFO_MESSAGE_REFRESHING = "Now refreshing and turning auto build back on to invoke normal code generation (build) process ...";

   private static final String INFO_MESSAGE_ECORE_FILES_EXPORTED = "Great!  All model (.ecore) files have been exported ...";

   private static final String INFO_MESSAGE_PROJECTS_CHECKED_OUT = "All projects have now been checked out ...";

   private static final String INFO_MESSAGE_SWITCHED_OFF_AUTO_BUILD = "Ok - I was able to switch off auto build ...";

   private static final String PATH_TO_DEVELOPER_PSF = "/resources/PSFs/developerWorkspace.psf";

   private static final String PATH_TO_TESTWORKSPACE_MISC_PSF = "/resources/PSFs/testsuiteWorkspace.psf";

   private static final String PATH_TO_TESTWORKSPACE_TGG_0_PSF = "/resources/PSFs/tggTestSuiteWorkspace0.psf";

   private static final String PATH_TO_TESTWORKSPACE_TGG_1_PSF = "/resources/PSFs/tggTestSuiteWorkspace1.psf";

   private static final String PATH_TO_TRANSFORMATIONZOO_0_PSF = "/resources/PSFs/transformationZoo0.psf";

   private static final String PATH_TO_TRANSFORMATIONZOO_1_PSF = "/resources/PSFs/transformationZoo1.psf";

   private static final String PATH_TO_DEMOCLES_0_PSF = "/resources/PSFs/democles0.psf";
   
   private IWorkbenchWindow window;
   
   public AutoTestController(IWorkbenchWindow window)
   {
      this.window = window;
   }

   public void installDevWorkspace(String displayName)
   {
      installWorkspace(PATH_TO_DEVELOPER_PSF, displayName);
   }

   public void installTestWorkspaceMisc(String displayName)
   {
      installWorkspace(PATH_TO_TESTWORKSPACE_MISC_PSF, displayName);
   }

   public void installTestWorkspaceTGG_0(String displayName)
   {
      installWorkspace(PATH_TO_TESTWORKSPACE_TGG_0_PSF, displayName);
   }

   public void installTestWorkspaceTGG_1(String displayName)
   {
      installWorkspace(PATH_TO_TESTWORKSPACE_TGG_1_PSF, displayName);
   }
   
   public void installTestWorkspaceTextual(String displayName)
   {
	   try{
	      //get all newest moca.xmi files from EAP 
		   if(exportModelsFromEAPFilesInWorkspace()){
		      //converting all EAP Projects
		      Collection<IProject> projects = getProjectsByNatureID(CoreActivator.METAMODEL_NATURE_ID);
			   convertProjectsToMOSLProjects(projects);
			   
			   //Build new and test
			   refreshAndBuildWorkspace();
			   runJUnitTests();
		   }
	   }catch (CoreException e){
		   logger.error(Activator.displayExceptionAsString(e));
	   }
   }

   private void installWorkspace(String pluginRelativePathToPSF, String displayName)
   {
      logger.info("Installing " + displayName + "...");

      if (AutoTestHelper.turnOffAutoBuild())
      {
         logger.info(INFO_MESSAGE_SWITCHED_OFF_AUTO_BUILD);

         if (checkOutProjectsWithPSFFile(pluginRelativePathToPSF))
         {
            logger.info(INFO_MESSAGE_PROJECTS_CHECKED_OUT);

            if (exportModelsFromEAPFilesInWorkspace())
            {
               logger.info(INFO_MESSAGE_ECORE_FILES_EXPORTED);

               logger.info(INFO_MESSAGE_REFRESHING);

               refreshAndBuildWorkspace();
               
               logger.info(INFO_MESSAGE_FINISHED_BUILDING_NOW_RUNNING_TESTS);
               
               runJUnitTests();

               logger.info(INFO_MESSAGE_FINISHED_AUTO_TEST_PROCESS);
            }
         }
      }
   }

   private void runJUnitTests()
   {
      IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
      IProject[] projects = root.getProjects();
      Collection<IProject> testProjects = new ArrayList<IProject>();
      for (int i = 0; i < projects.length; i++)
      {
         if (isTestProjectAccordingToConvention(projects[i]))
         {
            testProjects.add(projects[i]);
         }
      }

      ILaunchShortcut shortcut = new JUnitLaunchShortcut();
      for (IProject testProject : testProjects)
      {
         shortcut.launch(new StructuredSelection(testProject), ILaunchManager.RUN_MODE);
         logger.info("Found " + testProject + " and ran tests...");
      }
   }

   private boolean isTestProjectAccordingToConvention(IProject project)
   {
      try
      {
         return project.getName().contains("TestSuite") && project.getNature(JavaCore.NATURE_ID) != null;
      } catch (CoreException e)
      {
         return false;
      }
   }

   private boolean checkOutProjectsWithPSFFile(final String pluginRelativePathToPSF)
   {      
      try
      {
         ProgressMonitorDialog dialog = new ProgressMonitorDialog(window.getShell());

         dialog.run(true, true, new IRunnableWithProgress() {
            @Override
            public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
            {
               final IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
               if (projects.length > 0){
                  Display.getDefault().syncExec(new Runnable() {
                     @Override
                     public void run()
                     {
                        if(MessageDialog.openConfirm(null, "There are projects in the workspace!", "Should I delete them for you?")){
                           for (IProject project : projects)
                           {
                              try
                              {
                                 logger.info("Deleting " + project.getName() + "...");
                                 project.delete(true, monitor);
                              } catch (CoreException e)
                              {
                                 logger.error("Sorry - I was unable to clean up your workspace. I'll continue anyway...");
                              } 
                           }
                        }
                     }
                  });

               }

               
               URL fullPathURL = Activator.getPathRelToPlugIn(pluginRelativePathToPSF, AutoTestActivator.PLUGIN_ID);
               String fullPath = new File(fullPathURL.getPath()).getAbsolutePath();

               logger.info("Checking out projects using PSF file: " + fullPath);

               AutoTestHelper.importProjectSet(monitor, fullPath);
            }
         });
      } catch (Exception e)
      {
         logger.error(ERROR_MESSAGE_UNABLE_TO_CHECK_OUT_PROJECTS);
         logger.error(ERROR_MESSAGE_PROBLEMS);
         logger.error(ERROR_MESSAGE_SVNKIT);
         logger.error(ERROR_MESSAGE_CLEAN_WORKSPACE);
         logger.error(ERROR_MESSAGE_ACCESS);
         logger.error(ERROR_MESSAGE_OUTDATED_PSF);
         logger.error(ERROR_MESSAGE_SERVER_DOWN);
         logger.error(ERROR_MESSAGE_NO_IDEA);

         logger.error("Exception: " + Activator.displayExceptionAsString(e));

         return false;
      }

      return true;
   }

   private Collection<IProject> getProjectsByNatureID(String natureID) throws CoreException{
	   IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
	   HashSet<IProject> projects = new HashSet<>();
	   for(IProject project : root.getProjects()){
           if (project.isOpen() && project.hasNature(natureID)){
        	   projects.add(project);
           }
	   }	   
	   return projects;
   }
   
   private boolean exportModelsFromEAPFilesInWorkspace()
   {
      try
      {
         IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
         for (IProject project : root.getProjects())
         {
            if (project.isOpen() && project.hasNature(CoreActivator.METAMODEL_NATURE_ID))
            {
               logger.info("Exporting EAP file in " + project + "...");

               // Check if snapshot of ecore files is available (this must be the case when EAP is under VC in EA)
               IFolder snapshot = project.getFolder("snapshot");
               if (snapshot.exists())
               {
                  copyEcoreFilesFromSnapshot(project, snapshot);
               } else
                  exportEcoreFilesFromEAP(project);
            }
         }
      } catch (CoreException e)
      {
         logger.error(ERROR_MESSAGE_UNABLE_TO_EXPORT_EAPS);
         logger.error(Activator.displayExceptionAsString(e));

         return false;
      }

      return true;
   }

   private void copyEcoreFilesFromSnapshot(final IProject project, final IFolder snapshot)
   {
      ProgressMonitorDialog dialog = new ProgressMonitorDialog(window.getShell());

      try
      {
         dialog.run(true, true, new IRunnableWithProgress() {
            @Override
            public void run(IProgressMonitor monitor) throws InterruptedException
            {
               try
               {
                  // Create .temp folder for export
                  IFolder temp = project.getFolder(".temp");
                  if (temp.exists())
                     temp.delete(true, monitor);

                  temp.create(true, true, monitor);

                  // Copy all files from snapshot (== export)
                  for (IResource file : snapshot.members())
                  {
                     if (file.getType() == IResource.FILE)
                        file.copy(temp.getFullPath().append(file.getName()), true, monitor);
                  }
               } catch (Exception e)
               {
                  throw new InterruptedException(e.getMessage());
               }
            }
         });
      } catch (Exception e)
      {
         logger.error(ERROR_MESSAGE_PROBLEMS_SNAPSHOT + project.getName());
         logger.error(Activator.displayExceptionAsString(e));
      }
   }

   private void exportEcoreFilesFromEAP(IProject project)
   {
      try
      {
         EnterpriseArchitectHelper.delegateToEnterpriseArchitect(project);
      } catch (IOException | InterruptedException e)
      {
         logger.error(ERROR_MESSAGE_PROBLEMS_EA_EXPORT + project.getName());
         logger.info(ERROR_MESSAGE_LATEST_VERSIONS);
         logger.error(Activator.displayExceptionAsString(e));
      }
   }



   private boolean refreshAndBuildWorkspace()
   {
      ProgressMonitorDialog dialog = new ProgressMonitorDialog(window.getShell());

      try
      {
         dialog.run(true, true, new IRunnableWithProgress() {
            @Override
            public void run(IProgressMonitor monitor) throws InterruptedException
            {
               try
               {
                  IWorkspace ws = ResourcesPlugin.getWorkspace();

                  ws.getRoot().refreshLocal(IResource.DEPTH_INFINITE, monitor);
                  Job.getJobManager().join(ResourcesPlugin.FAMILY_MANUAL_REFRESH, monitor);

                  AutoTestHelper.turnOnAutoBuild();
                  Job.getJobManager().join(ResourcesPlugin.FAMILY_AUTO_BUILD, monitor);

                  ws.getRoot().refreshLocal(IResource.DEPTH_INFINITE, monitor);
                  Job.getJobManager().join(ResourcesPlugin.FAMILY_MANUAL_REFRESH, monitor);
               } catch (CoreException e)
               {
                  throw new InterruptedException(e.getMessage());
               }

            }
         });
      } catch (Exception e)
      {
         logger.error(ERROR_MESSAGE_PROBLEMS_REFRESHING);
         logger.error(Activator.displayExceptionAsString(e));
         
         e.printStackTrace();

         return false;
      }

      return true;
   }
   
   private boolean convertProjectsToMOSLProjects(final Collection<IProject> projects)
   {
      ProgressMonitorDialog dialog = new ProgressMonitorDialog(window.getShell());
      try
      {
         dialog.run(true, true, new IRunnableWithProgress() {
            @Override
            public void run(IProgressMonitor monitor) throws InterruptedException
            {
               try
               {
                  if (projects != null)
                  {
                     for (IProject project : projects)
                     {
                        logger.info(INFO_MESSAGE_BEGIN_CONVERTING_FROM_EAP_TO_MOSL + project.getName());
                        WorkspaceHelper.addNature(project, CoreActivator.MOSL_NATURE_ID, monitor);
                        MOSLBuilder.convertEAPProjectToMOSL(project);
                        logger.info(INFO_MESSAGE_CONVERTED_EAP_TO_MOSL + project.getName());                        
                     }
                  }
               } catch (CoreException e)
               {
                  throw new InterruptedException(e.getMessage());
               }

            }
         });
      } catch (Exception e)
      {
         logger.error(ERROR_MESSAGE_UNABLE_TO_ADD_MOSL_NATURE);
         logger.error(Activator.displayExceptionAsString(e));

         e.printStackTrace();

         return false;
      }

      return true;
   }

   public void installTransformationZoo_0(String displayName)
   {
      installWorkspace(PATH_TO_TRANSFORMATIONZOO_0_PSF, displayName);
   }

   public void installTransformationZoo_1(String displayName)
   {
      installWorkspace(PATH_TO_TRANSFORMATIONZOO_1_PSF, displayName);
   }

   public void installTestWorkspaceDemocles_0(String displayName)
   {
      installWorkspace(PATH_TO_DEMOCLES_0_PSF, displayName);
   }
}
