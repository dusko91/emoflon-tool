package org.moflon.tgg.mosl.resources;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.xtext.resource.SaveOptions;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.moflon.tgg.mosl.TGGStandaloneSetupGenerated;

public class TGGXMIHelper {
	public static void serializeUseCase(String xmiFileName, String dslFileName) throws IOException {
		XtextResourceSet resourceSet = getResourceSet();
		XtextResource xtextResource = (XtextResource) resourceSet.createResource(getUri(dslFileName), "tgg");
		xtextResource.getContents().add(resourceSet.getResource(getUri(xmiFileName), true).getContents().get(0));
		SaveOptions.Builder options = SaveOptions.newBuilder();
		options.format();
		options.noValidation();
		xtextResource.save(options.getOptions().toOptionsMap());
	}
	
	public static void parseUseCase(String dslFileName, String xmiFileName)
			throws IOException {
		XtextResourceSet resourceSet = getResourceSet();
		Resource xtextResource = resourceSet.createResource(getUri(dslFileName));
		if (!(xtextResource instanceof XtextResource)) {
			throw new IllegalArgumentException("Resource " + xtextResource.toString() + " is not XTextResource");
		}
		xtextResource.load(null);
		EObject root = xtextResource.getContents().get(0);
		Resource xmiResource = resourceSet.createResource(getUri(xmiFileName));
		xmiResource.getContents().add(root);
		xmiResource.save(null);
	}
	
	private static URI getUri(String fileName) {
		File filePath = new File(fileName);
		return URI.createFileURI(filePath.getAbsolutePath());
	}
	
	private static XtextResourceSet getResourceSet() {
		Map<String, Object> m = Resource.Factory.Registry.INSTANCE
				.getExtensionToFactoryMap();
		m.remove("*");
		m.put("xmi", new XMIResourceFactoryImpl());
		TGGStandaloneSetupGenerated useCaseStandalone = new TGGStandaloneSetupGenerated();
		XtextResourceSet resourceSet = useCaseStandalone.createInjectorAndDoEMFRegistration().getInstance(XtextResourceSet.class);
		// Add adapter for reverse navigation along unidirectional links
		ECrossReferenceAdapter adapter = ECrossReferenceAdapter.getCrossReferenceAdapter(resourceSet);
		if (adapter == null) {
			resourceSet.eAdapters().add(new ECrossReferenceAdapter());
		}
		return resourceSet;
	}
}
