/*
 * generated by Xtext 2.10.0
 */
package org.moflon.gt.mosl.scoping

import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference
import org.moflon.gt.mosl.moslgt.EClassDef
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.moflon.codegen.eclipse.CodeGeneratorPlugin
import org.moflon.gt.mosl.moslgt.GraphTransformationFile
import org.eclipse.emf.common.util.URI
import org.eclipse.xtext.EcoreUtil2
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.EClass
import org.eclipse.xtext.scoping.Scopes

/**
 * This class contains custom scoping description.
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#scoping
 * on how and when to use it.
 */
class MOSLGTScopeProvider extends AbstractMOSLGTScopeProvider {
	override getScope(EObject context, EReference reference) {
		if(isClassDef(context,reference)){
			return potentialClassDef(context as EClassDef)
		}
		super.getScope(context, reference);
	}
	
	def boolean isClassDef(EObject context, EReference refernce){
		return context instanceof EClassDef 
	}
	
	def potentialClassDef(EClassDef classDef){
		val set = new ResourceSetImpl()
		CodeGeneratorPlugin.createPluginToResourceMapping(set);
		var gtf = classDef.eContainer as GraphTransformationFile
		var ePackage = gtf.imports.map[u | set.getResource(URI.createURI(u.name), true).contents.get(0) as EPackage].get(0)
		var candidates = EcoreUtil2.getAllContentsOfType(ePackage, EClass)
		
		
		return Scopes.scopeFor(candidates)
	}
}
