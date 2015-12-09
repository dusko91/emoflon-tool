package org.moflon.tgg.mosl.scoping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.xtext.resource.SaveOptions;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.moflon.tgg.mosl.tgg.Adornment;
import org.moflon.tgg.mosl.tgg.AttrCondDef;
import org.moflon.tgg.mosl.tgg.AttrCondDefLibrary;
import org.moflon.tgg.mosl.tgg.Param;
import org.moflon.tgg.mosl.tgg.TggFactory;
import org.moflon.tgg.mosl.tgg.TggPackage;
import org.moflon.tgg.mosl.tgg.TripleGraphGrammarFile;

public class AttrCondDefLibraryProvider {
	
	private static String[] names = {"eq", "addPrefix", "addSuffix", "concat", "setDefaultString", 
			"setDefaultNumber", "stringToDouble", "stringToInt", "multiply", "divide", 
			"add", "sub", "max", "smallerOrEqual"};

	private static EDataType[][] paramTypes = {
			{EcorePackage.Literals.EJAVA_OBJECT, EcorePackage.Literals.EJAVA_OBJECT},						// 0. eq
			{EcorePackage.Literals.ESTRING , EcorePackage.Literals.ESTRING, EcorePackage.Literals.ESTRING},	// 1. addPrefix
			{EcorePackage.Literals.ESTRING , EcorePackage.Literals.ESTRING, EcorePackage.Literals.ESTRING},	// 2. addSuffix
			{EcorePackage.Literals.ESTRING , EcorePackage.Literals.ESTRING, EcorePackage.Literals.ESTRING, EcorePackage.Literals.ESTRING},	// 3. concat
			{EcorePackage.Literals.ESTRING , EcorePackage.Literals.ESTRING},	// 4. setDefaultString
			
			{EcorePackage.Literals.EDOUBLE , EcorePackage.Literals.EDOUBLE},	// 5. setDefaultNumber
			{EcorePackage.Literals.ESTRING , EcorePackage.Literals.EDOUBLE},	// 6. stringToDouble
			{EcorePackage.Literals.ESTRING , EcorePackage.Literals.EINT},		// 7. stringToInt
			{EcorePackage.Literals.EDOUBLE , EcorePackage.Literals.EDOUBLE, EcorePackage.Literals.EDOUBLE},	// 8. multiply
			{EcorePackage.Literals.EDOUBLE , EcorePackage.Literals.EDOUBLE, EcorePackage.Literals.EDOUBLE},	// 9. divide
			
			{EcorePackage.Literals.EDOUBLE , EcorePackage.Literals.EDOUBLE, EcorePackage.Literals.EDOUBLE},	// 10. add
			{EcorePackage.Literals.EDOUBLE , EcorePackage.Literals.EDOUBLE, EcorePackage.Literals.EDOUBLE},	// 11. sub
			{EcorePackage.Literals.EDOUBLE , EcorePackage.Literals.EDOUBLE, EcorePackage.Literals.EDOUBLE},	// 12. max
			{EcorePackage.Literals.EDOUBLE , EcorePackage.Literals.EDOUBLE}		// 13. smallerOrEqual
	};
	
	private static String[][] syncAdornments = {
			{"BB", "BF", "FB"},					// 0. eq
			{"BBB", "BBF", "BFB", "FBB"},		// 1. addPrefix
			{"BBB", "BBF", "BFB", "FBB"},		// 2. addSuffix
			{"BBBB", "BBBF", "BBFB", "BFFB", "BFBB"},		// 3. concat
			{"BB", "FB"},						// 4. setDefaultString

			{"BB", "FB"},						// 5. setDefaultNumber
			{"BB", "BF", "FB"},					// 6. stringToDouble
			{"BB", "BF", "FB"},					// 7. stringToInt
			{"BBB", "BBF", "BFB", "FBB"},		// 8. multiply
			{"BBB", "BBF", "BFB", "FBB"},		// 9. divide
			
			{"BBB", "BBF", "BFB", "FBB"},		// 10. add
			{"BBB", "BBF", "BFB", "FBB"},		// 11. sub
			{"BBB", "BBF", "BFB", "FBB"},		// 12. max
			{"BB", "BF", "FB"}					// 13. smallerOrEqual
	};

