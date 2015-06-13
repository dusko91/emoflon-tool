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

public class NewEClassWizard extends AbstractWizard implements INewWizard
{
   // Page containing controls for taking user input
   private NewEClassPage eclassInfo;
   
   @Override
   public void addPages()
   {
      eclassInfo = new NewEClassPage();
      addPage(eclassInfo);
   }

   protected void doFinish(IProgressMonitor monitor) throws CoreException
   {
      String className = eclassInfo.getClassName();
      
      monitor.beginTask("", 1);
      
      StringTemplateGroup stg = loadStringTemplateGroup("/resources/mosl/templates/eclass.stg");
      
      Map<String, Object> attrs = new HashMap<String, Object>();
      attrs.put("name", className);
      String content = renderTemplate(stg, "eclass", attrs);
      
      String filePath = resource.getProjectRelativePath() + "/" + className + ".eclass";
      WorkspaceHelper.addFile(resource.getProject(), filePath, content, monitor);
      
      IFile classFile = resource.getProject().getFile(filePath);
      
      openEditor(classFile);
      
      monitor.done();
   }
}
