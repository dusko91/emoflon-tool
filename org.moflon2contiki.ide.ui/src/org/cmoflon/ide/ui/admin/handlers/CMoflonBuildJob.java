package org.cmoflon.ide.ui.admin.handlers;

import java.util.List;

import org.cmoflon.ide.ui.CMoflonUIActivator;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.moflon.core.utilities.WorkspaceHelper;

public class CMoflonBuildJob extends Job {

//	private Object logger;
	private List<IProject> projects;
	 private final IStatus OK_STATUS = new Status(IStatus.OK, CMoflonUIActivator.getModuleID(), IStatus.OK, "", null);

	public CMoflonBuildJob(final String name, final List<IProject> projects)
	   {
	      super(name);
//	      this.logger = Logger.getLogger(this.getClass());
	      this.projects = projects;
	   }
	
	@Override
	protected IStatus run(IProgressMonitor monitor) {
		IStatus status = OK_STATUS;
		for(IProject p: projects){
			try {
				p.build(IncrementalProjectBuilder.CLEAN_BUILD, WorkspaceHelper.createSubmonitorWith1Tick(monitor));
				p.build(IncrementalProjectBuilder.FULL_BUILD, WorkspaceHelper.createSubmonitorWith1Tick(monitor));
			} catch (CoreException e) {
				status = new Status(IStatus.ERROR, CMoflonUIActivator.getModuleID(), IStatus.OK, "", e);
			}
			finally{
				monitor.done();
			}
		}
		return status;
	}

}
