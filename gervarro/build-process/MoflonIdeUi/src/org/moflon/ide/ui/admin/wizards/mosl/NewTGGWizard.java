package org.moflon.ide.ui.admin.wizards.mosl;

import static org.moflon.ide.ui.admin.wizards.util.WizardUtil.loadStringTemplateGroup;
import static org.moflon.ide.ui.admin.wizards.util.WizardUtil.renderTemplate;

import java.util.HashMap;
import java.util.Map;

import org.antlr.stringtemplate.StringTemplateGroup;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.INewWizard;
import org.moflon.ide.ui.admin.wizards.AbstractWizard;
import org.moflon.util.WorkspaceHelper;

public class NewTGGWizard extends AbstractWizard implements INewWizard
{
   // Page containing controls for taking user input
   private NewTGGPage tggInfo;

   @Override
   public void addPages()
   {
      tggInfo = new NewTGGPage();
      addPage(tggInfo);
   }

   protected void doFinish(IProgressMonitor monitor) throws CoreException
   {
      String packageName = tggInfo.getPackageName();
      String sourceLanguage = tggInfo.getSourceLanguage();
      String targetLanguage = tggInfo.getTargetLanguage();
      
      monitor.beginTask("", 1);
      
      StringTemplateGroup stg = loadStringTemplateGroup("/resources/mosl/templates/schema.stg");
      
      Map<String, Object> attrs = new HashMap<String, Object>();
      attrs.put("source", sourceLanguage);
      attrs.put("target", targetLanguage);
      String content = renderTemplate(stg, "schema", attrs);
      
      String packagePath = resource.getProjectRelativePath() + "/" + packageName;
      
      WorkspaceHelper.addFolder(resource.getProject(), packagePath, monitor);
      WorkspaceHelper.addFolder(resource.getProject(), packagePath + "/Rules", monitor);
      WorkspaceHelper.addFile(resource.getProject(), packagePath + "/schema.sch", content, monitor);
      
      IFile schemaFile = resource.getProject().getFile(packagePath + "/schema.sch");
      
      openEditor(schemaFile);
      
      monitor.done();
   }
   
}
