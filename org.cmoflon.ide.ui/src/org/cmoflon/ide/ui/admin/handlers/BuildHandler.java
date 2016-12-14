package org.cmoflon.ide.ui.admin.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.cmoflon.ide.core.runtime.natures.CMoflonRepositoryNature;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.moflon.ide.ui.admin.handlers.AbstractCommandHandler;

/**
 * Replacement of eMoflons {@link org.moflon.ide.ui.admin.handlers.BuildHandler}.<br>
 *  Builds the CMoflon Repository Projects(and only those).
 * @author David Giessing
 *
 */
public class BuildHandler extends AbstractCommandHandler
{

   @Override
   public Object execute(ExecutionEvent event) throws ExecutionException
   {

      final ISelection selection = HandlerUtil.getCurrentSelectionChecked(event);
      final List<IProject> projects = new ArrayList<>();
      if (selection instanceof StructuredSelection)
      {
         final StructuredSelection structuredSelection = (StructuredSelection) selection;
         for (final Iterator<?> selectionIterator = structuredSelection.iterator(); selectionIterator.hasNext();)
         {
            projects.addAll(getProjects(selectionIterator.next()));
         }
      } else if (selection instanceof ITextSelection)
      {
         final IFile file = getEditedFile(event);
         final IProject project = file.getProject();
         projects.add(project);
      }
      //Only allow CMoflonRepoProjects to be build
      for (IProject p : projects)
      {
         try
         {
            if (!p.hasNature(CMoflonRepositoryNature.class.getName()))
               projects.remove(p);
         } catch (CoreException e)
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
      cleanAndBuild(projects);

      return null;
   }

   private void cleanAndBuild(List<IProject> projects)
   {
      if (!projects.isEmpty())
      {

         final Job job = new CMoflonBuildJob("CMoflon Building", projects);
         job.setUser(true);
         job.schedule();
      }

   }

}