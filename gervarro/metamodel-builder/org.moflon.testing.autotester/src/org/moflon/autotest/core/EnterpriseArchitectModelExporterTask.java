package org.moflon.autotest.core;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.MultiRule;
import org.gervarro.eclipse.workspace.util.WorkspaceTask;
import org.moflon.core.utilities.WorkspaceHelper;
import org.moflon.ide.core.CoreActivator;
import org.moflon.ide.core.ea.EnterpriseArchitectHelper;

public final class EnterpriseArchitectModelExporterTask extends WorkspaceTask {
	private final IProject[] metamodelProjects;
	
	public EnterpriseArchitectModelExporterTask() {
		this(ResourcesPlugin.getWorkspace().getRoot().getProjects());
	}
	
	public EnterpriseArchitectModelExporterTask(final IProject... metamodelProjects) {
		this(metamodelProjects, true);
	}

	EnterpriseArchitectModelExporterTask(final IProject[] metamodelProjects, final boolean checkProjects) {
		if (metamodelProjects == null) {
			throw new NullPointerException();
		}
		this.metamodelProjects = checkProjects ?
				CoreActivator.getMetamodelProjects(metamodelProjects) : metamodelProjects;
	}

	@Override
	public String getTaskName() {
		return "Exporting metamodel from Enterprise Architect";
	}

	@Override
	public ISchedulingRule getRule() {
		return MultiRule.combine(metamodelProjects);
	}
	   
	@Override
	public void run(final IProgressMonitor monitor) throws CoreException {
		final SubMonitor subMonitor = SubMonitor.convert(monitor, metamodelProjects.length);
		try {
			for (int i = 0; i < metamodelProjects.length; i++) {
				final SubMonitor loopMonitor = SubMonitor.convert(subMonitor, 11);
				try {
					loopMonitor.setTaskName("Exporting from Enterprise Architect");
					if (shouldExport(metamodelProjects[i])) {
						EnterpriseArchitectHelper.exportEcoreFilesFromEAP(metamodelProjects[i], new NullProgressMonitor());
						loopMonitor.worked(10);
					}
					loopMonitor.setWorkRemaining(1);
					metamodelProjects[i].refreshLocal(IResource.DEPTH_INFINITE,
							loopMonitor.newChild(1));
				} finally {
					loopMonitor.done();
				}
			}
		} finally {
			subMonitor.done();
		}
	}
	
	public final IProject[] getMetamodelProjects() {
		return metamodelProjects;
	}

	private final boolean shouldExport(final IProject metamodelProject) {
		final IFile eapFile = WorkspaceHelper.getEapFileFromMetamodelProject(metamodelProject);
		if (!eapFile.exists()) {
			return false;
		}
		
		final IFolder tempFolder = metamodelProject.getFolder(WorkspaceHelper.TEMP_FOLDER);
		if (tempFolder.exists()) {
			final IFile mocaTree = WorkspaceHelper.getExportedMocaTree(metamodelProject);
			if (tempFolder.exists() && mocaTree.exists()) {
				return mocaTree.getLocalTimeStamp() < eapFile.getLocalTimeStamp();
			}
		}
		return true;
	}
}
