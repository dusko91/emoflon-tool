package org.moflon.ide.ui.admin.handlers;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.moflon.core.utilities.WorkspaceHelper;
import org.moflon.ide.core.preferences.EMoflonPreferencesStorage;
import org.moflon.ide.core.util.BuilderHelper;
import org.moflon.ide.ui.UIActivator;
import org.moflon.ide.ui.preferences.EMoflonPreferenceInitializer;

public class EMoflonBuildJob extends WorkspaceJob
{
   private final Logger logger;

   private final List<IProject> projects;

   public EMoflonBuildJob(final String name, final List<IProject> projects)
   {
      super(name);
      this.logger = Logger.getLogger(this.getClass());
      this.projects = projects;
   }

   @Override
   public IStatus runInWorkspace(final IProgressMonitor monitor)
   {
      final MultiStatus resultStatus = new MultiStatus(UIActivator.getModuleID(), 0, "eMoflon Build Job failed", null);
      final List<IProject> projectsToBeBuilt = this.projects.stream().filter(project -> shallBuildProject(project)).collect(Collectors.toList());

      final SubMonitor subMon = SubMonitor.convert(monitor, "eMoflon Build Job", 2 * projectsToBeBuilt.size());

      updateUserSelectedTimeoutForValidation();

      for (final IProject project : projectsToBeBuilt)
      {

         final IStatus projectBuildStatus = cleanAndBuild(project, subMon.newChild(2));
         if (!projectBuildStatus.isOK())
         {
            resultStatus.add(projectBuildStatus);
         }
      }

      final IStatus codeGenerationStatus = BuilderHelper.generateCodeInOrder(subMon.newChild(projectsToBeBuilt.size()), projectsToBeBuilt);
      if (!codeGenerationStatus.isOK())
      {
         resultStatus.add(codeGenerationStatus);
      }

      return resultStatus.matches(Status.ERROR) ? resultStatus : Status.OK_STATUS;

   }

   private void updateUserSelectedTimeoutForValidation()
   {
      EMoflonPreferencesStorage.getInstance().setValidationTimeout(EMoflonPreferenceInitializer.getValidationTimeoutMillis());
   }

   private IStatus cleanAndBuild(final IProject project, final IProgressMonitor monitor)
   {
      IStatus status = Status.OK_STATUS;
      try
      {
         final String projectName = project.getName();
         final SubMonitor subMon = SubMonitor.convert(monitor, "Clean and build of " + projectName, 2);

         if (project != null && WorkspaceHelper.isMoflonOrMetamodelProject(project))
         {

            logger.info("Cleaning project " + projectName + " - triggered manually!");

            project.build(IncrementalProjectBuilder.CLEAN_BUILD, subMon.newChild(1));
            project.build(IncrementalProjectBuilder.FULL_BUILD, subMon.newChild(1));

            logger.debug("Cleaning project " + projectName + " done.");

         }
      } catch (final OperationCanceledException e)
      {
         status = new Status(IStatus.CANCEL, UIActivator.getModuleID(), "", null);
      } catch (final CoreException e)
      {
         status = new Status(IStatus.ERROR, UIActivator.getModuleID(), "Problem during clean and build: " + e.getMessage(), e);
      }
      return status;
   }

   /**
    * Returns true if the given project should be rebuilt
    */
   protected boolean shallBuildProject(final IProject project)
   {
      return true;
   }
}