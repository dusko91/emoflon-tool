package org.moflon.tgg.mosl.scoping

import java.util.Collection
import java.util.List
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EClassifier
import org.eclipse.emf.ecore.EDataType
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.EReference
import org.eclipse.emf.ecore.EcorePackage
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipse.xtext.EcoreUtil2
import org.eclipse.xtext.resource.IEObjectDescription
import org.eclipse.xtext.scoping.IScope
import org.eclipse.xtext.scoping.Scopes
import org.eclipse.xtext.scoping.impl.AbstractDeclarativeScopeProvider
import org.eclipse.xtext.scoping.impl.FilteringScope
import org.moflon.tgg.mosl.tgg.CorrTypeDef
import org.moflon.tgg.mosl.tgg.CorrVariablePattern
import org.moflon.tgg.mosl.tgg.LinkVariablePattern
import org.moflon.tgg.mosl.tgg.ObjectVariablePattern
import org.moflon.tgg.mosl.tgg.Param
import org.moflon.tgg.mosl.tgg.ParamValue
import org.moflon.tgg.mosl.tgg.Rule
import org.moflon.tgg.mosl.tgg.Schema
import org.moflon.tgg.mosl.tgg.TggPackage
import org.moflon.tgg.mosl.tgg.TypeExtension
import org.moflon.tgg.mosl.tgg.TripleGraphGrammarFile

/**
 * This class contains custom scoping description.
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#scoping
 * on how and when to use it.
 * 
 */
class TGGScopeProvider extends AbstractDeclarativeScopeProvider {

	override getScope(EObject context, EReference reference) {
		
		/* Scopes in Rule */
		
		if (is_type_of_lv(context, reference)) 
			return type_of_lv_must_be_a_reference_of_enclosing_ov(context)
		
		if(is_target_of_lv(context, reference))
			return target_of_lv_must_be_compatible_with_its_type(context)			
		
		if (is_attr_cond(context, reference))
			return attr_in_cond_must_be_an_attr_of_the_ref_ov(context)
		
		if(is_type_of_ov(context, reference))
			return type_of_ov_must_be_from_correct_domain(context)
		
		if(is_src_of_corr_ov(context, reference))
			return src_of_corr_ov_must_be_in_src_domain(context)
		
		if(is_trg_of_corr_ov(context, reference))
			return trg_of_corr_ov_must_be_in_trg_domain(context)
		
		/* Scopes in Schema */
		
		if (is_src_of_corr_type(context, reference))
			return src_of_corr_type_must_be_a_src_type(context)
		
		if (is_trg_of_corr_type(context, reference))
			return trg_of_corr_type_must_be_a_trg_type(context)
			
		if (is_type_of_param(context, reference))
			return type_of_param_must_be_edatatype(context)
			
			
//		if (is_src_types_of_schema(context, reference) && TGGXMIHelper.serializing)
//			return types_of_schema_must_be_imported(context)
//		
//		if (is_trg_types_of_schema(context, reference) && TGGXMIHelper.serializing)
//			return types_of_schema_must_be_imported(context)
			
			
//		if(is_type_of_corr_ov(context, reference) && TGGXMIHelper.serializing)
//			return type_of_corr_ov_must_be_a_corr_type(context)

		super.getScope(context, reference)
	}
	
	def type_of_corr_ov_must_be_a_corr_type(EObject context) {
		var rule = context.eContainer as Rule
		var tgg = rule.eContainer as TripleGraphGrammarFile
		var schema = tgg.schema as Schema
		return Scopes.scopeFor(schema.correspondenceTypes)
	}
	
//	def types_of_schema_must_be_imported(EObject context) {
//		var schema = context as Schema
//		var packages = new BasicEList<EPackage>()
//		var importURIs = TGGXMIHelper.getSchemaImportURIs(schema)
//		
//		for (URI uri : importURIs) {
//			var importResource = schema.eResource().getResourceSet().getResource(uri, true)
//			var xmiRes = importResource.getContents()
//			for (EObject eObject : xmiRes) {
//				if (eObject instanceof EPackage) {
//					packages.add(eObject as EPackage)
//				}
//			}
//		}
//		return Scopes.scopeFor(packages)
//	}
	
	def type_of_param_must_be_edatatype(EObject object) {
		var eClassifiers = EcorePackage.eINSTANCE.getEClassifiers()
		var edata = (EcoreUtil.getObjectsByType(eClassifiers, EcorePackage.Literals.EDATA_TYPE) as Object) as Collection<EDataType>
		return Scopes.scopeFor(edata)
	}
	
	def is_type_of_corr_ov(EObject context, EReference reference) {
		context instanceof CorrVariablePattern && reference == TggPackage.Literals.CORR_VARIABLE_PATTERN__TYPE
	}
	
	def is_type_of_param(EObject context, EReference reference) {
		context instanceof Param && reference == TggPackage.Literals.PARAM__TYPE
	}
	
	def is_trg_types_of_schema(EObject context, EReference reference) {
		context instanceof Schema && reference == TggPackage.Literals.SCHEMA__TARGET_TYPES
	}
	
	def is_src_types_of_schema(EObject context, EReference reference) {
		context instanceof Schema && reference == TggPackage.Literals.SCHEMA__SOURCE_TYPES
	}

	def is_trg_of_corr_ov(EObject context, EReference reference) {
		context instanceof CorrVariablePattern && reference == TggPackage.Literals.CORR_VARIABLE_PATTERN__TARGET
	}
	
	def is_src_of_corr_ov(EObject context, EReference reference) {
		context instanceof CorrVariablePattern && reference == TggPackage.Literals.CORR_VARIABLE_PATTERN__SOURCE
	}
	
