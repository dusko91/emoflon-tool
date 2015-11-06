package org.moflon.tgg.mosl.builder;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.xtext.resource.XtextResourceSet;

public class MOSLTGGConversionHelper extends AbstractHandler {

	public static void generateTGGModel(IResource resource) {
		
		try {
			if(resource.getProjectRelativePath().equals(resource.getProject().getProjectRelativePath().append("/src/org/moflon/tgg/mosl"))){
				IFolder moslFolder = IFolder.class.cast(resource);
				XtextResourceSet resourceSet = new XtextResourceSet();

				IFile schemaFile = moslFolder.getFile("Schema.tgg");
				Resource schema = resourceSet.createResource(URI.createPlatformResourceURI(schemaFile.getFullPath().toString(), true));
				schema.load(null);
				
				//TODO Load all rules
				
				//TODO Add everything to form a single container
				
				//TODO Invoke TGG forward transformation to produce TGG model
				
				//TODO Persist TGG model in /model folder of current project according to naming convention
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);

		if (selection instanceof IStructuredSelection) {
			Object file = ((IStructuredSelection) selection).getFirstElement();
			if(file instanceof IFile){
				IFile tggFile = (IFile)file;
				
				//TODO Invoke TGG backward transformation to get the mosl tgg model
				
				//TODO Persist mosl tgg model as set of xtext files
			}
		}

		return null;
	}
}
