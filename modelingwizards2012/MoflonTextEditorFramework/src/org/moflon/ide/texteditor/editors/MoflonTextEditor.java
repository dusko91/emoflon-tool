package org.moflon.ide.texteditor.editors;

import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.ide.ResourceUtil;
import org.moflon.ide.texteditor.builtInEditors.CodeGen2TextEditorConfiguration;
import org.moflon.ide.texteditor.builtInEditors.TGGRuleTextEditorConfiguration;
import org.moflon.ide.texteditor.builtInEditors.TGGSchemaTextEditorConfiguration;
import org.moflon.ide.ui.admin.wizards.texteditor.TextEditorConfigurationClassGenerator;

/**
 * the main editor has the feature to be able to become configured before the
 * editor is instantiated
 * 
 * @author eleblebici, wienker
 * @author (last editor) $Author$
 * @version $Revision$ $Date$
 * 
 */
public class MoflonTextEditor extends TextEditor {

	private HashMap<String, MoflonTextEditorConfiguration> builtInEditors;

   public MoflonTextEditor() {
		super();

		// Set map of built in editors to supported file extensions
		builtInEditors = new HashMap<String, MoflonTextEditorConfiguration>();
		builtInEditors.put("vm", new CodeGen2TextEditorConfiguration());
		
		builtInEditors.put("tgg", new TGGRuleTextEditorConfiguration());
		builtInEditors.put("sch", new TGGSchemaTextEditorConfiguration());
   }

	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {

		super.init(site, input); 

		IFile openedFile = ResourceUtil.getFile(input);
		IProject projectOfOpenedFile = openedFile.getProject();
		TextEditorConfigurationClassGenerator generator = new TextEditorConfigurationClassGenerator(projectOfOpenedFile);
		
		// If we do not offer built-in support or if user already has a config
      if (!builtInEditors.containsKey(openedFile.getFileExtension()) || generator.configExists())
      {
         try
         {
            generator.doFinish();
         } catch (Exception e)
         {
            e.printStackTrace();
         }

         SourceViewerConfiguration sourceViewConfig = new MoflonSourceViewerConfiguration(projectOfOpenedFile);
         this.setSourceViewerConfiguration(sourceViewConfig);
      } 
      // Use built-in support to retrieve editor config
      else
      {
         SourceViewerConfiguration sourceViewConfig = new MoflonSourceViewerConfiguration(projectOfOpenedFile, builtInEditors.get(openedFile.getFileExtension()));
         this.setSourceViewerConfiguration(sourceViewConfig);
      }

	}

	/*
	 * @see IWorkbenchPart#dispose()
	 */
	public void dispose() {
		// colorManager.dispose();
		super.dispose();
	}


}
