package org.moflon.ide.texteditor.helpers;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import Moca.Problem;

public class MarkerHelper {
	IResource resource;
	final static String MARKER_TYPE = "org.moflon.ide.TextEditor.MoflonEditorProblem";

	/**
	 * returns the number of Markers for the given resource.  
	 * all marker types are counted.
	 * @param resource
	 * @return
	 */
	public static int getNumberOfMarkers(IResource resource){
		IMarker[] markers = null;
		try {
			markers = resource.findMarkers(null, true, IResource.DEPTH_INFINITE);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return markers.length;
	}

	/**
	 * removes all markers from given resource
	 * @param resource
	 */
	public static void removeMarkers(IResource resource) {
		try {
			resource.deleteMarkers(MARKER_TYPE, true, IResource.DEPTH_INFINITE);
		} catch (CoreException e) {
			e.printStackTrace();
		}      

	}

	
	private static IDocument getDocument(ITextEditor textEditor){
		IDocumentProvider provider = textEditor.getDocumentProvider();
		IDocument document = provider.getDocument(textEditor.getEditorInput());
		return document;
	}

	public static void reportError(IResource resource, Exception exception) {
		IMarker m;
		try {
			m = resource.createMarker(MARKER_TYPE);
			m.setAttribute(IMarker.MESSAGE, exception.getMessage());
			m.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
			m.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * creates an marker for the given error and binds it to resource
	 * @param resource
	 * @param error
	 */
	public static void reportError(IResource resource, Problem problem, ITextEditor editor) {
		if (resource.getType() == IResource.FILE)
		{
			IMarker m;
			int line = problem.getLine();
			if(line == 0)
				line++;
			int posStart = 0;
			int posEnd = 0;

			try {
				IDocument document = getDocument(editor);
				if(document != null){
					posStart = problem.getCharacterPositionStart() + document.getLineOffset(line-1) ;
					posEnd   = problem.getCharacterPositionEnd()   + document.getLineOffset(line-1) ;
					if(posStart<0){
						posStart=0;
						posEnd=1;
					}
				}
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
			
			try {
				m = resource.createMarker(MARKER_TYPE);
				m.setAttribute(IMarker.LINE_NUMBER, line);
				m.setAttribute(IMarker.CHAR_START, posStart);
				m.setAttribute(IMarker.CHAR_END,   posEnd);
				m.setAttribute(IMarker.MESSAGE, (String)problem.getMessage());
				m.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
				switch (problem.getType()) {
				case INFO:
					m.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO);
					break;
				case ERROR:
					m.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
					break;
				case WARNING:
					m.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
					break;
				default:
					break;
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}


}
