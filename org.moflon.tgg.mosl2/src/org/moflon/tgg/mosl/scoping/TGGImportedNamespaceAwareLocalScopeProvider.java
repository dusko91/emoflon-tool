package org.moflon.tgg.mosl.scoping;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.ui.PlatformUI;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.Scopes;
import org.eclipse.xtext.scoping.impl.ImportNormalizer;
import org.eclipse.xtext.scoping.impl.ImportedNamespaceAwareLocalScopeProvider;
import org.eclipse.xtext.ui.containers.WorkspaceProjectsStateHelper;
import org.eclipse.xtext.ui.resource.IResourceSetProvider;
import org.eclipse.xtext.util.Strings;
import org.moflon.tgg.mosl.resources.TGGXMIHelper;
import org.moflon.tgg.mosl.tgg.Import;
import org.moflon.tgg.mosl.tgg.Param;
import org.moflon.tgg.mosl.tgg.Schema;
import org.moflon.tgg.mosl.tgg.TggPackage;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

import SDMLanguage.SDMLanguagePackage;

public class TGGImportedNamespaceAwareLocalScopeProvider extends ImportedNamespaceAwareLocalScopeProvider {
	
//	@Inject
//	IResourceSetProvider resourceSetProvider;
	
	
//	@Override
//	protected List<ImportNormalizer> getImplicitImports(boolean ignoreCase) {
//		
//		System.out.println("REACHED: getImplicitImports");
//		
//		List<ImportNormalizer> temp = new ArrayList<ImportNormalizer>();
//	    temp.add(new ImportNormalizer(
//	    QualifiedName.create("builtin","types","namespace"),
//	    true, ignoreCase));
//	    return temp;
//	}
	
	
//	@Inject
//	private IQualifiedNameConverter nameConverter;
//	
//	@Override
//	protected List<ImportNormalizer> internalGetImportedNamespaceResolvers(EObject context, boolean ignoreCase) {
//	    if (!(context instanceof Schema))
//	      return Collections.emptyList();
//	    Schema model = (Schema)context;
//	    List<ImportNormalizer> importedNamespaceResolvers = Lists.newArrayList();
//	    
//	    for (Import anImport : model.getImports()) {
//	    	// We CANNOT do this here: 
//	    	//    EPackage ePackage = anImport.getImportedEPackage();
//	        //    QualifiedName packageQN = getQualifiedNameProvider().getFullyQualifiedName(ePackage);
//	        //    String packageName = nameConverter.toString(packageQN);
//	    	// because that would lead to a ""Cyclic resolution of lazy links".. so, instead:
//	    	List<INode> nodes = NodeModelUtils.findNodesForFeature(anImport, TggPackage.Literals.IMPORT.IMPORT__IMPORTED_EPACKAGE);
//	        INode node = nodes.get(0);
//	        final String packageName = NodeModelUtils.getTokenText(node);
//			importedNamespaceResolvers.add(createImportedNamespaceResolver(packageName, ignoreCase));
//		}
//	    return importedNamespaceResolvers;
//	}
//	  
//	/**
//	 * We override this because in our grammar, the trailing .* isn't part of the rule, so that we can use a reference.  
//	 */
//	@Override
//	protected ImportNormalizer createImportedNamespaceResolver(final String namespace, boolean ignoreCase) {
//		if (Strings.isEmpty(namespace))
//			return null;
//		QualifiedName importedNamespace = nameConverter.toQualifiedName(namespace);
//		if (importedNamespace == null || importedNamespace.getSegmentCount() < 1) {
//			return null;
//		}
//		// We know our language has a wildcard, but it's in the Grammar (not in Rule)
//		// instead of being part of the namespace String, so this can be simplified, 
//		// and should not use skipLast(1)
//		return doCreateImportNormalizer(importedNamespace, true, ignoreCase);
//	}
//}
	
	
	
	
	
	
	
	@Override
	public IScope getScope(final EObject context, final EReference reference) {
//		
//		if(context instanceof Param && reference == TggPackage.Literals.PARAM__TYPE){
//			Param param = (Param) context;
//			EList<EClassifier> eClassifiers = EcorePackage.eINSTANCE.getEClassifiers();
//			Collection<EDataType> edata = (Collection<EDataType>)(Object)EcoreUtil.getObjectsByType(eClassifiers, EcorePackage.Literals.EDATA_TYPE);
////			System.out.println(edata);
//			return Scopes.scopeFor(edata);
//		}
//		
//		
//		if(context instanceof Schema && reference == TggPackage.Literals.SCHEMA__TARGET_TYPES){
//			EList<EPackage> packages = new BasicEList<EPackage>();
//			Schema schema = ((Schema) context);
//			EList<URI> importURIs = TGGXMIHelper.getSchemaImportURIs(schema);
//			for (URI uri : importURIs) {
//				Resource importResource = schema.eResource().getResourceSet().getResource(uri, true);	
//				EList<EObject> xmiRes = importResource.getContents();		
//				for (EObject eObject : xmiRes) {
//					if (eObject instanceof EPackage) {
//						packages.add((EPackage) eObject);
//					}
//				}
//			}
//			return Scopes.scopeFor(packages);
//		}
//		
//		if(context instanceof Schema && reference == TggPackage.Literals.SCHEMA__SOURCE_TYPES){
//			EList<EPackage> packages = new BasicEList<EPackage>();
//			Schema schema = ((Schema) context);
////			EList<Resource> schRes = schema.eResource().getResourceSet().getResources();
//			// Version 2
//			EList<URI> importURIs = TGGXMIHelper.getSchemaImportURIs(schema);
//			for (URI uri : importURIs) {
//				Resource importResource = schema.eResource().getResourceSet().getResource(uri, true);	
//				EList<EObject> xmiRes = importResource.getContents();		
//				for (EObject eObject : xmiRes) {
//					if (eObject instanceof EPackage) {
//						packages.add((EPackage) eObject);
//					}
//				}
//			}
//			
//			// Version 1
////			for (Resource resource : schRes) {
////				if (resource instanceof XMIResourceImpl) {
////					URI uri = resource.getURI();
////					Resource importResource = schema.eResource().getResourceSet().getResource(uri, true);
//////					EList<EObject> xmiRes = resource.getContents();		    		
////					EList<EObject> xmiRes = importResource.getContents();		
////					for (EObject eObject : xmiRes) {
////						if (eObject instanceof EPackage) {
////							packages.add((EPackage) eObject);
////						}
////					}
////				}
////			}
//
////			IScope ret = Scopes.scopeFor(packages);
////			System.out.println("Pack: " + packages);
////			System.out.println("IScope: " + ret);
//			return Scopes.scopeFor(packages);
//		}
		
		return super.getScope(context, reference);
	}

}
