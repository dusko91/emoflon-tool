package org.moflon.ide.ui.admin.handlers;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.moflon.core.utilities.WorkspaceHelper;
import org.moflon.ide.core.CoreActivator;

final class EMoflonBuildJobForDirtyProjects extends EMoflonBuildJob
{

   EMoflonBuildJobForDirtyProjects(final String name, final List<IProject> projects)
   {
      super(name, projects);
   }

   @Override
   protected boolean shallBuildProject(final IProject project)
   {
	   // TODO@rkluge org.moflon.autotest.core.WorkspaceInstaller.prepareIncrementalProjectBuilderJob(List<Job>, IProject[])
      return CoreActivator.getDefault().isDirty(project) && project.isAccessible() && !WorkspaceHelper.isMetamodelProjectNoThrow(project);
   }

}