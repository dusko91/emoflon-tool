package org.moflon.ide.ui.admin.actions;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public abstract class AbstractCleanAndBuildClasses implements
		IWorkbenchWindowActionDelegate {

	IResource selectedResource;

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		Object obj = null;

		// Check if selection is valid (i.e., inside Package Explorer or
		// Navigator
		if (selection instanceof TreeSelection) {
			obj = ((TreeSelection) selection).getFirstElement();
		} else if (selection instanceof StructuredSelection) {
			obj = ((StructuredSelection) selection).getFirstElement();
		}

		// Get what the user selected in the workspace as a resource
		if (obj != null && obj instanceof IResource) {
			selectedResource = (IResource) obj;
		} else {
			selectedResource = null;
		}
	}

	@Override
	public void dispose() {
	}

	@Override
	public void init(IWorkbenchWindow window) {
	}

}
