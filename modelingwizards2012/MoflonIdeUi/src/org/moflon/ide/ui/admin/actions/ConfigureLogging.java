package org.moflon.ide.ui.admin.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowPulldownDelegate;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.moflon.ide.core.CoreActivator;
import org.moflon.ide.ui.UIActivator;

/**
 * Action to open log file or configuration file in an editor directly in the running eclipse instance. Log4j can also
 * be reconfigured after changes to the configuration file. The files are retrieved transparently and the user does not
 * need to care about where they are located.
 * 
 * @author anjorin
 * @author (last editor) $Author$
 * @version $Revision$ $Date$
 */
public class ConfigureLogging implements IWorkbenchWindowPulldownDelegate
{
   private IWorkbenchWindow window;

   @Override
   public void init(IWorkbenchWindow window)
   {
      this.window = window;
   }

   @Override
   public void run(IAction action)
   {
      UIActivator.getDefault().openLogFileInEditor(window);
   }

   @Override
   public Menu getMenu(Control parent)
   {
      // Create root
      Menu menu = new Menu(parent);

      // Add pulldown item for opening log file
      org.eclipse.swt.graphics.Image image = AbstractUIPlugin.imageDescriptorFromPlugin(UIActivator.PLUGIN_ID, "resources/icons/logfile.png").createImage();
      MenuItem menuItem = new org.eclipse.swt.widgets.MenuItem(menu, SWT.NONE);
      menuItem.setImage(image);
      menuItem.setText("Open Logfile");
      menuItem.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e)
         {
            run(null);
         }
      });

      new org.eclipse.swt.widgets.MenuItem(menu, SWT.SEPARATOR);

      // Add pulldown item for opening config file
      menuItem = new org.eclipse.swt.widgets.MenuItem(menu, SWT.NONE);
      menuItem.setText("Edit Config File");
      menuItem.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e)
         {
            UIActivator.getDefault().openConfigFileInEditor(window);
         }
      });

      // Add pulldown item for reconfiguring log4j
      menuItem = new org.eclipse.swt.widgets.MenuItem(menu, SWT.NONE);
      menuItem.setText("Reconfigure Logging");
      menuItem.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e)
         {
            CoreActivator.getDefault().reconfigureLogging();
         }
      });

      return menu;
   }

   @Override
   public void selectionChanged(IAction action, ISelection selection)
   {
   }

   @Override
   public void dispose()
   {
   }
}