	def is_equal_or_super_type_of_ov(EClass sup, IEObjectDescription desc){
		var sub = (desc.EObjectOrProxy as ObjectVariablePattern).type
		sub.equals(sup) || sub.EAllSuperTypes.contains(sup)
	}

	def is_type_of_ov(EObject context, EReference reference) {
		context instanceof ObjectVariablePattern && reference == TggPackage.Literals.OBJECT_VARIABLE_PATTERN__TYPE
	}

	def is_attr_cond(EObject context, EReference reference) {
		context instanceof ParamValue && reference == TggPackage.Literals.PARAM_VALUE__TYPE
	}

	def is_type_of_lv(EObject context, EReference reference) {
		context instanceof LinkVariablePattern && reference == TggPackage.Literals.LINK_VARIABLE_PATTERN__TYPE
	}
	
	def is_target_of_lv(EObject context, EReference reference) {
		context instanceof LinkVariablePattern && reference == TggPackage.Literals.LINK_VARIABLE_PATTERN__TARGET
	}
	
	def is_trg_of_corr_type(EObject context, EReference reference) {
		context instanceof CorrTypeDef && reference == TggPackage.Literals.CORR_TYPE_DEF__TARGET
	}

	def is_src_of_corr_type(EObject context, EReference reference) {
		context instanceof CorrTypeDef && reference == TggPackage.Literals.CORR_TYPE_DEF__SOURCE
	}

	def trg_of_corr_ov_must_be_in_trg_domain(EObject context) {
		val typeDef = determineTypeDef(context)
		var rule = context.eContainer as Rule
		var IScope allCandidates = Scopes.scopeFor(rule.targetPatterns)		
		return new FilteringScope(allCandidates, [c | is_equal_or_super_type_of_ov(typeDef.target, c)])
	}
	
//	def determineTypeDef(EObject context){
//		var corrOv = context as CorrVariablePattern
//		var type = corrOv.type
//		if(!(type instanceof CorrTypeDef))
//			type = (type as TypeExtension).super as CorrTypeDef	
//		
//		return type as CorrTypeDef
//	}
	
	def determineTypeDef(EObject context){
		var corrOv = context as CorrVariablePattern
		var type = corrOv.type
		if(type instanceof CorrTypeDef)
			return type as CorrTypeDef
		else if(type instanceof TypeExtension)
			return determineTypeDefFromExtension(type) as CorrTypeDef
		else
			throw new IllegalStateException("This should never be the case!")
	}
	
	def determineTypeDefFromExtension(TypeExtension typeExtension) {
		if(typeExtension.super instanceof CorrTypeDef)
			return typeExtension.super as CorrTypeDef
		else if(typeExtension.super instanceof TypeExtension)
			return determineTypeDefFromExtension(typeExtension.super as TypeExtension)
		else
			throw new IllegalStateException("This should never be the case!") 
	}
	
	def src_of_corr_ov_must_be_in_src_domain(EObject context) {
		val typeDef = determineTypeDef(context)
		var rule = context.eContainer as Rule
		var IScope allCandidates = Scopes.scopeFor(rule.sourcePatterns)		
		return new FilteringScope(allCandidates, [c | is_equal_or_super_type_of_ov(typeDef.source, c)])
	}
	
	def target_of_lv_must_be_compatible_with_its_type(EObject context) {
		val lvPattern = context as LinkVariablePattern
		var ovPattern = lvPattern.eContainer as ObjectVariablePattern
		var rule = ovPattern.eContainer as Rule
		
		// Must be in same domain as source ov
		var IScope allCandidates = null;
		if(rule.sourcePatterns.contains(ovPattern))
			allCandidates = Scopes.scopeFor(rule.sourcePatterns)
		else {
			allCandidates = Scopes.scopeFor(rule.targetPatterns)		
		}
		
		// Must be compatible to lv
		return new FilteringScope(allCandidates, [c | is_equal_or_super_type_of_ov(lvPattern.type.EType as EClass, c)])
	}
	
	
	def type_of_ov_must_be_from_correct_domain(EObject context) {
		var ov = context as ObjectVariablePattern
		var rule = ov.eContainer as Rule
		var schema = rule.schema
		
		if(rule.sourcePatterns.contains(ov)){
			// typeOfOv must be in source domain
			allTypes(schema.sourceTypes, schema)
		}else{
			// typeOfOv must be in target domain
			allTypes(schema.targetTypes, schema)
		}
	}
	

	def src_of_corr_type_must_be_a_src_type(EObject context) {
		handleCorrTypeDef(context, [Schema s | s.sourceTypes])
	}

	def trg_of_corr_type_must_be_a_trg_type(EObject context) {
		handleCorrTypeDef(context, [Schema s|s.targetTypes])
	}

	def handleCorrTypeDef(EObject context, (Schema)=>List<EPackage> types) {
		var corrTypeDef = context as CorrTypeDef
		var schema = corrTypeDef.eContainer as Schema
		var sources = types.apply(schema)
		return allTypes(sources, schema)
	}
	
	def allTypes(List<EPackage> types, Schema schema) {
		Scopes.scopeFor(types.map[EPackage p|EcoreUtil2.getAllContentsOfType(p, EClassifier)].flatten)
	}

	def attr_in_cond_must_be_an_attr_of_the_ref_ov(EObject context) {
		var paramVal = context as ParamValue
		var ovPattern = paramVal.objectVar as ObjectVariablePattern
		return Scopes.scopeFor(ovPattern.type.EAllAttributes)
	}

	def type_of_lv_must_be_a_reference_of_enclosing_ov(EObject context) {
		var lvPattern = context as LinkVariablePattern
		var ovPattern = lvPattern.eContainer as ObjectVariablePattern
		return Scopes.scopeFor(ovPattern.type.EAllReferences)
	}
}