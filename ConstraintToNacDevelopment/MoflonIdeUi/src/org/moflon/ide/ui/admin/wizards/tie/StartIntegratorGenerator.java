package org.moflon.ide.ui.admin.wizards.tie;

import java.net.URL;

import org.eclipse.core.resources.IProject;
import org.moflon.Activator;
import org.moflon.ide.core.CoreActivator;
import org.moflon.ide.ui.UIActivator;
import org.moflon.moca.tie.AbstractIntegratorGenerator;
import org.moflon.util.WorkspaceHelper;

public class StartIntegratorGenerator extends AbstractIntegratorGenerator
{
   public StartIntegratorGenerator(IProject project)
   {
      super(project);
   }

   @Override
   protected String getPackagePrefix()
   {
      return "org.moflon.tie";
   }

   @Override
   protected String getTemplateName()
   {
      return "StartIntegrator";
   }

   @Override
   protected String getPathToFileToBeGenerated()
   {
      return "/src/org/moflon/tie/StartIntegrator.java";
   }

   @Override
   protected URL getTemplateFileURL()
   {
      return Activator.getPathRelToPlugIn("/resources/moca/templates/TiE/StartIntegrator.stg", UIActivator.PLUGIN_ID);
   }

   @Override
   protected String getSupportedNature()
   {
      return WorkspaceHelper.REPOSITORY_NATURE_ID;
   }
}
