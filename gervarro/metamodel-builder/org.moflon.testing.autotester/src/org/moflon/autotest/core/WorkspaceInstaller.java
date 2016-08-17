package org.moflon.autotest.core;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IBuildConfiguration;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.core.JavaModelManager;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.team.internal.ui.wizards.ImportProjectSetOperation;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.IWorkingSetManager;
import org.eclipse.ui.PlatformUI;
import org.moflon.autotest.AutoTestActivator;
import org.moflon.core.utilities.LogUtils;
import org.moflon.core.utilities.MoflonUtil;
import org.moflon.core.utilities.MoflonUtilitiesActivator;
import org.moflon.core.utilities.WorkspaceHelper;
import org.moflon.ide.core.CoreActivator;
import org.moflon.ide.workspaceinstaller.psf.EMoflonStandardWorkspaces;
import org.moflon.ide.workspaceinstaller.psf.PSFPlugin;
import org.moflon.ide.workspaceinstaller.psf.PsfFileUtils;

@SuppressWarnings("restriction")
public class WorkspaceInstaller
{
   private static final String JUNIT_TEST_LAUNCHER_FILE_NAME_PATTERN = "^.*[Test|TestSuite].*[.]launch$";

   private static final Logger logger = Logger.getLogger(WorkspaceInstaller.class);

   public void installWorkspaceByName(final String workspaceName)
   {
      final List<String> path = EMoflonStandardWorkspaces.getPathToPsfFileForWorkspace(workspaceName);
      if (!path.isEmpty())
      {
         this.installPluginRelativePsfFiles(path, workspaceName);
      } else
      {
         logger.debug("Not a recognized workspace: " + workspaceName);
      }
   }

   public void installPsfFiles(final List<File> psfFiles)
   {
      final String label = joinBasenames(psfFiles);
      installPsfFiles(psfFiles, label);
   }

   private void installPluginRelativePsfFiles(final Collection<String> pluginRelativePsfFiles, final String label)
   {
      prepareWorkspace();

      installPsfFiles(mapToAbsoluteFiles(pluginRelativePsfFiles), label);
   }

   private static String mapToAbsolutePath(final String pluginRelativePathToPSF)
   {
      final IProject workspaceProject = WorkspaceHelper.getProjectByPluginId(PSFPlugin.getDefault().getPluginId());
      if (workspaceProject != null)
      {
         logger.debug("Using PSF files from workspace project with plugin ID " + PSFPlugin.getDefault().getPluginId() + ".");
         final File fullPath = new File(workspaceProject.getLocation().toOSString(), pluginRelativePathToPSF);
         return fullPath.getAbsolutePath();
      } else
      {
         logger.debug("Using PSF files in installed plugin " + PSFPlugin.getDefault().getPluginId() + ".");
         final URL fullPathURL = MoflonUtilitiesActivator.getPathRelToPlugIn(pluginRelativePathToPSF, PSFPlugin.getDefault().getPluginId());
         logger.debug("Retrieved URL: " + fullPathURL);
         final String absolutePathToPSF = new File(fullPathURL.getPath()).getAbsolutePath();
         return absolutePathToPSF;
      }
   }
   
   private static final boolean switchAutoBuilding(final boolean newAutoBuildValue) throws CoreException {
	   final IWorkspace workspace = ResourcesPlugin.getWorkspace();
	   final IWorkspaceDescription description = workspace.getDescription();
	   final boolean oldAutoBuildValue = description.isAutoBuilding();
	   if (oldAutoBuildValue ^ newAutoBuildValue) {
		   description.setAutoBuilding(newAutoBuildValue);
		   workspace.setDescription(description);
	   }
	   return oldAutoBuildValue;
   }

