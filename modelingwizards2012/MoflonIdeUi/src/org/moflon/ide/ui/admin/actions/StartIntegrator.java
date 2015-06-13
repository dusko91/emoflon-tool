package org.moflon.ide.ui.admin.actions;

import java.io.FileNotFoundException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.moflon.ide.ui.admin.wizards.tie.StartIntegratorGenerator;

public class StartIntegrator implements IWorkbenchWindowActionDelegate {
	private IFile correspondenceModel;
	private IProject project;

	@Override
	public void run(IAction action) {
		
		generateStartIntegratorClass();
		
		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType type = manager
				.getLaunchConfigurationType(IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION);
		ILaunchConfiguration[] configurations;
		try {
			configurations = manager.getLaunchConfigurations(type);

			// This can be removed and adjusted to allow for e.g. the last five runs
			for (int i = 0; i < configurations.length; i++) {
				ILaunchConfiguration configuration = configurations[i];
				if (configuration.getName().equals("Start Integrator")) {
					configuration.delete();
					break;
				}
			}

			ILaunchConfigurationWorkingCopy workingCopy = type.newInstance(
					null, "Start Integrator");
			;

			// The projects classpath should have all necessary entries
			workingCopy.setAttribute(
					IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
					project.getProject().getName());
			workingCopy.setAttribute(
					IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
					"org.moflon.tie.StartIntegrator");

			// Create arguments for call to main
			workingCopy.setAttribute(
					IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS,
					"\"" + correspondenceModel.getLocation() + "\"");

			ILaunchConfiguration configuration = workingCopy.doSave();
			DebugUITools.launch(configuration, ILaunchManager.RUN_MODE);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		try {
			// Check that an IFile is selected
			if (selection != null && selection instanceof StructuredSelection) {
				StructuredSelection fileSelection = (StructuredSelection) selection;

				// Assign files
				Object chosen = fileSelection.getFirstElement();

				if (chosen != null && chosen instanceof IFile) {
					correspondenceModel = (IFile) fileSelection
							.getFirstElement();
					if (correspondenceModel != null
							&& correspondenceModel.getFileExtension().equals(
									"xmi")) {
						// Assign project
						project = correspondenceModel.getProject();
					} else {
						action.setEnabled(false);
						return;
					}
				}
			} else {
				action.setEnabled(false);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void dispose() {

	}

	@Override
	public void init(IWorkbenchWindow window) {

	}

	private void generateStartIntegratorClass() {
		StartIntegratorGenerator generator = new StartIntegratorGenerator(project);
		try {
			generator.doFinish();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}



}
