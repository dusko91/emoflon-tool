package org.moflon.tgg.mosl.ui.visualisation;

import java.util.HashMap;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPartConstants;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.moflon.core.utilities.LogUtils;
import org.moflon.core.utilities.MoflonUtil;
import org.moflon.core.utilities.WorkspaceHelper;
import org.moflon.core.utilities.eMoflonEMFUtil;
import org.moflon.ide.visualisation.dot.language.DotUnparserAdapter;
import org.moflon.ide.visualisation.dot.language.ToggleRefinementHandler;
import org.moflon.ide.visualization.dot.language.DirectedGraph;
import org.moflon.ide.visualization.dot.tgg.TGGRuleDiagramTextProvider;
import org.moflon.ide.visualization.dot.tgg.schema.TGGSchemaDiagramTextProvider;
import org.moflon.tgg.language.TGGRule;
import org.moflon.tgg.language.TripleGraphGrammar;

import net.sourceforge.plantuml.eclipse.utils.AbstractDiagramTextProvider;

public class MOSLTGGDiagramTextProvider extends AbstractDiagramTextProvider {
   private static final Logger logger = Logger.getLogger(MOSLTGGDiagramTextProvider.class);
	private boolean outdated = false;
	private XtextEditor oldEditor;
	private HashMap<String, String> oldValue = new HashMap<>();
	private String currentTggFile = WorkspaceHelper.PRE_TGG_FILE_EXTENSION;
	
	private IPropertyListener listener = (o, p) -> {
		if (p == IWorkbenchPartConstants.PROP_DIRTY && !oldEditor.isDirty()) {
			Job j = new Job("set outdated") {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					try {
						IJobManager jobManager = Job.getJobManager();

						// Wait for auto build to finish if running
						jobManager.join(ResourcesPlugin.FAMILY_AUTO_BUILD, new NullProgressMonitor());
						outdated = true;
					} catch (Exception e) {
                  LogUtils.error(logger, e);
					}

					return Status.OK_STATUS;
				}
			};
			
			j.setPriority(Job.LONG);
			j.schedule();
		}
   };
	
	
   @Override
   public String getDiagramText(IEditorPart editorPart, IEditorInput editorInput)
   {
      try
      {
    	 TripleGraphGrammar tgg = getTGG();
         ISelection selection = editorPart.getSite().getSelectionProvider().getSelection();
         Optional<TGGRule> rule = getTGGRuleForSelection(selection, tgg);

         if (rule.isPresent() && oldValue.containsKey(rule.get().getName()) && !outdated)
            return oldValue.get(rule.get().getName());

         TGGSchemaDiagramTextProvider schemaProvider = new TGGSchemaDiagramTextProvider();
         
         DirectedGraph graph = (DirectedGraph)schemaProvider.modelToDot(tgg);
         String schemaDiagram;
         if(graph != null)
        	 schemaDiagram = new DotUnparserAdapter().unparse(graph);
         else
        	 schemaDiagram = "@startuml @enduml";
         
         return rule.map(r -> {
            outdated = false;
            TGGRuleDiagramTextProvider tggTextProvider = new TGGRuleDiagramTextProvider();
            String diagram = new DotUnparserAdapter().unparse(tggTextProvider.modelToDot(r));
            oldValue.put(r.getName(), diagram);
            return diagram;
         }).orElse(schemaDiagram);
         
      } catch (Exception e)
      {
         LogUtils.error(logger, e);
         return "@startuml @enduml";
      }
   }

	@Override
	public boolean supportsEditor(IEditorPart editorPart) {
		if(oldEditor != null && oldEditor.equals(editorPart))
			return true;
		
		if(editorPart instanceof XtextEditor){
			XtextEditor ed  = (XtextEditor)editorPart;
			if("org.moflon.tgg.mosl.TGG".equals(ed.getLanguageName())){
				oldEditor = ed;
				oldValue = new HashMap<>();
				oldEditor.addPropertyListener(listener);
				return true;
			}
		}
		
		return false;
	}

	private Optional<TGGRule> getTGGRuleForSelection(ISelection selection, TripleGraphGrammar tgg) { 
		IPath ruleNamePath = new Path(oldEditor.getEditorInput().getName());
		ruleNamePath = ruleNamePath.removeFileExtension();
		String ruleName = ruleNamePath.toString();
		
		String selectedRuleName = extractRuleName(selection);

		
			if (tgg!=null) {				
				return tgg.getTggRule()
				      .stream()
				      .filter(r -> r.getName().equals(ruleName) || r.getName().equals(selectedRuleName))
				      .findAny();
			}
		

		return Optional.empty();
	}
	
	private TripleGraphGrammar getTGG() {
		if (oldEditor != null && oldEditor.getEditorInput() instanceof FileEditorInput) {
			IFile file = FileEditorInput.class.cast(oldEditor.getEditorInput()).getFile();
			IProject project = file.getProject();
			IFile tggFile = project.getFile(MoflonUtil.getDefaultPathToFileInProject(project.getName(), getTGGFileWithRules()));
			if (tggFile.exists()) {
				ResourceSet rs = eMoflonEMFUtil.createDefaultResourceSet();
				URI uri = URI.createPlatformResourceURI(tggFile.getFullPath().toString(), true);
				Resource tggResource = rs.getResource(uri, true);
				TripleGraphGrammar tgg = (TripleGraphGrammar) tggResource.getContents().get(0);
				return tgg;
			}
		}
		return null;
	}

   private String getTGGFileWithRules() {
	   String tggFile = ToggleRefinementHandler.flattenRefinements()? WorkspaceHelper.TGG_FILE_EXTENSION : WorkspaceHelper.PRE_TGG_FILE_EXTENSION;
	   if(!currentTggFile.equals(tggFile)){
		   currentTggFile = tggFile;
		   outdated = true;
	   }
	   
	   return currentTggFile;
   }

   private String extractRuleName(ISelection selection)
   {
      if(selection instanceof ITextSelection){
         ITextSelection textSelection = (ITextSelection)selection;
         return textSelection.getText();
      } else
         return "";

   }
}
