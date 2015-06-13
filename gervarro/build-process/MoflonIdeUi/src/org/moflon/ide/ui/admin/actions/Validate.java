package org.moflon.ide.ui.admin.actions;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.moflon.codegen.eclipse.CodeGeneratorPlugin;
import org.moflon.codegen.eclipse.MethodBodyHandlerDescriptor;
import org.moflon.eclipse.job.IMonitoredJob;
import org.moflon.eclipse.job.ProgressMonitoringJob;

public class Validate extends AbstractHandler {
	
	public Object execute(ExecutionEvent event) throws ExecutionException {
		return execute(HandlerUtil.getCurrentSelectionChecked(event));
	}

	public Object execute(ISelection selection) throws ExecutionException {
		if (selection instanceof TreeSelection) {
			TreeSelection treeSelection = (TreeSelection) selection;
			for (Iterator<?> selectionIterator = treeSelection.iterator(); selectionIterator.hasNext(); ) {
				Object element = selectionIterator.next();
				if (element instanceof IFile) {
					IFile ecoreFile = (IFile) element;
					MethodBodyHandlerDescriptor descriptor =
							new MethodBodyHandlerDescriptor("org.moflon.compiler.sdm.democles", "org.moflon.compiler.sdm.democles.eclipse.MonitoredSDMValidator");
					try {
						descriptor.lookupClass();
						final IMonitoredJob internalJob =
								(IMonitoredJob) ecoreFile.getAdapter(IMonitoredJob.class);
						if (internalJob != null) {
							final ProgressMonitoringJob job =
									new ProgressMonitoringJob(CodeGeneratorPlugin.getModuleID(), internalJob);
							final boolean runInBackground = PlatformUI.getPreferenceStore().getBoolean("RUN_IN_BACKGROUND");
							if (!runInBackground) {
								// Run in foreground
								PlatformUI.getWorkbench().getProgressService().showInDialog(null, job);
							}
							job.schedule();
						}
					} catch (ClassNotFoundException e) {
						// Do nothing
					}
				}
			}
		}
		return null;
	}
}
