package org.moflon.ide.ui.admin.actions;

import java.io.InputStream;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.moflon.moca.inject.InjectionFile;
import org.moflon.util.WorkspaceHelper;

public class CreateInjectionAction implements IObjectActionDelegate
{
   private static final Logger logger = Logger.getLogger(CreateInjectionAction.class);
   
   private IFile file;

   @Override
   public void run(IAction action)
   {
      if(file == null)
         return;
      
      IProject project = file.getProject();
      IFolder injectionFolder = project.getFolder("injection");
      
      IPath pathToFile = file.getProjectRelativePath().removeFirstSegments(1);
      IPath pathToInjection = pathToFile.removeFileExtension().addFileExtension("inject");
      
      // Determine contents of file
      String injContent = "";
      InputStream javaContent = null;
      try
      {
         javaContent = file.getContents();
      } catch (CoreException e1)
      {
         e1.printStackTrace();
      }
      InjectionFile injFile = new InjectionFile(javaContent, file.getName().replace(".java", ""));
      injContent = injFile.getFileContent();
      
      
      // insert the contents
      try
      {
         project.getFile(injectionFolder.getProjectRelativePath().append(pathToInjection)).delete(true, new NullProgressMonitor());
         
         WorkspaceHelper.addAllFoldersAndFile(project, injectionFolder.getProjectRelativePath().append(pathToInjection), injContent, new NullProgressMonitor());
      } catch (CoreException e)
      {
         logger.error("Unable to create injection code for " + file + " due to " + e);
      }
   }

   @Override
   public void selectionChanged(IAction action, ISelection selection)
   {  
      if (selection != null && selection instanceof IStructuredSelection) {
         Object o = ((IStructuredSelection) selection).getFirstElement();
         if(o instanceof IFile)
            file = (IFile)o;
      }      
   }   

   @Override
   public void setActivePart(IAction action, IWorkbenchPart targetPart)
   {  
      
   }
   

}
