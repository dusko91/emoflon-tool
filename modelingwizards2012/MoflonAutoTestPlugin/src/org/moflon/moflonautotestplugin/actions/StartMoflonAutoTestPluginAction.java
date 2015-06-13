package org.moflon.moflonautotestplugin.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.IWorkbenchWindowPulldownDelegate;

import autobuildplugin.Activator;
import autobuildplugin.DeploymentHelper;
import autobuildplugin.MoflonWorkspace;
import autobuildplugin.preferences.CompileTextualTGGsHelper;
import autobuildplugin.preferences.PreferenceConstants;

/**
 * Starting the Moflon AutoBuild Process. The hook is implemented as an Action Set, which can be customized by every
 * view.
 * 
 * The AutoBuild Process consists of the following steps: a) delete the current workspace b) import the selected project
 * set c) building EAP files d) refreshing and code generating the workspace e) running the junit test suite
 * 
 * @see IWorkbenchWindowActionDelegate
 * @see MoflonWorkspace
 */
public class StartMoflonAutoTestPluginAction implements IWorkbenchWindowPulldownDelegate
{
   private IWorkbenchWindow window;

   /**
    * The constructor.
    */
   public StartMoflonAutoTestPluginAction()
   {
   }

   /**
    * The action has been activated. The argument of the method represents the 'real' action sitting in the workbench
    * UI.
    * 
    * @see IWorkbenchWindowActionDelegate#run
    */
   public void run(IAction action)
   {
      MoflonWorkspace.run(window);
   }

   /**
    * Selection in the workbench has been changed. We can change the state of the 'real' action here if we want, but
    * this can only happen after the delegate has been created.
    * 
    * @see IWorkbenchWindowActionDelegate#selectionChanged
    */
   public void selectionChanged(IAction action, ISelection selection)
   {
   }

   /**
    * We can use this method to dispose of any system resources we previously allocated.
    * 
    * @see IWorkbenchWindowActionDelegate#dispose
    */
   public void dispose()
   {
   }

   /**
    * We will cache window object in order to be able to provide parent shell for the message dialog.
    * 
    * @see IWorkbenchWindowActionDelegate#init
    */
   public void init(IWorkbenchWindow window)
   {
      this.window = window;
   }

   @Override
   public Menu getMenu(Control parent)
   {
      Menu menu = new Menu(parent);
      MenuItem menuItem = new org.eclipse.swt.widgets.MenuItem(menu, SWT.NONE);
      menuItem.setText(PreferenceConstants.NEXTOPERATION.DELETEWORKSPACE.toString());
      menuItem.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e)
         {
            IPreferenceStore store = Activator.getDefault().getPreferenceStore();
            store.setValue(PreferenceConstants.AB_NEXTOP, PreferenceConstants.NEXTOPERATION.DELETEWORKSPACE.toString());
            MoflonWorkspace.run(window);
         }
      });
      menuItem = new org.eclipse.swt.widgets.MenuItem(menu, SWT.NONE);
      menuItem.setText(PreferenceConstants.NEXTOPERATION.IMPORTPROJECTSET.toString());
      menuItem.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e)
         {
            IPreferenceStore store = Activator.getDefault().getPreferenceStore();
            store.setValue(PreferenceConstants.AB_NEXTOP, PreferenceConstants.NEXTOPERATION.IMPORTPROJECTSET.toString());
            MoflonWorkspace.run(window);
         }
      });

      menuItem = new org.eclipse.swt.widgets.MenuItem(menu, SWT.NONE);
      menuItem.setText(PreferenceConstants.NEXTOPERATION.MOFLONBUILD.toString());
      menuItem.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e)
         {
            IPreferenceStore store = Activator.getDefault().getPreferenceStore();
            store.setValue(PreferenceConstants.AB_NEXTOP, PreferenceConstants.NEXTOPERATION.MOFLONBUILD.toString());
            MoflonWorkspace.run(window);
         }
      });

      menuItem = new org.eclipse.swt.widgets.MenuItem(menu, SWT.NONE);
      menuItem.setText(PreferenceConstants.NEXTOPERATION.REFRESH.toString());
      menuItem.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e)
         {
            IPreferenceStore store = Activator.getDefault().getPreferenceStore();
            store.setValue(PreferenceConstants.AB_NEXTOP, PreferenceConstants.NEXTOPERATION.REFRESH.toString());
            MoflonWorkspace.run(window);
         }
      });

      menuItem = new org.eclipse.swt.widgets.MenuItem(menu, SWT.NONE);
      menuItem.setText(PreferenceConstants.NEXTOPERATION.JUNIT.toString());
      menuItem.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e)
         {
            IPreferenceStore store = Activator.getDefault().getPreferenceStore();
            store.setValue(PreferenceConstants.AB_NEXTOP, PreferenceConstants.NEXTOPERATION.JUNIT.toString());
            MoflonWorkspace.run(window);
         }
      });
      
      menuItem = new MenuItem(menu, SWT.SEPARATOR);
      
      // Menu item for testing the model creating from text
      menuItem = new org.eclipse.swt.widgets.MenuItem(menu, SWT.NONE);
      
      final String menuitemModelFromText = "Create models from text";
      menuItem.setText(menuitemModelFromText);
      
      menuItem.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e)
         {
            Display.getDefault().asyncExec(new Runnable() {
               @Override
               public void run() {
                  if (MessageDialog.openConfirm(null, menuitemModelFromText, "Overwrite existing models by models from text?")) {                     
                     CompileTextualTGGsHelper.start();
                  }
               }
            });
         }
      });
            
      menuItem = new MenuItem(menu, SWT.SEPARATOR);
      
      menuItem = new org.eclipse.swt.widgets.MenuItem(menu, SWT.NONE);
      final String deployBeta = "Deploy eMoflon (beta)";
      menuItem.setText(deployBeta);
      menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						if (MessageDialog.openConfirm(null, deployBeta, "Are you sure you want to (beta) deploy eMoflon? This cannot be reverted!")) {							
							DeploymentHelper.start(1);														
						}
					}
				});
			}
		});
      
      menuItem = new MenuItem(menu, SWT.SEPARATOR);
      
      menuItem = new org.eclipse.swt.widgets.MenuItem(menu, SWT.NONE);
      final String deployRelease= "Deploy eMoflon (release)";
      menuItem.setText(deployRelease);
      menuItem.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						if (MessageDialog.openConfirm(null, deployRelease, "Are you sure you want to release eMoflon? This cannot be reverted!")) {							
							DeploymentHelper.start(0);													
						}
					}
				});
			}
      });
      
      menuItem = new MenuItem(menu, SWT.SEPARATOR);
      
      menuItem = new org.eclipse.swt.widgets.MenuItem(menu, SWT.NONE);
      final String deployLocal= "Deploy eMoflon (local)";
      menuItem.setText(deployLocal);
      menuItem.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						if (MessageDialog.openConfirm(null, deployLocal, "Are you sure you want to release eMoflon? This cannot be reverted!")) {							
							DeploymentHelper.start(2);													
						}
					}
				});
			}
      });

      return menu;
   }
}