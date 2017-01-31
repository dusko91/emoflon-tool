package org.moflon.autotest.core;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.gervarro.eclipse.task.ITask;
import org.moflon.autotest.AutoTestActivator;
import org.moflon.core.utilities.LogUtils;

public class LaunchInvocationTask implements ITask {
	private static final Logger logger = Logger.getLogger(LaunchInvocationTask.class);
	private final ILaunchConfiguration launchConfiguration;
	
	public LaunchInvocationTask(final ILaunchConfiguration launchConfiguration) {
		this.launchConfiguration = launchConfiguration;
	}
	
	public IStatus run(final IProgressMonitor monitor) {
		try {
			final ILaunch launch = launchConfiguration.launch(
					ILaunchManager.RUN_MODE, monitor, true);
			while (!launch.isTerminated()) {
				try {
					Thread.sleep(10000);
					if (monitor.isCanceled()) {
						terminateProcess(launch);
					}
				} catch (final InterruptedException e) {
					terminateProcess(launch);
				}
			}
			return new Status(IStatus.OK, AutoTestActivator.getModuleID(),
					launchConfiguration.getName() + " successfully terminated");
		} catch (final CoreException e) {
			return new Status(IStatus.ERROR, AutoTestActivator.getModuleID(),
					IStatus.ERROR, "Unable to launch " + launchConfiguration.getName(), e);
		}
	}

	private final void terminateProcess(final ILaunch launch) {
		if (launch.canTerminate()) {
			try {
				launch.terminate();
			} catch (final DebugException e) {
				LogUtils.warn(logger, "Unable to terminate %s", launchConfiguration.getName());
			}
		} else {
			LogUtils.warn(logger, "Unable to terminate %s", launchConfiguration.getName());
		}
		throw new OperationCanceledException();
	}

	@Override
	public final String getTaskName() {
		return "Launching configuration " + launchConfiguration.getName();
	}
}
