package org.moflon.ide.ui.admin.actions;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.moflon.ide.ui.admin.wizards.moca.TextEditorGenerator;
import org.moflon.ide.ui.admin.wizards.texteditor.TextEditorConfigurationClassGenerator;

/**
 * AddTextEditorAction is used when user wants to set the properties of text editor 
 * the actual job is done in TextEditorGenerator by creating a java class
 * 
 * @author Amir Naseri
 * @author (last editor) $Author$
 * @version $Revision$ $Date$
 */

public class AddTextEditorAction implements IObjectActionDelegate {

	private IWorkbenchWindow window;

	private Logger logger = Logger.getLogger(AddTextEditorAction.class);

	private IStructuredSelection selection;

	private IProject project;

	public void init(IStructuredSelection selection)
	{
		// Check if selection is valid (i.e., inside Package Explorer or
		// Navigator
		Object obj = null;
		IResource resource = null;
		if (selection instanceof TreeSelection)
		{
			obj = ((TreeSelection) selection).getFirstElement();
		} else if (selection instanceof StructuredSelection)
		{
			obj = ((StructuredSelection) selection).getFirstElement();
		}
		// Get what the user selected in the workspace as a resource
		if (obj != null && obj instanceof IResource)
		{
			resource = (IResource) obj;
		} else
		{
			resource = null;
		}
		if (resource != null)
		{
			project = resource.getProject();
		}
	}


	@Override
	public void run(IAction action) {

		init(selection);

		if (project != null)
		{
			TextEditorConfigurationClassGenerator generator = new TextEditorConfigurationClassGenerator(project);
			try {
				generator.doFinish();
			} catch (Exception e){
				e.printStackTrace();
			}
		}

	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		if (selection != null && selection instanceof IStructuredSelection) {
			this.selection = (IStructuredSelection) selection;
		}

	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		window = targetPart.getSite().getWorkbenchWindow();  

	}



}
