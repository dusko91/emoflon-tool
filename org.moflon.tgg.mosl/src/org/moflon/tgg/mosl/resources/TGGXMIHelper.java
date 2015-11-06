package org.moflon.tgg.mosl.resources;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Map;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.xtext.resource.SaveOptions;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.moflon.tgg.mosl.TGGStandaloneSetupGenerated;
import org.moflon.tgg.mosl.tgg.Import;
import org.moflon.tgg.mosl.tgg.Rule;
import org.moflon.tgg.mosl.tgg.Schema;
import org.moflon.tgg.mosl.tgg.TggPackage;
import org.moflon.tgg.mosl.tgg.TripleGraphGrammar;
import org.moflon.tgg.mosl.tgg.impl.TripleGraphGrammarImpl;

import SDMLanguage.SDMLanguagePackage;

public class TGGXMIHelper {
	
	public static boolean serializing = false;
	
	public static void serializeTGG(String xmiFileName, String dslFileName) throws IOException {
		serializing = true;
		XtextResourceSet resourceSet = getResourceSet();
		XtextResource xtextResource = (XtextResource) resourceSet.createResource(getUri(dslFileName), "tgg");
		
		xtextResource.getContents().add(resourceSet.getResource(getUri(xmiFileName), true).getContents().get(0));
		EcoreUtil.resolveAll(xtextResource);
				
		SaveOptions.Builder options = SaveOptions.newBuilder();
		options.format();
		options.noValidation();
		xtextResource.save(options.getOptions().toOptionsMap());
		serializing = false;
	}

	public static void parseAllTGGSpecfiles(String xmiFileName) throws IOException {
		parseTGGProject("src/org/moflon/tgg/mosl/", xmiFileName);
		
		
	}
	
	public static void parseTGGProject(String dslDirectory, String xmiFileName) throws IOException {
		File currentDir = new File(dslDirectory); 			// Schema directory
		File[] files = currentDir.listFiles(new FilenameFilter() {
	        @Override
	        public boolean accept(File dir, String name) {
	            return name.toLowerCase().endsWith(".tgg");
	        }
	    });
		if(files.length == 1){
			XtextResourceSet resourceSet = getResourceSet();
			//********************************************
			// 1. Process Schema.tgg
			//********************************************
//			Resource xtextResource = resourceSet.createResource(getUri(files[0].toString()));
			Resource xtextResource = resourceSet.getResource(getUri(files[0].toString()), true);
			if (!(xtextResource instanceof XtextResource)) {
				throw new IllegalArgumentException("Resource " + xtextResource.toString() + " is not XTextResource");
			}
			xtextResource.load(null);
			EcoreUtil.resolveAll(xtextResource);

			TripleGraphGrammarImpl tggRoot = (TripleGraphGrammarImpl) xtextResource.getContents().get(0);			
			
			//********************************************
			// 2. Process all *Rule.tgg
			//********************************************
			currentDir = new File(currentDir.toString() + "/rules/"); 	// Rules directory
			files = currentDir.listFiles(new FilenameFilter() {
		        @Override
		        public boolean accept(File dir, String name) {
		            return name.toLowerCase().endsWith(".tgg");
		        }
			});

			EList<Rule> rules = new BasicEList<Rule>((tggRoot).getRules());
			for (File file : files) {
//				xtextResource = resourceSet.createResource(getUri(file.toString()));
				xtextResource = resourceSet.getResource(getUri(file.toString()), true);
				if (!(xtextResource instanceof XtextResource)) {
					throw new IllegalArgumentException("Resource " + xtextResource.toString() + " is not XTextResource");
				}
				xtextResource.load(null);
				EcoreUtil.resolveAll(xtextResource);
				
				EObject rule = xtextResource.getContents().get(0).eContents().get(0);
				if(rule instanceof Rule) {
					rules.add((Rule) rule);
				}
			}
			tggRoot.eSet(TggPackage.TRIPLE_GRAPH_GRAMMAR__RULES, rules);
			
			
			Resource xmiResource = resourceSet.createResource(getUri(xmiFileName));
			xmiResource.getContents().add(tggRoot);
			xmiResource.save(null);
		}
		else {
			System.out.println("TGG-Project must contain exactly one Schema in " + dslDirectory + ".");
		}
	}


	public static void parseTGGSpecfile(String dslFileName, String xmiFileName)
			throws IOException {
		XtextResourceSet resourceSet = getResourceSet();
//		Resource xtextResource = resourceSet.createResource(getUri(dslFileName));
		Resource xtextResource = resourceSet.getResource(getUri(dslFileName), true);
		if (!(xtextResource instanceof XtextResource)) {
			throw new IllegalArgumentException("Resource " + xtextResource.toString() + " is not XTextResource");
		}
		xtextResource.load(null);
		EcoreUtil.resolveAll(resourceSet);
		
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
