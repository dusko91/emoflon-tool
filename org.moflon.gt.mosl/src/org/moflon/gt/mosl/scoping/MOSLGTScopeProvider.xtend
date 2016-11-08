/*
 * generated by Xtext 2.10.0
 */
package org.moflon.gt.mosl.scoping

import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference
import org.moflon.gt.mosl.moslgt.EClassDef
import org.moflon.codegen.eclipse.CodeGeneratorPlugin
import org.moflon.gt.mosl.moslgt.GraphTransformationFile
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.EClass
import org.moflon.ide.mosl.core.scoping.ScopeProviderHelper
import org.moflon.gt.mosl.moslgt.MethodDec
import org.eclipse.emf.ecore.EClassifier
import org.moflon.ide.mosl.core.exceptions.CannotFindScopeException
import org.apache.log4j.Logger
import org.moflon.gt.mosl.moslgt.ObjectVariableDefinition

/**
 * This class contains custom scoping description.
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#scoping
 * on how and when to use it.
 */
class MOSLGTScopeProvider extends AbstractMOSLGTScopeProvider {
	private ScopeProviderHelper<EPackage> helper = new ScopeProviderHelper();
	private Logger log = Logger.getLogger(MOSLGTScopeProvider.getClass());
	
	override getScope(EObject context, EReference reference) {
	try{
		if(searchForEClass(context,reference)){
			return getScopeByType(context, EClass)
		}
		if(searchForEClassifier(context,reference)){
			return getScopeByType(context, EClassifier)
		}
	}catch (CannotFindScopeException e){
		log.debug("Cannot find Scope",e)
	}
		super.getScope(context, reference);
	}
	
	def getScopeByType(EObject context, Class<? extends EObject> type)throws CannotFindScopeException{
		val set = helper.resourceSet
		CodeGeneratorPlugin.createPluginToResourceMapping(set);		
		var gtf = getGraphTransformationFile(context)
		var uris = gtf.imports.map[importValue | URI.createURI(importValue.name)];
		return helper.createScope(uris, EPackage, type);		 
	}
	
	def GraphTransformationFile getGraphTransformationFile(EObject context){
		if(context == null)
			return null
		else if(context instanceof GraphTransformationFile)
			return context
		else
			return getGraphTransformationFile(context.eContainer)
	}
	
	def boolean searchForEClass(EObject context, EReference reference){
		return context instanceof EClassDef 
	}
	
	def boolean searchForEClassifier(EObject context, EReference reference){
		return context instanceof MethodDec || context instanceof ObjectVariableDefinition
	}
	
}
