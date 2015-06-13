package org.moflon.moca.tie;

import java.net.URL;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.moflon.MoflonDependenciesPlugin;
import org.moflon.ide.core.CoreActivator;
import org.moflon.util.WorkspaceHelper;

public class RunIntegrationGeneratorBatch extends AbstractIntegratorGenerator
{
   public RunIntegrationGeneratorBatch(IProject project)
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
      return "TGGMainBatch";
   }

   @Override
   protected String getPathToFileToBeGenerated()
   {
      return "/src/org/moflon/tie/" + getClassName() + ".java";
   }

   protected String getClassName()
   {
      return project.getName() + "Trafo";
   }

   @Override
   protected URL getTemplateFileURL()
   {
      return MoflonDependenciesPlugin.getPathRelToPlugIn("/resources/templates/TGGMain.stg", CoreActivator.PLUGIN_ID);
   }

   @Override
   protected String getSupportedNature()
   {
      return WorkspaceHelper.REPOSITORY_NATURE_ID;
   }

   @Override
   protected Map<String, Object> extractTemplateParameters()
   {
      Map<String, Object> attributes = super.extractTemplateParameters();
      attributes.put("corrPackage", project.getName() + "Package.eINSTANCE");
      attributes.put("className", getClassName());
      return attributes;
   }

}