	private static String[][] genAdornments = {
			{"BB", "BF", "FB", "FF"},							// 0. eq
			{"BBB", "BBF", "BFB", "FBB", "BFF", "FBF"},			// 1. addPrefix
			{"BBB", "BBF", "BFB", "FBB", "BFF", "FFF", "FBF"},	// 2. addSuffix
			{"BBBB", "BBBF", "BBFB", "BFFB", "BFBB", "BFFF", "BFBF", "BBFF"},	// 3. concat
			{"BB", "FB", "FF"},									// 4. setDefaultString

			{"BB", "FB", "FF"},									// 5. setDefaultNumber
			{"BB", "BF", "FB", "FF"},							// 6. stringToDouble
			{"BB", "BF", "FB", "FF"},							// 7. stringToInt
			{"BBB", "BBF", "BFB", "FBB"},						// 8. multiply
			{"BBB", "BBF", "BFB", "FBB"},						// 9. divide
			
			{"BBB", "BBF", "BFB", "FBB", "FFB", "FBF", "BFF"},			// 10. add
			{"BBB", "BBF", "BFB", "FBB", "FFB", "BFF", "FBF", "FFF"},	// 11. sub
			{"BBB", "BBF", "BFB", "FBB"},		// 12. max
			{"BB", "BF", "FB", "FF"}			// 13. smallerOrEqual
	};
	
			
	protected static EList<AttrCondDef> attrCondDefs;
	
	public static EList<AttrCondDef> getAttrCondDefs() {
		if(attrCondDefs == null){
			TggFactory tggFactory = TggPackage.eINSTANCE.getTggFactory();
			
			attrCondDefs = new BasicEList<AttrCondDef>();
			
			for (int i = 0; i < names.length; i++) {
				attrCondDefs.add(createAttrCondDef(names[i], paramTypes[i], syncAdornments[i], genAdornments[i]));
			}
		}
			
		return attrCondDefs;
	}


	public static void syncAttrCondDefLibrary(XtextResourceSet resourceSet, String projectPath) throws IOException {
		TggFactory tggFactory = TggPackage.eINSTANCE.getTggFactory();
		
		AttrCondDefLibrary attrCondDefLib = tggFactory.createAttrCondDefLibrary();
		attrCondDefLib.setName("AttrCondDefLibrary");
		attrCondDefLib.getAttributeCondDefs().addAll(getAttrCondDefs());
		
		TripleGraphGrammarFile tgg = tggFactory.createTripleGraphGrammarFile();
		tgg.setLibrary(attrCondDefLib);
		
//		XtextResourceSet resourceSet = new XtextResourceSet();
		URI uri = URI.createPlatformResourceURI(projectPath + "/src/org/moflon/tgg/mosl/csp/lib/AttrCondDefLibrary.tgg", true);
		XtextResource resource = (XtextResource) resourceSet.createResource(uri);
		resource.getContents().add(tgg);
		resource.save(null);
		
//		uri = URI.createPlatformPluginURI(projectPath + "/lib/AttrCondDefLibrary.xmi", true);
//		XMIResource xmiResource = (XMIResource) resourceSet.createResource(uri);
//		xmiResource.getContents().add(tgg);
//		xmiResource.save(null);
	}

	private static AttrCondDef createAttrCondDef(String name, EDataType[] paramTypes, String[] syncAdornments, String[] genAdornments) {
		TggFactory tggFactory = TggPackage.eINSTANCE.getTggFactory();
		AttrCondDef attrCondDef = tggFactory.createAttrCondDef();
		attrCondDef.setName(name);
		
		Param param;
		for (int i = 0; i < paramTypes.length; i++) {
			param = tggFactory.createParam();
			param.setIndex(i);
			param.setType(paramTypes[i]);
			attrCondDef.getParams().add(param);
		}
		
		Adornment adornment;
		for (String adornmentValue : syncAdornments) {
			adornment = tggFactory.createAdornment();
			adornment.setValue(adornmentValue);
			attrCondDef.getAllowedSyncAdornments().add(adornment);
		}
		for (String adornmentValue : genAdornments) {
			adornment = tggFactory.createAdornment();
			adornment.setValue(adornmentValue);
			attrCondDef.getAllowedGenAdornments().add(adornment);
		}
		return attrCondDef;
	}
	
	
}
