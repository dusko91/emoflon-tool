package org.moflon.ide.ui.admin.actions;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.moflon.ide.ui.UIActivator;
import org.moflon.ide.ui.admin.wizards.metamodel.NewMetamodelWizard;

/**
 * Actions launches {@link NewMetamodelWizard}
 * 
 * @author anjorin
 * @author (last editor) $Author$
 * @version $Revision$ $Date$
 */
public class NewMetamodelAction implements IWorkbenchWindowActionDelegate
{
   private final Logger logger = Logger.getLogger(NewMetamodelAction.class);

   private IWorkbenchWindow window;

   @Override
   public void init(IWorkbenchWindow window)
   {
      this.window = window;
   }

   @Override
   public void run(IAction action)
   {
      try
      {
         UIActivator.openWizard(UIActivator.NEW_METAMODEL_WIZARD_ID, window);
      } catch (Exception e)
      {
         logger.error("unable to open wizard");
         e.printStackTrace();
      }
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
