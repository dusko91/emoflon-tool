package org.moflon2contiki.ide.ui.runtime.natures;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import org.moflon2contiki.ide.core.ContikiCoreActivator;

public class ContikiMetamodelNature implements IProjectNature{
	private IProject project;

	   @Override
	   public void configure() throws CoreException
	   {
	      // Get project description and add model builder
	      IProjectDescription desc = project.getDescription();
	      ICommand command = desc.newCommand();
	      command.setBuilderName(ContikiCoreActivator.METAMODEL_BUILDER_ID);
	      desc.setBuildSpec(new ICommand[] { command });
	      // Reset augmented description
	      project.setDescription(desc, null);
	   }

	   @Override
	   public void deconfigure() throws CoreException
	   {
	   }

	   @Override
	   public IProject getProject()
	   {
	      return project;
	   }

	   @Override
	   public void setProject(IProject project)
	   {
	      this.project = project;
	   }

}
