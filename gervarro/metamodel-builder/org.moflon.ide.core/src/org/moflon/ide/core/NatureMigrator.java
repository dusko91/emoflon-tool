package org.moflon.ide.core;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.JavaCore;
import org.gervarro.eclipse.workspace.autosetup.JavaProjectConfigurator;
import org.gervarro.eclipse.workspace.autosetup.PluginProjectConfigurator;
import org.gervarro.eclipse.workspace.autosetup.ProjectConfigurator;
import org.gervarro.eclipse.workspace.util.ProjectStateObserver;
import org.gervarro.eclipse.workspace.util.ProjectUtil;
import org.gervarro.eclipse.workspace.util.WorkspaceTask;
import org.moflon.ide.core.runtime.ProjectNatureAndBuilderConfiguratorTask;
import org.moflon.ide.core.runtime.natures.AntlrNature;

public class NatureMigrator extends ProjectStateObserver implements ProjectConfigurator {
	
	protected void handleResourceChange(final IResource resource, final boolean added) {
		if (added && resource.getType() == IResource.PROJECT) {
			try {
				final ProjectNatureAndBuilderConfiguratorTask task =
						new ProjectNatureAndBuilderConfiguratorTask((IProject) resource, false);
				task.updateNatureIDs(this, added);
				task.updateBuildSpecs(this, added);
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
		final String[] natureIDs = description.getNatureIds();
		for (int i = 0; i < natureIDs.length; i++) {
			if (JavaCore.NATURE_ID.equals(natureIDs[i])) {
				buildSpecs = new JavaProjectConfigurator().updateBuildSpecs(description, buildSpecs, added);
			}
			if ("org.eclipse.pde.PluginNature".equals(natureIDs[i])) {
				buildSpecs = new PluginProjectConfigurator().updateBuildSpecs(description, buildSpecs, added);
			}
			if (ProjectUtil.indexOf(buildSpecs, CoreActivator.ANTLR_BUILDER_ID) >= 0) {
				buildSpecs = new AntlrNature().updateBuildSpecs(description, buildSpecs, added);
			}
		}
		return buildSpecs;
	}
}
