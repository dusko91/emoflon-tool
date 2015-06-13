package org.moflon.ide.ui.admin.wizards.moca;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.moflon.ide.ui.UIActivator;
import org.moflon.ide.ui.admin.wizards.AbstractFileGenerator;

/**
 * Wizard for creating a Parser and/or Unparser
 * 
 * @author david
 * @author (last editor) $Author$
 * @version $Revision$ $Date$
 */

public class AddParserAndUnparserWizard extends Wizard implements INewWizard
{
   private AddParserAndUnparserWizardPage page;

   private IProject project;

   private AbstractFileGenerator generator;

   private static Logger logger = Logger.getLogger(UIActivator.class);

   @Override
   public void init(IWorkbench workbench, IStructuredSelection selection)
   {
      // Check if selection is valid (i.e., inside Package Explorer or Navigator
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
   public void addPages()
   {
      page = new AddParserAndUnparserWizardPage();
      addPage(page);
   }

   @Override
   public boolean performFinish()
   {
      if (project != null)
      {
         generator = new ParserUnparserGenerator(page, project);
         try
         {
            String message = generator.doFinish();
            if (message.length() > 0)
            {
               MessageDialog.openInformation(getShell(), "Creating Parser/Unparser", message);
            }
         } catch (Exception e)
         {
            logger.debug("error while creating parser/unparser");
            e.printStackTrace();
         }
      } else
      {
         logger.debug("no project was selected!");
      }
      return true;
   }

}
