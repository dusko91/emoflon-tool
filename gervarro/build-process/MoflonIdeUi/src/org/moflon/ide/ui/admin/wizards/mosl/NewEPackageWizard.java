package org.moflon.ide.ui.admin.wizards.mosl;

import static org.moflon.ide.ui.admin.wizards.util.WizardUtil.loadStringTemplateGroup;
import static org.moflon.ide.ui.admin.wizards.util.WizardUtil.renderTemplate;

import java.util.HashMap;
import java.util.Map;

import org.antlr.stringtemplate.StringTemplateGroup;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.INewWizard;
import org.moflon.ide.ui.admin.wizards.AbstractWizard;
import org.moflon.util.WorkspaceHelper;

public class NewEPackageWizard extends AbstractWizard implements INewWizard
{
   // Page containing controls for taking user input
   private NewEPackagePage eclassInfo;

   @Override
   public void addPages()
   {
      eclassInfo = new NewEPackagePage();
      addPage(eclassInfo);
   }

   protected void doFinish(IProgressMonitor monitor) throws CoreException
   {
      String packageName = eclassInfo.getPackageName();
      
      monitor.beginTask("", 1);
      
      StringTemplateGroup stg = loadStringTemplateGroup("/resources/mosl/templates/constraints.stg");
      
      Map<String, Object> attrs = new HashMap<String, Object>();
      String content = renderTemplate(stg, "constraints", attrs);
      
      String packagePath = resource.getProjectRelativePath() + "/" + packageName;
      
      WorkspaceHelper.addFolder(resource.getProject(), packagePath, monitor);
      WorkspaceHelper.addFolder(resource.getProject(), packagePath + "/_patterns", monitor);
      WorkspaceHelper.addFile(resource.getProject(), packagePath + "/_constraints.mconf", content, monitor);
      
      monitor.done();
   }
   
}
