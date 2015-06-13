package autobuildplugin;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jdt.junit.launcher.JUnitLaunchShortcut;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.team.internal.ui.wizards.ImportProjectSetOperation;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkingSet;

import autobuildplugin.preferences.PreferenceConstants;

@SuppressWarnings("restriction")
public class MoflonWorkspace
{
   static MoflonWorkspace m;

   IWorkbenchWindow window;

   public MoflonWorkspace(IWorkbenchWindow window)
   {
      this.window = window;
   }

   public static void run(IWorkbenchWindow window)
   {
      if (m == null)
         m = new MoflonWorkspace(window);
      m.startAutoBuild();
   }

   private void startAutoBuild()
   {
      if (isPerformMoflonWorkspace())
      {
         ProgressMonitorDialog dialog = new ProgressMonitorDialog(window.getShell());
         try
         {
            dialog.run(true, true, new IRunnableWithProgress() {
               @Override
               public void run(IProgressMonitor monitor)
               {
                  if (monitor.isCanceled())
                     return;
                  startBuildProcess(monitor);
               }
            });
         } catch (Exception e)
         {
            e.printStackTrace();
         }
      }
   }

   /**
    * Performing all 5 steps to generate and test the eap files
    * 
    * @param monitor
    */
   private void startBuildProcess(IProgressMonitor monitor)
   {
      String nextop = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.AB_NEXTOP);
      monitor.beginTask("auto build process", 100);
      switch (PreferenceConstants.getNextOp(nextop))
      {
      case DELETEWORKSPACE:
         deletingOldProjects(monitor);
      case IMPORTPROJECTSET:
         importingProjectSet(monitor);
      case MOFLONBUILD:
         buildingEAP(monitor);
      case REFRESH:
         refreshWorkSpace(monitor);
      default:
         WorkspaceAutoBuildHandler.turnOffAutoBuild();
         runJUnitTest(monitor);
         WorkspaceAutoBuildHandler.turnOnAutoBuild();
      }
      monitor.done();
   }

   /**
    * Graphical Dialog, which pops up to ask the user, if the Moflon AutoBuild Process should start
    * 
    * @return
    */
   private boolean isPerformMoflonWorkspace()
   {
      String[] buttons = { "Start", "Cancel" };
      Dialog start = new MessageDialog(window.getShell(), "Start Moflon Auto Build Process?", null, Activator.getDefault().getPreferenceStore()
            .getString(PreferenceConstants.AB_NEXTOP)
            + "?", MessageDialog.QUESTION_WITH_CANCEL, buttons, 0);
      start.setBlockOnOpen(true);
      int returnCode = start.open();
      return (returnCode == MessageDialog.OK);
   }

   /**
    * Importing all projects of the choosen project set. The used *.psf file is specified over the preference menu.
    * 
    * @param monitor
    * @param store
    */
   private void importingProjectSet(IProgressMonitor monitor)
   {
      monitor.subTask("Importing project set ...");
      ImportProjectSetOperation op = new ImportProjectSetOperation(null, Activator.getDefault().getPreferenceStore()
            .getString(PreferenceConstants.AB_PROJECTSET), new IWorkingSet[0]);
      try
      {
         op.run(monitor);
      } catch (Exception e)
      {
         e.printStackTrace();
      }
      monitor.worked(30);
   }

   /**
    * Looks up every project in the workspace for *.eap files, which are used to generate the eap models
    * 
    * @param monitor
    * @param store
    */
   private void buildingEAP(IProgressMonitor monitor)
   {

      monitor.subTask("Exporting Ecore models from EAP files ...");
      try
      {
         URL pathToExe = Activator.getPathRelToPlugIn("/commandLineExeAndJunitTest/MOFLON2EAExportImportTest.exe", Activator.PLUGIN_ID);
         File exe = new File(pathToExe.getPath());
         IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
         IProject[] projects = root.getProjects();
         
         for (IProject iProject : projects)
         {
            if(iProject.isOpen() && iProject.hasNature("org.moflon.ide.ui.runtime.natures.MetamodelNature")){
               // Check if snapshot of ecore files is available (this must be the case when EAP is under VC in EA)
               IFolder snapshot = iProject.getFolder("snapshot");
               if(snapshot.exists()){
                  copyEcoreFilesFromSnapshot(monitor, iProject, snapshot);
               }else
                  exportEcoreFilesFromEAP(monitor, exe, iProject);
            }
         }
      } catch (Exception e)
      {
         e.printStackTrace();
      }
      
      monitor.worked(20);
   }

   private void copyEcoreFilesFromSnapshot(IProgressMonitor monitor, IProject iProject, IFolder snapshot) throws CoreException
   {
      // Create temp folder if necessary
      IFolder temp = iProject.getFolder(".temp");
      if(!temp.exists())
         temp.create(true, true, monitor);
      else{
    	 temp.delete(true, monitor);
    	 temp.create(true, true, monitor);
      }
      
      // Copy all files from snapshot (we know this exists!) to temp 
      for (IResource file : snapshot.members())
      {
         if(file.getType() == IResource.FILE)
            file.copy(temp.getFullPath().append(file.getName()), true, monitor);
      }
   }

   private void exportEcoreFilesFromEAP(IProgressMonitor monitor, File exe, IProject iProject) throws IOException, InterruptedException
   {
      IFile eap = iProject.getFile(iProject.getName().concat(".eap"));
      Runtime rt = Runtime.getRuntime();
      Process pr = rt.exec("\"" + exe.getAbsolutePath() + "\"" + " -e --eap " + "\"" + eap.getLocation() + "\"");
      monitor.subTask(iProject.getName());
      pr.waitFor();
   }

   /**
    * Refreshes the workspace, enables the eclipse autobuild process until this operation is finished to generate the
    * models by the moflon EAAddin.
    * 
    * @param monitor
    * @param store
    */
   private void refreshWorkSpace(IProgressMonitor monitor)
   {

      IWorkspace ws = ResourcesPlugin.getWorkspace();
      try
      {
         ws.getRoot().refreshLocal(IResource.DEPTH_INFINITE, monitor);
         WorkspaceAutoBuildHandler.turnOnAutoBuild();
         try
         {
            monitor.subTask("Refreshing workspace ...");
            Job.getJobManager().join(ResourcesPlugin.FAMILY_AUTO_BUILD, monitor);
         } catch (OperationCanceledException e)
         {
            e.printStackTrace();
         } catch (InterruptedException e)
         {
            e.printStackTrace();
         }
         WorkspaceAutoBuildHandler.turnOffAutoBuild();
         ws.getRoot().refreshLocal(IResource.DEPTH_INFINITE, monitor);
      } catch (Exception e)
      {
         e.printStackTrace();
      }
      monitor.worked(20);

   }

   /**
    * Looking up for a project with the name 'EclipseTestSuite' and perfoming this project as junit test suite.
    * 
    * @param monitor
    * @param store
    */
   private void runJUnitTest(IProgressMonitor monitor)
   {
      monitor.subTask("Running JUnit testsuite ...");

      IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
      IProject[] projects = root.getProjects();
      IProject testProject = null;
      for (int i = 0; i < projects.length; i++)
      {
         if (projects[i].getName().compareTo("EclipseTestSuite") == 0)
         {
            testProject = projects[i];
         }
      }

      ILaunchShortcut shortcut = new JUnitLaunchShortcut();
      if (testProject != null)
      {
         shortcut.launch(new StructuredSelection(testProject), ILaunchManager.RUN_MODE);
      }

   }

   /**
    * Deleting all projects in the workspace, which doesn#t start with a dot ('.').
    * 
    * @param root
    * @param monitor
    */
   private void deleteFolder(IProgressMonitor monitor)
   {
      IWorkspaceRoot root2 = ResourcesPlugin.getWorkspace().getRoot();
      IProject[] projects = root2.getProjects();
      for (int i = 0; i < projects.length; i++)
      {
         if (!projects[i].getName().startsWith("."))
         {
            try
            {
               projects[i].delete(true, true, monitor);
            } catch (Exception e)
            {
               e.printStackTrace();
            }
         }
         monitor.worked(1);
      }
   }

   /**
    * Deleting all projects of the current workspace, expect projects, which starts with a '.' in the name
    * 
    * @param monitor
    * @param store
    */
   private void deletingOldProjects(IProgressMonitor monitor)
   {

      monitor.subTask("Deleting projects ...");
      deleteFolder(monitor);
      monitor.worked(10);
      try
      {
         ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, monitor);
      } catch (Exception e)
      {
         e.printStackTrace();
      }

   }

}
