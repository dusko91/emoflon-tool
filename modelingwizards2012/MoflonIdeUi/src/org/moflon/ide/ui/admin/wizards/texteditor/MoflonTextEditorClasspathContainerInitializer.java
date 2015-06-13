package org.moflon.ide.ui.admin.wizards.texteditor;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.moflon.ide.core.admin.MoflonClasspathContainerInitializer;

public class MoflonTextEditorClasspathContainerInitializer extends ClasspathContainerInitializer
{

	   private static final Logger logger = Logger.getLogger(MoflonClasspathContainerInitializer.class);

	   @Override
	   public void initialize(IPath containerPath, IJavaProject project) throws CoreException
	   {
		   MoflonTextEditorClasspathContainer container = new MoflonTextEditorClasspathContainer(containerPath, project);
	      if (container.isValid())
	      {
	         JavaCore.setClasspathContainer(containerPath, new IJavaProject[] { project }, new IClasspathContainer[] { container }, null);
	      } else
	      {
	         logger.warn("InvalidContainer: " + containerPath);
	      }
	   }
}
