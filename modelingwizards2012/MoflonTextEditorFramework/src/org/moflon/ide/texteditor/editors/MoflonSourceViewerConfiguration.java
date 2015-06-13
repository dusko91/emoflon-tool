package org.moflon.ide.texteditor.editors;

import java.util.Map;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.graphics.RGB;
import org.moflon.ide.texteditor.wizards.TextEditorConfigurationClassLoader;

public class MoflonSourceViewerConfiguration extends SourceViewerConfiguration {

	private Object loadedMoflonTextEditorConfiguration;

	public MoflonSourceViewerConfiguration(IProject selectedProject) {
	   this(selectedProject, new TextEditorConfigurationClassLoader().loadClass(selectedProject));
	}

	public MoflonSourceViewerConfiguration(IProject selectedProject, MoflonTextEditorConfiguration moflonTextEditorConfiguration)
   {
	   loadedMoflonTextEditorConfiguration = moflonTextEditorConfiguration;
      configureTextEditorBuilder(selectedProject);
   }

   private void configureTextEditorBuilder(IProject selectedProject) {
		M2TSynchronizer m2tSynch = (M2TSynchronizer) loadedMoflonTextEditorConfiguration;
		TextEditorBuilder.synchronizer = m2tSynch;
		
		//TODO: we should find a better way to pass the project object to the text editor configuration.
		((MoflonTextEditorConfiguration)loadedMoflonTextEditorConfiguration).setProject(selectedProject);
		
		this.associateTextEditorBuilderWhenNecessary(selectedProject);
	}

	@Override
	public IPresentationReconciler getPresentationReconciler(
			ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		ITokenScanner tokenScanner = (ITokenScanner) loadedMoflonTextEditorConfiguration;
		MultilineDamagerRepairer dr = new MultilineDamagerRepairer(tokenScanner);
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		return reconciler;
	}

	@Override
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		ContentAssistant assistant = new ContentAssistant();
		assistant
				.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));

		IContentAssistProcessor processor = (IContentAssistProcessor) loadedMoflonTextEditorConfiguration;
		assistant.setContentAssistProcessor(processor,
				IDocument.DEFAULT_CONTENT_TYPE);

		assistant
				.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_ABOVE);
		assistant
				.setInformationControlCreator(getInformationControlCreator(sourceViewer));

		return assistant;
	}

	private void associateTextEditorBuilderWhenNecessary(IProject project) {
		final String BUILDER_ID = "org.moflon.ide.texteditor.editors.TextEditorBuilder";
		IProjectDescription desc = null;
		try {
			desc = project.getDescription();
		} catch (CoreException e1) {
			e1.printStackTrace();
		}
		ICommand[] commands = desc.getBuildSpec();
		boolean found = false;

		for (int i = 0; i < commands.length; ++i) {
			if (commands[i].getBuilderName().equals(BUILDER_ID)) {
				found = true;
				break;
			}
		}
		if (!found) {
			// add builder to project
			ICommand command = desc.newCommand();
			command.setBuilderName(BUILDER_ID);
			ICommand[] newCommands = new ICommand[commands.length + 1];

			// Add it before other builders.
			System.arraycopy(commands, 0, newCommands, 1, commands.length);
			newCommands[0] = command;
			desc.setBuildSpec(newCommands);
			try {
				project.setDescription(desc, null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

}
