package org.moflon.tgg.mosl.builder;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResourceSet;

public class MOSLTGGConversionHelper {

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
}
