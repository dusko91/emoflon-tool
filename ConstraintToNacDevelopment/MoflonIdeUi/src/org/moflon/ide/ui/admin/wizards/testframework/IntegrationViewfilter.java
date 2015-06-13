package org.moflon.ide.ui.admin.wizards.testframework;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.moflon.util.WorkspaceHelper;

public class IntegrationViewfilter extends ViewerFilter
{

   @Override
   public boolean select(Viewer viewer, Object parentElement, Object element)
   {
      // On default everything that can be cast to 'IProject' will be available
      // Everything else, i.e. files of different kind, aren't available
      try{
         IProject project = (IProject)element;
         try
         {
            // If 'hasNature()' fails the project will be available in the Chooser, 
            // but there will be a remark its not of the Integration Nature
            if(!project.hasNature(WorkspaceHelper.INTEGRATION_NATURE_ID))
               return false;
         } catch (CoreException e)
         {
            e.printStackTrace();
         }
         return true;
      } catch (ClassCastException e){
      }
      return false;
   }

}
