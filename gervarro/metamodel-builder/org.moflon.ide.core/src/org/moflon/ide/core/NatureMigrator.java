package org.moflon.ide.core;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.gervarro.eclipse.workspace.autosetup.ProjectConfigurator;
import org.gervarro.eclipse.workspace.autosetup.ProjectNatureAndBuilderConfiguratorTask;
import org.gervarro.eclipse.workspace.util.ProjectStateObserver;
import org.gervarro.eclipse.workspace.util.WorkspaceTask;

public class NatureMigrator extends ProjectStateObserver implements ProjectConfigurator {
	
	protected void handleResourceChange(final IResource resource, final boolean added) {
		if (added && resource.getType() == IResource.PROJECT) {
			try {
				final ProjectNatureAndBuilderConfiguratorTask task =
						new ProjectNatureAndBuilderConfiguratorTask((IProject) resource, false);
				task.updateNatureIDs(this, added);
				WorkspaceTask.execute(task, false);
			} catch (CoreException e) {
				// Do nothing
			}
		}
	}

	@Override
	public String[] updateNatureIDs(String[] natureIDs, boolean added) throws CoreException {
		for (int i = 0; i < natureIDs.length; i++) {
			if (natureIDs[i].startsWith("org.moflon.ide.ui")) {
				natureIDs[i] = natureIDs[i].replaceFirst("^org[.]moflon[.]ide[.]ui", "org.moflon.ide.core");
			}
		}
		return natureIDs;
	}

	@Override
	public ICommand[] updateBuildSpecs(IProjectDescription description, ICommand[] buildSpecs, boolean added)
			throws CoreException {
		return buildSpecs;
	}
}
