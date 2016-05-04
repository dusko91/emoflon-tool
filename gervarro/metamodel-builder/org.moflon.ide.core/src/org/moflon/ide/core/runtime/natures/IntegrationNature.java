package org.moflon.ide.core.runtime.natures;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.gervarro.eclipse.workspace.util.WorkspaceTask;
import org.moflon.ide.core.runtime.MoflonProjectConfigurator;
import org.moflon.ide.core.runtime.ProjectNatureAndBuilderConfiguratorTask;

public class IntegrationNature implements IProjectNature {
   private IProject project; 
   
	@Override
	public void configure() throws CoreException {
		final MoflonProjectConfigurator configurator =
				new MoflonProjectConfigurator(true);
		final ProjectNatureAndBuilderConfiguratorTask task =
				new ProjectNatureAndBuilderConfiguratorTask(project, false);
		task.updateBuildSpecs(configurator, true);
		WorkspaceTask.execute(task, false);
	}

	@Override
	public void deconfigure() throws CoreException {
		final MoflonProjectConfigurator configurator =
				new MoflonProjectConfigurator(true);
		final ProjectNatureAndBuilderConfiguratorTask task =
				new ProjectNatureAndBuilderConfiguratorTask(project, false);
		task.updateBuildSpecs(configurator, false);
		WorkspaceTask.execute(task, false);
	}

   @Override
   public IProject getProject() {
      return project;
   }

   @Override
   public void setProject(final IProject project) {
      this.project = project;
   }

}
