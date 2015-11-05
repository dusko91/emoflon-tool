package org.moflon.tgg.mosl.resources;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.xtext.resource.SaveOptions;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.serializer.ISerializer;
import org.eclipse.xtext.serializer.impl.Serializer;
import org.moflon.tgg.mosl.TGGRuntimeModule;
import org.moflon.tgg.mosl.TGGStandaloneSetupGenerated;
import org.moflon.tgg.mosl.tgg.Import;
import org.moflon.tgg.mosl.tgg.Rule;
import org.moflon.tgg.mosl.tgg.Schema;
import org.moflon.tgg.mosl.tgg.TggPackage;
import org.moflon.tgg.mosl.tgg.TripleGraphGrammar;
import org.moflon.tgg.mosl.tgg.impl.TripleGraphGrammarImpl;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import SDMLanguage.SDMLanguagePackage;

public class TGGXMIHelper {
	
	public static boolean serializing = false;
	
	public static void serializeUseCase(String xmiFileName, String dslFileName) throws IOException {
		serializing = true;
		XtextResourceSet resourceSet = getResourceSet();
		XtextResource xtextResource = (XtextResource) resourceSet.createResource(getUri(dslFileName), "tgg");
		
		xtextResource.getContents().add(resourceSet.getResource(getUri(xmiFileName), true).getContents().get(0));
		EcoreUtil.resolveAll(xtextResource);
		
//		getImportedResources(xtextResource);
//		System.out.println(resourceSet);
		
//		String s = xtextResource.getSerializer().serialize(xtextResource.getContents().get(0));
//		System.out.println(s);
		
		SaveOptions.Builder options = SaveOptions.newBuilder();
		options.format();
		options.noValidation();
		xtextResource.save(options.getOptions().toOptionsMap());
		serializing = false;
	}

	public static void parseUseCase() throws IOException {
		parseTGGProject("src/org/moflon/tgg/mosl/", "XtextParsedInstances/");
		
		
	}
	
	public static void parseTGGProject(String dslDirectory, String xmiDirectory) throws IOException {
		File currentDir = new File(dslDirectory); 			// Schema directory
		File[] files = currentDir.listFiles(new FilenameFilter() {
	        @Override
	        public boolean accept(File dir, String name) {
	            return name.toLowerCase().endsWith(".tgg");
	        }
	    });
		if(files.length == 1){
			XtextResourceSet resourceSet = getResourceSet();
//			Resource xtextResource = resourceSet.createResource(getUri(dslFileName));

			//********************************************
			// 1. Process Schema.tgg
			//********************************************
//			String file = files[0].toString();
//			System.out.println("Files: " + file);
			
			Resource xtextResource = resourceSet.createResource(getUri(files[0].toString()));
			if (!(xtextResource instanceof XtextResource)) {
				throw new IllegalArgumentException("Resource " + xtextResource.toString() + " is not XTextResource");
			}
			xtextResource.load(null);
//			EcoreUtil.resolveAll(xtextResource);

//			EObject root = xtextResource.getContents().get(0);				
			TripleGraphGrammarImpl tggRoot = (TripleGraphGrammarImpl) xtextResource.getContents().get(0);
//			System.out.println("TGG: " + tggRoot);
			
			

			//********************************************
			// 2. Process all *Rule.tgg
			//********************************************
			currentDir = new File(currentDir.toString() + "/rules/"); 	// Rules directory
			System.out.println(currentDir);
			files = currentDir.listFiles(new FilenameFilter() {
		        @Override
		        public boolean accept(File dir, String name) {
		            return name.toLowerCase().endsWith(".tgg");
		        }
			});

			EList<Rule> rules = new BasicEList<Rule>((tggRoot).getRules());
			for (File file : files) {
				
				xtextResource = resourceSet.createResource(getUri(file.toString()));
				if (!(xtextResource instanceof XtextResource)) {
					throw new IllegalArgumentException("Resource " + xtextResource.toString() + " is not XTextResource");
				}
				xtextResource.load(null);
//				EcoreUtil.resolveAll(xtextResource);
				
				EObject rule = xtextResource.getContents().get(0).eContents().get(0);
				if(rule instanceof Rule) {
					rules.add((Rule) rule);
				}
				
//				xtextResource.
			}
			tggRoot.eSet(TggPackage.TRIPLE_GRAPH_GRAMMAR__RULES, rules);
			
			
			
			
//			if(tggSpec instanceof Rule) {
//				rules.add((Rule) tggSpec);
//				tggRoot.eSet(TggPackage.TRIPLE_GRAPH_GRAMMAR__RULES, rules);
//			}
//			
//			
//			EObject root = xtextResource.getContents().get(0);
			
			Resource xmiResource = resourceSet.createResource(getUri(xmiDirectory + "TGG.xmi"));
			xmiResource.getContents().add(tggRoot);
			xmiResource.save(null);
			
			
			
		}
		else {
			System.out.println("TGG-Project must contain exactly one Schema in " + dslDirectory + ".");
			System.out.println("Files: " + files.length);
//			System.out.println("Files: " + files[0]);
//			System.out.println("Files: " + files[1]);
//			System.out.println("Files: " + files[2]);
			
		}
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
	
	private static URI getImportUri(String importedNamespace) {
	    String path = (new File("")).getAbsolutePath();
	    path = path.substring(0, path.lastIndexOf('\\')+1);
	    importedNamespace = importedNamespace.substring(0, importedNamespace.lastIndexOf(".*"));
	    String fullPath = path + importedNamespace + "\\model\\" + importedNamespace + ".ecore";
//	    URI importURI = URI.createFileURI(fullPath);	    
	    if((new File(fullPath)).exists()){
	    	return URI.createFileURI(fullPath);
	    } else {
	    	return null;
	    }
	}

	public static EList<URI> getSchemaImportURIs(Schema schema) {
		EList<URI> importURIs = new BasicEList<URI>();
		for (Import nsImport : schema.getImports()) {
			URI importURI = getImportUri(nsImport.getImportedNamespace());
			if(importURI != null){
				importURIs.add(importURI);
			}
		}
		return importURIs;
	}
	
	private static XtextResourceSet getResourceSet() {
		Map<String, Object> m = Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap();
		m.remove("*");
		m.put("xmi", new XMIResourceFactoryImpl());
		m.put("ecore", new EcoreResourceFactoryImpl());
		SDMLanguagePackage packageInstance = SDMLanguagePackage.eINSTANCE;
		TGGStandaloneSetupGenerated useCaseStandalone = new TGGStandaloneSetupGenerated();
		XtextResourceSet resourceSet = useCaseStandalone.createInjectorAndDoEMFRegistration().getInstance(XtextResourceSet.class);
		// Add adapter for reverse navigation along unidirectional links
		ECrossReferenceAdapter adapter = ECrossReferenceAdapter.getCrossReferenceAdapter(resourceSet);
		if (adapter == null) {
			resourceSet.eAdapters().add(new ECrossReferenceAdapter());
		}
		return resourceSet;
	}
	
	private static void getImportedResources(XtextResource xtextResource) {
		if(xtextResource.getContents().get(0) instanceof TripleGraphGrammar){
			TripleGraphGrammar tggRoot = (TripleGraphGrammar) xtextResource.getContents().get(0);
//			SDMLanguagePackage packageInstance = SDMLanguagePackage.eINSTANCE;
			for (Import nsImport : tggRoot.getSchema().getImports()) {
				URI importURI = getImportUri(nsImport.getImportedNamespace());
				if(importURI != null){
					xtextResource.getResourceSet().getResource(importURI, true);
				}
			}
		}
	}
}
