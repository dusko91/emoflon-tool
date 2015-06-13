package org.moflon.ide.texteditor.editors;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.projection.ProjectionSupport;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.ide.ResourceUtil;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.moflon.ide.texteditor.builders.TextEditorBuilder;
import org.moflon.ide.texteditor.config.MoflonTextEditorConfigIntern;
import org.moflon.ide.texteditor.modules.folding.MoflonFoldingHandler;
import org.moflon.ide.texteditor.modules.outline.MocaTreeContentOutlinePage;

import MocaTree.Node;

/**
 * the main editor has the feature to be able to become configured before the editor is instantiated
 * 
 * @author eleblebici, wienker
 * @author (last editor) $Author$
 * @version $Revision$ $Date$
 * 
 */
public abstract class MoflonTextEditor extends TextEditor
{

   public static boolean IsSaving = false;

   public boolean fileChanged = false;

   MocaTreeContentOutlinePage contentOutlinePage;

   ProjectionSupport projectionSupport;

   MoflonFoldingHandler foldingProvider;

   
   
   public void init(IEditorSite site, IEditorInput input) throws PartInitException
   {
      super.init(site, input);
      
      IFile openedFile = ResourceUtil.getFile(input);
      
      // Unable to retrieve file so give up (reverts to normal default TextEditor)
      if(openedFile == null)
    	  return;
      
      IProject projectOfOpenedFile = openedFile.getProject();
      configure(projectOfOpenedFile, openedFile);
      this.foldingProvider = new MoflonFoldingHandler(this);
   }

   /*
    * performing a full refresh of the textviewer during save
    * 
    * @see org.eclipse.ui.texteditor.AbstractTextEditor#doSave(org.eclipse.core. runtime.IProgressMonitor)
    */
   private static final String DEPENDENCIES = "org.moflon.dependencies";

   @Override
   public void doSave(org.eclipse.core.runtime.IProgressMonitor progressMonitor)
   {
      IsSaving = true;
      super.doSave(progressMonitor);

      // refresh texteditor
      getSourceViewer().invalidateTextPresentation();
      // refresh outlineview
      refreshTreeModules();
      IsSaving = false;
   }

   public ISourceViewer getSourceViewerPublic()
   {
      return super.getSourceViewer();
   }

   /*
    * public ITextViewer getTextViewer() {
    * 
    * ITextOperationTarget target = (ITextOperationTarget) getAdapter(ITextOperationTarget.class); if (target instanceof
    * ITextViewer) { ITextViewer textViewer = (ITextViewer) target; return textViewer; } return null; }
    */

   public void refreshTreeModules()
   {
      /*
       * use the internal antlr parser for .g files CharStream input = new ANTLRFileStream(file.getAbsolutePath()); //
       * BUILD AST ANTLRv3Lexer lex = new ANTLRv3Lexer(input); CommonTokenStream tokens = new CommonTokenStream(lex);
       * ANTLRv3Parser g = new ANTLRv3Parser(tokens); ANTLRv3Parser.grammarDef_return r = g.grammarDef(); CommonTree t =
       * (CommonTree) r.getTree();
       */
      String resourceFilePath = getMoflonTextEditorConfiguration().getResource().getLocation().toString();
      Node tree = getMoflonTextEditorConfiguration().getUnderlyingTree(resourceFilePath);
      if (tree != null)
      {
         getMocaTreeContentOutlinePage().refreshOutlineView(tree);
         foldingProvider.refreshAnnotations(tree);
      }
   }

   public Object getAdapter(Class key)
   {
      if (key.equals(IContentOutlinePage.class))
      {
         return getMocaTreeContentOutlinePage();
      } else
      {
         return super.getAdapter(key);
      }
   }

   /**
    * This accesses a cached version of the content outliner. <!-- begin-user-doc --> <!-- end-user-doc -->
    * 
    * @generated
    */
   public MocaTreeContentOutlinePage getMocaTreeContentOutlinePage()
   {
      if (contentOutlinePage == null)
      {
         contentOutlinePage = new MocaTreeContentOutlinePage(this);
      }

      return contentOutlinePage;
   }

   /**
    * Create appropriate editor configuration and use to initialize the source view configuration.
    * 
    * @param projectOfOpenedFile
    * @param openedFile
    */
   protected abstract void configure(IProject projectOfOpenedFile, IFile openedFile);

   /*
    * For folding we need ProjectionViewer instead of the default SourceViewer.
    * 
    * @see org.eclipse.ui.texteditor.AbstractDecoratedTextEditor#createSourceViewer(org.eclipse.swt.widgets.Composite,
    * org.eclipse.jface.text.source.IVerticalRuler, int)
    */
   @Override
   protected ISourceViewer createSourceViewer(Composite parent, IVerticalRuler ruler, int styles)
   {
      fAnnotationAccess = getAnnotationAccess();
      fOverviewRuler = createOverviewRuler(getSharedColors());

      ISourceViewer viewer = new ProjectionViewer(parent, ruler, getOverviewRuler(), isOverviewRulerVisible(), styles);
      // ensure decoration support has been created and configured.
      getSourceViewerDecorationSupport(viewer);

      return viewer;
   }

   @Override
   public void createPartControl(Composite parent)
   {
      super.createPartControl(parent);
      ProjectionViewer viewer = (ProjectionViewer) getSourceViewer();

      projectionSupport = new ProjectionSupport(viewer, getAnnotationAccess(), getSharedColors());
      projectionSupport.install();

      // turn projection mode on
      viewer.doOperation(ProjectionViewer.TOGGLE);

   }
   
   
   public MoflonTextEditorConfigIntern getMoflonTextEditorConfiguration()
   {
      MoflonSourceViewerConfiguration sourceViewConfig = (MoflonSourceViewerConfiguration) getSourceViewerConfiguration();
      return (MoflonTextEditorConfigIntern) sourceViewConfig.getMoflonTextEditorConfiguration();
   }

   /*
    * @see IWorkbenchPart#dispose()
    */
   public void dispose()
   {
      MoflonSourceViewerConfiguration sourceViewConfig = (MoflonSourceViewerConfiguration) getSourceViewerConfiguration();
      TextEditorBuilder.activeEditors.remove(sourceViewConfig.getMoflonTextEditorConfiguration());
      super.dispose();
   }

}