   public void installPsfFiles(final List<File> psfFiles, final String label) {
	   try {
		   final String joinedPaths = joinBasenames(psfFiles);

		   // We extract the contents beforehand because the following action may delete them if we load PSF files
		   // directly from the workspace
		   final String psfContent = PsfFileUtils.joinPsfFile(psfFiles);

		   final int numberOfProjects = StringUtils.countMatches(psfContent, "<project ");
		   final int numberOfWorkingSets = StringUtils.countMatches(psfContent, "<workingSets ");
		   logger.info(String.format("Checking out %d projects and %d working sets in %s.", numberOfProjects, numberOfWorkingSets, joinedPaths));

		   final ImportProjectSetOperation importProjectSetOperation =
				   new ImportProjectSetOperation(null, psfContent,
						   psfFiles.size() > 1 ? null : psfFiles.get(0).getAbsolutePath(),
								   new IWorkingSet[0]);
		   final Shell shell = Display.getCurrent().getActiveShell();
		   new ProgressMonitorDialog(shell).run(true, true, importProjectSetOperation);
	   } catch (final InterruptedException e) {
		   // Operation cancelled by the user on the GUI
		   return;
	   } catch (final InvocationTargetException e) {
		   e.printStackTrace();
		   return;
	   } catch (final IOException e) {
		   e.printStackTrace();
		   return;
	   } catch (final CoreException e) {
		   e.printStackTrace();
		   return;
	   }
	   
	   ResourcesPlugin.getWorkspace().checkpoint(false);
	   
	   final List<Job> jobs = new ArrayList<Job>();
	   final Job mainInstallerJob = new Job("Installing " + label + "...") {
		   @Override
		   protected IStatus run(final IProgressMonitor monitor) {
			   try {
				   logger.info("Installing " + label + "...");
				   final boolean isAutoBuilding = switchAutoBuilding(false);
				   try {
					   final SubMonitor subMonitor = SubMonitor.convert(monitor,
							   "Installing " + label, jobs.size());
					   for (int i = 0; i < jobs.size(); i++) {
						   final Job job = jobs.get(i);
						   job.schedule();
						   while (!job.join(0, monitor)) {
							   // Do nothing: wait for job to terminate
						   }
						   subMonitor.worked(1);
					   }
				   } catch (OperationCanceledException e) {
					   Job.getJobManager().cancel(this);
					   throw e;
				   } catch (InterruptedException e) {
					   Job.getJobManager().cancel(this);
					   throw new OperationCanceledException();
				   } finally {
					   monitor.done();
					   if (isAutoBuilding ^ ResourcesPlugin.getWorkspace().isAutoBuilding()) {
						   // Do nothing: User explicitly switched auto-building flag during the build
					   } else {
						   switchAutoBuilding(isAutoBuilding);
					   }
					   
				   }
				   return Status.OK_STATUS;
//			   } catch (final InterruptedException e) {
//				   return new Status(IStatus.ERROR, AutoTestActivator.getModuleID(), "Failed to install workspace", e);
			   } catch (final CoreException e) {
				   final String message = "Sorry, I was unable to check out the projects in the PSF file.\n"//
						   + "  If you did not explicitly cancel then please check the following (most probable first):\n"//
						   + "      (1) SVN: Ensure you have switched to SVNKit (Window/Preferences/Team/SVN) or make sure JavaHL is working.\n"//
						   + "      (2) Git: Ensure that the Git repositories appearing in the error message below are clean or do not exist (Window/Perspective/Open Perspective/Other.../Git)\n" //
						   + "      (3) If possible, start with an empty, fresh workspace. Although the PSF import offers to delete the projects this may fail, especially on Windows.\n"//
						   + "      (4) Are you sure you have acess to all the projects (if they do not support anonymous access)?\n"//
						   + "      (5) The PSF file might be outdated - please check for an update of the test plugin\n"//
						   + "      (6) If it's quite late in the night, our server might be down performing a back-up - try again in a few hours.\n"//
						   + "      (7) If none of these helped, write us a mail to contact@emoflon.org :)\n" //
						   + "\n" //
						   + "Exception of type " + e.getClass().getName() + ", Message: " + MoflonUtil.displayExceptionAsString(e);
				   logger.error(message);
				   return new Status(IStatus.ERROR, AutoTestActivator.getModuleID(),
						   "Installing workspace failed. Please consult the eMoflon console for further information.", e);
			   }
		   }
	   };

	   if (exportingEapFilesRequired(label)) {
		   final IProject[] metamodelProjects = CoreActivator.getMetamodelProjects(
				   ResourcesPlugin.getWorkspace().getRoot().getProjects());
		   if (metamodelProjects.length > 0) {
			   final EnterpriseArchitectModelExporterTask eaModelExporter =
					   new EnterpriseArchitectModelExporterTask(metamodelProjects, false);
			   jobs.add(new WorkspaceJob(eaModelExporter.getTaskName()) {

				   @Override
				   public IStatus runInWorkspace(final IProgressMonitor monitor) throws CoreException {
					   eaModelExporter.run(monitor);
					   return Status.OK_STATUS;
				   }
				   
				   public boolean belongsTo(Object family) {
					   return family == mainInstallerJob;
				   }
				   
				   protected void canceling() {
					   mainInstallerJob.cancel();
				   }
			   });
		   }
		   final IBuildConfiguration[] buildConfigurations = 
				   CoreActivator.getDefaultBuildConfigurations(metamodelProjects);
		   if (buildConfigurations.length > 0) {
			   final IncrementalProjectBuilderTask metamodelBuilder =
					   new IncrementalProjectBuilderTask(buildConfigurations);
			   jobs.add(new WorkspaceJob(metamodelBuilder.getTaskName()) {

				   @Override
				   public IStatus runInWorkspace(final IProgressMonitor monitor) throws CoreException {
					   metamodelBuilder.run(monitor);
					   return Status.OK_STATUS;
				   }
				   
				   public boolean belongsTo(Object family) {
					   return family == mainInstallerJob;
				   }
				   
				   protected void canceling() {
					   mainInstallerJob.cancel();
				   }
			   });
		   }
	   }

	   final IProject[] moflonProjects = CoreActivator.getRepositoryAndIntegrationProjects(
			   ResourcesPlugin.getWorkspace().getRoot().getProjects());
	   final IBuildConfiguration[] buildConfigurations = 
			   CoreActivator.getDefaultBuildConfigurations(moflonProjects);
	   if (buildConfigurations.length > 0) {
		   final IncrementalProjectBuilderTask visualMetamodelBuilder =
				   new IncrementalProjectBuilderTask(buildConfigurations);
		   jobs.add(new WorkspaceJob(visualMetamodelBuilder.getTaskName()) {

			   @Override
			   public IStatus runInWorkspace(final IProgressMonitor monitor) throws CoreException {
				   visualMetamodelBuilder.run(monitor);
				   return Status.OK_STATUS;
			   }
			   
			   public boolean belongsTo(Object family) {
				   return family == mainInstallerJob;
			   }
			   
			   protected void canceling() {
				   mainInstallerJob.cancel();
			   }
		   });
	   }

	   try {
		   final ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		   final ILaunchConfigurationType type =
				   manager.getLaunchConfigurationType("org.eclipse.emf.mwe2.launch.Mwe2LaunchConfigurationType");
		   final ILaunchConfiguration[] configurations =
				   manager.getLaunchConfigurations(type);
		   for (int i = 0; i < configurations.length; i++) {
			   final ILaunchConfiguration config = configurations[i];
			   jobs.add(new LaunchInvocationJob(config) {
				   
				   public boolean belongsTo(Object family) {
					   return family == mainInstallerJob;
				   }
				   
				   protected void canceling() {
					   mainInstallerJob.cancel();
				   }
			   });
		   }
		   if (configurations.length > 0 && buildConfigurations.length > 0) {
			   final IncrementalProjectBuilderTask textualMetamodelBuilder =
					   new IncrementalProjectBuilderTask(buildConfigurations);
			   jobs.add(new WorkspaceJob(textualMetamodelBuilder.getTaskName()) {

				   @Override
				   public IStatus runInWorkspace(final IProgressMonitor monitor) throws CoreException {
					   textualMetamodelBuilder.run(monitor);
					   return Status.OK_STATUS;
				   }
				   
				   public boolean belongsTo(Object family) {
					   return family == mainInstallerJob;
				   }
				   
				   protected void canceling() {
					   mainInstallerJob.cancel();
				   }
			   });
		   }
	   } catch (final CoreException e) {
		   // Do nothing
	   }

	   if (isRunningJUnitTestsRequired(label)) {
		   final Collection<IProject> testProjects = collectTestProjects();
		   try {
			   for (final IProject testProject : testProjects) {
				   final List<IFile> selectedLaunchConfigurations = Arrays.asList(testProject.members()).stream()//
						   .filter(m -> m instanceof IFile) //
						   .map(m -> (IFile) m.getAdapter(IFile.class))//
						   .filter(f -> f.getName().matches(JUNIT_TEST_LAUNCHER_FILE_NAME_PATTERN))//
						   .collect(Collectors.toList());
				   final ILaunchManager mgr = DebugPlugin.getDefault().getLaunchManager();
				   for (final IFile file : selectedLaunchConfigurations) {
					   jobs.add(new LaunchInvocationJob(mgr.getLaunchConfiguration(file)) {
						   public boolean belongsTo(Object family) {
							   return family == mainInstallerJob;
						   }
						   
						   protected void canceling() {
							   mainInstallerJob.cancel();
						   }
					   });
				   }
			   }
		   } catch (final CoreException e) {
			   // Do nothing: Ignore test projects in case of errors
		   }
	   }
	   mainInstallerJob.setUser(true);
	   mainInstallerJob.schedule();
   }

