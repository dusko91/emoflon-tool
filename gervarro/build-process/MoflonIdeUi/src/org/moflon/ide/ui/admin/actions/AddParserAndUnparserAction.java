package org.moflon.ide.ui.admin.actions;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.moflon.ide.ui.UIActivator;

/**
 * This Action opens the "Add Parser" Wizard
 */
public class AddParserAndUnparserAction implements IObjectActionDelegate 
{

   private IWorkbenchWindow window;
   
   private Logger logger = Logger.getLogger(AddParserAndUnparserAction.class);

   private IStructuredSelection selection;

   @Override
   public void run(IAction action)
   {
      try
      {
         UIActivator.openWizard(UIActivator.ADD_PARSER_AND_UNPARSER_WIZARD_ID, window, selection);
      } catch (CoreException e)
      {
         logger.debug("unable to open wizard");
         e.printStackTrace();
      }
   }
   
   @Override
   public void selectionChanged(IAction action, ISelection selection)
   {  
      if (selection != null && selection instanceof IStructuredSelection) {
         this.selection = (IStructuredSelection) selection;
      }      
   }   

   @Override
   public void setActivePart(IAction action, IWorkbenchPart targetPart)
   {
      window = targetPart.getSite().getWorkbenchWindow();      
   }

}
