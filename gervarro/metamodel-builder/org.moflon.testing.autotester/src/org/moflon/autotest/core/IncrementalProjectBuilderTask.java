package org.moflon.autotest.core;

import org.eclipse.core.resources.IBuildConfiguration;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.gervarro.eclipse.workspace.util.WorkspaceTask;

public final class IncrementalProjectBuilderTask extends WorkspaceTask {
	private final IBuildConfiguration[] buildConfigurations;
	
	public IncrementalProjectBuilderTask() {
		this.buildConfigurations = null;
	}
	
	public IncrementalProjectBuilderTask(final IBuildConfiguration... buildConfigurations) {
		if (buildConfigurations == null) {
			throw new NullPointerException();
		}
		this.buildConfigurations = buildConfigurations;
	}

	@Override
	public String getTaskName() {
		return "Performing incremental build";
	}

	@Override
	public ISchedulingRule getRule() {
		return ResourcesPlugin.getWorkspace().getRuleFactory().buildRule();
	}
	   
	@Override
	public void run(final IProgressMonitor monitor) throws CoreException {
		if (buildConfigurations != null) {
			ResourcesPlugin.getWorkspace().build(buildConfigurations,
					IncrementalProjectBuilder.INCREMENTAL_BUILD, true, monitor);
		} else {
			ResourcesPlugin.getWorkspace().build(
					IncrementalProjectBuilder.INCREMENTAL_BUILD, monitor);
		}
	}
}
