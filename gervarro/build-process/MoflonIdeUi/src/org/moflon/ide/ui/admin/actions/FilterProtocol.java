package org.moflon.ide.ui.admin.actions;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.dialogs.ListDialog;
import org.moflon.util.eMoflonEMFUtil;

import TGGLanguage.algorithm.protocol.TranslationProtocol;

public class FilterProtocol extends Action implements IActionDelegate {
	private static final Logger logger = Logger.getLogger(ConvertToDot.class);

	private final static String FilterAllButEdgeTranslationSteps = "FILTER_ALL_BUT_EDGE_TRANSLATION_STEPS";
	private final static String FilterAllButErrors = "FILTER_ALL_BUT_ERRORS";
	private final static String FilterAllButMultipleIsAppropriateCandidates = "FILTER_ALL_BUT_MULTIPLE_ISAPPR_CANDIDATES";
	private final static String FilterAllButNoApplicableRule = "FILTER_ALL_BUT_NO_APPLICABLE_RULE";
	private final static String FilterAllButNoCoreMatch = "FILTER_ALL_BUT_NO_CORE_MATCH";
	private final static String FilterAllButOperationSuccessful = "FILTER_ALL_BUT_OPERATION_SUCCESSFUL";
	private final static String FilterNoCoreMatch = "FILTER_NO_CORE_MATCH";
	private final static String FilterStepsUntilFirstError = "FILTER_STEPS_UNTIL_FIRST_ERROR";

	private final static String[] filters = { FilterAllButEdgeTranslationSteps, FilterAllButErrors, FilterAllButMultipleIsAppropriateCandidates, FilterAllButNoApplicableRule,
			FilterAllButNoCoreMatch, FilterAllButOperationSuccessful, FilterNoCoreMatch, FilterStepsUntilFirstError };

	private IFile model;

	public FilterProtocol() {
	}

	@Override
	public void run(IAction action) {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				ListDialog dialog = new ListDialog(Display.getDefault().getActiveShell());
				dialog.setTitle("Filtering Protocol");
				dialog.setMessage("Choose filter");
				dialog.setContentProvider(new ArrayContentProvider());
				dialog.setLabelProvider(new FilterProvider());
				dialog.setInput(filters);
				if (dialog.open() == Window.OK) {
					if (dialog.getResult() != null && dialog.getResult().length != 0) {
						logger.debug("Applying filter: " + dialog.getResult()[0]);
						runForchosenAction((String) dialog.getResult()[0]);
						logger.debug("Filtering of " + model.getName() + " finished");
					}
				}

			}
		});
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// Check that an IFile is selected
		if (selection != null && selection instanceof StructuredSelection) {
			StructuredSelection fileSelection = (StructuredSelection) selection;

			// Assign files
			Object chosen = fileSelection.getFirstElement();

			if (chosen != null && chosen instanceof IFile) {
				model = (IFile) fileSelection.getFirstElement();
				EObject root = eMoflonEMFUtil.loadModelWithDependenciesAndCrossReferencer(URI.createPlatformResourceURI(model.getFullPath().toString(), true), null);
				if (root instanceof TranslationProtocol)
					return;
			}
		}

		action.setEnabled(false);
	}

	public void runForchosenAction(String nameOfMenuItem) {
		switch (nameOfMenuItem) {
		case FilterAllButEdgeTranslationSteps:
			save(getProtocol().filterAllButEdgeTranslationSteps());
			break;

		case FilterAllButErrors:
			save(getProtocol().filterAllButErrors());
			break;

		case FilterAllButMultipleIsAppropriateCandidates:
			save(getProtocol().filterAllButMultipleIsAppropriateCandidate());
			break;

		case FilterAllButNoApplicableRule:
			save(getProtocol().filterAllButNoApplicableRule());
			break;

		case FilterAllButNoCoreMatch:
			save(getProtocol().filterAllButNoCoreMatch());
			break;

		case FilterNoCoreMatch:
			save(getProtocol().filterNoCoreMatch());
			break;

		case FilterStepsUntilFirstError:
			save(getProtocol().filterStepsUntilFirstError());
			break;

		case FilterAllButOperationSuccessful:
			save(getProtocol().filterAllButPerformOperationSuccessful());
			break;

		default:
			break;
		}
	}

	protected TranslationProtocol getProtocol() {
		EObject root = eMoflonEMFUtil.loadModelWithDependenciesAndCrossReferencer(URI.createPlatformResourceURI(model.getFullPath().toString(), true), null);
		return (TranslationProtocol) root;
	}

	protected void save(TranslationProtocol protocol) {
		String path = model.getRawLocation().toOSString();
		path = path.substring(0, path.lastIndexOf(".")) + ".filtered.xmi";
		eMoflonEMFUtil.saveModel(protocol, path);
	}

	class FilterProvider extends LabelProvider implements ILabelProvider {
		@Override
		public String getText(Object element) {
			return (String) element;
		}
	}
}
