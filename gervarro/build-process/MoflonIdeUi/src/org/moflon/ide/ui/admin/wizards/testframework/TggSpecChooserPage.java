package org.moflon.ide.ui.admin.wizards.testframework;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.model.WorkbenchViewerComparator;
import org.moflon.ide.ui.UIActivator;
import org.moflon.util.WorkspaceHelper;


public class TggSpecChooserPage extends WizardPage {
	
	private static final String PAGE_TITLE = "TGG Project containing specification";
	private static final String PAGE_DESCRIPTION = "Select the Project which contains the specification TGG";
	private IProject tggSpecProject = null;
	private Button button;
	private Text selectedTggProjectEditBox;

   public TggSpecChooserPage(String pageName) {
		super(pageName);
		setTitle(PAGE_TITLE);
		setDescription(PAGE_DESCRIPTION);
	}

	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		final Composite pgComp = new Composite(parent, SWT.NONE);		
		pgComp.setLayout(new GridLayout(2, false));
		pgComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		createEcoreLocationGroup(pgComp);
        
		setPageComplete(false);		
		setControl(pgComp);
		Dialog.applyDialogFont(pgComp);
		
		// add a listener that emulates a button click on the "Browse" button
      IWizard theWizard = this.getWizard();
      if (theWizard != null) {
         IWizardContainer container = theWizard.getContainer();
         if (container != null && container instanceof WizardDialog) {
            WizardDialog dialog = (WizardDialog) container;
            dialog.addPageChangedListener(new IPageChangedListener() {
               
               @Override
               public void pageChanged(PageChangedEvent event)
               {
                  if (event.getSelectedPage() instanceof TggSpecChooserPage && selectedTggProjectEditBox.getText().length() == 0) {
                     button.notifyListeners(SWT.Selection, new Event());
                  }
               }
            });
         }
      }
	}
	
	private void createEcoreLocationGroup(Composite parent) {
		// create container group
		Composite locationGroup = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		locationGroup.setLayout(layout);
		locationGroup.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, true));
		
		// create label
		Label label = new Label(locationGroup, SWT.NONE);
		label.setText("Select a TGG project with the specification to generate testcases on");
		label.setFont(parent.getFont());
		
		createFileChooserGroup(locationGroup);
	}
	
	private void createFileChooserGroup(final Composite parent) {
		Composite fileChooserGroup = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		fileChooserGroup.setLayout(layout);
		fileChooserGroup.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, true));
		
		Label label = new Label(fileChooserGroup, SWT.NONE);
		label.setText("TGG project:");
		label.setFont(parent.getFont());
		
		selectedTggProjectEditBox = new Text(fileChooserGroup, SWT.BORDER);
		selectedTggProjectEditBox.setText("");
		selectedTggProjectEditBox.setFont(parent.getFont());
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		selectedTggProjectEditBox.setLayoutData(gridData);
		selectedTggProjectEditBox.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				final String path = selectedTggProjectEditBox.getText();
				final IPath ipath = new Path(path);
				if (ipath.isValidPath(path)) {
					final IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(path);
					try
               {
                  if (project != null && project.exists() && project.hasNature(WorkspaceHelper.INTEGRATION_NATURE_ID)) {
                  	setPageComplete(true);
                  	tggSpecProject = project;
                  } else {
                  	setPageComplete(false);
                  	tggSpecProject = null;
                  }
               } catch (CoreException e1)
               {
                  e1.printStackTrace();
               }
				} else {
					setPageComplete(false);
					tggSpecProject = null;
				}
			}
		});
		
		button = new Button(fileChooserGroup, SWT.PUSH);
		button.setText("Browse...");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {				
				ElementTreeSelectionDialog diag = new ElementTreeSelectionDialog(parent.getShell(),
																				 new WorkbenchLabelProvider(),
																				 new BaseWorkbenchContentProvider());
				diag.setTitle("Select TGG project");
				diag.setMessage("Select the TGG project with the Specification for testcase generation:");
				diag.setEmptyListMessage("Current Workspace contains no TGG Project. This has been analysed by the Projects Nature.");
				// Adds a filter, which extends "ViewFilter" to limit the Projects to integrationNature projects
				ViewerFilter filter = new IntegrationViewfilter();
				diag.addFilter(filter);
				diag.setInput(ResourcesPlugin.getWorkspace().getRoot());
				diag.setAllowMultiple(false);
				diag.setDoubleClickSelects(true);
				diag.setComparator(new WorkbenchViewerComparator());
				diag.setValidator(new ISelectionStatusValidator() {
					@Override
					public IStatus validate(Object[] selection) {
						if (selection.length == 1) {
							Object selected = selection[0];
							if (selected != null && (selected instanceof IProject)) {
							   tggSpecProject = (IProject) selected;
								try
                        {
                           if (tggSpecProject.hasNature(WorkspaceHelper.INTEGRATION_NATURE_ID)) {
                           	return new Status(Status.OK, UIActivator.PLUGIN_ID,  "Selected: " + tggSpecProject.getName());
                           }
                        } catch (CoreException e)
                        {
                           e.printStackTrace();
                        }
							}
						}
						return new Status(Status.ERROR, UIActivator.PLUGIN_ID, "Select a TGG project!");

					}
				});
				final int returnCode = diag.open();
				if (ElementTreeSelectionDialog.OK == returnCode) {
					Object[] result = diag.getResult();
					IProject project = (IProject) result[0];
					selectedTggProjectEditBox.setText(project.getFullPath().toPortableString());
				}
			}
		});		
	}

	@Override
	public boolean isPageComplete() {
		return super.isPageComplete();
	}

	public IProject getTggSpecProject()
	{
	   return tggSpecProject;
	}
}