   /**
    * Returns true if the JUnit tests in the workspace shall be executed, based on the provided displayed name
    */
   private boolean isRunningJUnitTestsRequired(String displayName)
   {
      // When installing the 'handbook' workspace, only latex sources are downloaded.
      if (EMoflonStandardWorkspaces.MODULE_DOCUMENTATION.equals(displayName))
         return false;

      return true;
   }

   public Collection<IProject> collectTestProjects()
   {
      return Arrays.asList(ResourcesPlugin.getWorkspace().getRoot().getProjects()).stream().filter(project -> isTestProjectAccordingToConvention(project))
            .collect(Collectors.toList());
   }

   /**
    * Returns true if EAP files shall be exported, based on the provided displayed name
    */
   private boolean exportingEapFilesRequired(String displayName)
   {
      // When installing the 'handbook' workspace, only latex sources are downloaded.
      if (EMoflonStandardWorkspaces.MODULE_DOCUMENTATION.equals(displayName))
         return false;

      return true;
   }

   // This is required to avoid NPEs when checking out plugin projects (a problem with JDT)
   private static void prepareWorkspace()
   {
      try
      {
         JavaModelManager.getExternalManager().createExternalFoldersProject(new NullProgressMonitor());
      } catch (final CoreException e)
      {
         LogUtils.error(logger, e);
      }

   }

   private static boolean isTestProjectAccordingToConvention(final IProject project)
   {
      try
      {
         return project.getName().toUpperCase().contains("TESTSUITE") && project.getNature(JavaCore.NATURE_ID) != null;
      } catch (CoreException e)
      {
         return false;
      }
   }

   /**
    * This method removes all working sets containing no elements whose name starts with "org.moflon"
    */
   public static void removeEmptyMoflonWorkingSets()
   {
      IWorkingSetManager workingSetManager = PlatformUI.getWorkbench().getWorkingSetManager();
      for (IWorkingSet ws : workingSetManager.getAllWorkingSets())
      {
         if (ws.getName().startsWith("org.moflon") && ws.getElements().length == 0)
         {
            workingSetManager.removeWorkingSet(ws);
         }
      }
   }

   private static List<File> mapToAbsoluteFiles(final Collection<String> pluginRelativePsfFiles)
   {
      return pluginRelativePsfFiles.stream().filter(s -> s != null).map(WorkspaceInstaller::mapToAbsolutePath).map(s -> new File(s))
            .collect(Collectors.toList());
   }

   private static String joinBasenames(final List<File> files)
   {
      return files.stream().map(f -> f.getName()).collect(Collectors.joining(", "));
   }
}
