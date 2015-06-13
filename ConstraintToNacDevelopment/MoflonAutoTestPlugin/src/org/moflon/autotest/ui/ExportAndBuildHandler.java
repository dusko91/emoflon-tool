package org.moflon.autotest.ui;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;
import org.eclipse.ui.statushandlers.StatusManager;
import org.moflon.autotest.AutoTestActivator;
import org.moflon.autotest.core.EnterpriseArchitectHelper;
import org.moflon.ide.core.CoreActivator;


public class ExportAndBuildHandler extends org.eclipse.core.commands.AbstractHandler{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IStructuredSelection selection = (IStructuredSelection)workbench.getActiveWorkbenchWindow().getSelectionService().getSelection();
		
		final Set<IProject> projects = getMetamodelProjectsFromSelection(selection);
		
		IWorkbench wb = PlatformUI.getWorkbench();
		IProgressService ps = wb.getProgressService();
		try {
			ps.busyCursorWhile(new IRunnableWithProgress() {
				public void run(IProgressMonitor pm) {
					pm.beginTask("Exporting metamodels to Eclipse...", 2*projects.size());
					for (IProject project : projects) {
						pm.worked(1);
						try {
							EnterpriseArchitectHelper.delegateToEnterpriseArchitect(project);
						} catch (IOException | InterruptedException e) {
							IStatus status = new Status(IStatus.ERROR, AutoTestActivator.PLUGIN_ID, e.getMessage());
							StatusManager.getManager().handle(status);
						}
						pm.worked(1);
					}				
				}
			});
			
			for (IProject project : projects) {
				Job refreshProject = new RefreshProjectJob(project);
				refreshProject.schedule();
			}
		} catch (InterruptedException | InvocationTargetException e) {
			throw new ExecutionException("Could not export metamodel to Eclipe", e);
		}
		
		return null;
	}
	
	private Set<IProject> getMetamodelProjectsFromSelection(IStructuredSelection selection) {
		Set<IProject> result = null;
		Iterator iterator = selection.iterator();
		if (iterator != null) {
			while (iterator.hasNext()) {
				Object next = iterator.next();
				if (next instanceof IResource) {
					IResource res = (IResource) next;
					IProject project = res.getProject();
					if (project != null) {
						if (result == null) {
							result = new HashSet<IProject>();							
						}
						try {
							String[] natureIds = project.getDescription().getNatureIds();
							if (Arrays.asList(natureIds).contains(CoreActivator.METAMODEL_NATURE_ID)) { 
								result.add(project);
							}
						} catch (CoreException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		
		return result;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	class RefreshProjectJob extends Job {

		private final static String NAME = "Refreshing project (\"%1$s\") job";
		
		private IProject project; 
		
		private RefreshProjectJob(String name) {
			super(name);
		}
		
		public RefreshProjectJob(IProject project) {
			this(String.format(NAME, project.getName()));
			this.project = project;
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			try {
				project.refreshLocal(IResource.DEPTH_INFINITE, new SubProgressMonitor(monitor, 1));
			} catch (CoreException e) {
				return e.getStatus();
			}
			return new Status(IStatus.OK, AutoTestActivator.PLUGIN_ID, null);
		}
	}
	
}
