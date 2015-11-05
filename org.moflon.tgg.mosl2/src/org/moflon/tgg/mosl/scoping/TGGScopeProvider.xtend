package org.moflon.tgg.mosl.scoping

import java.util.List
import org.eclipse.emf.ecore.EClassifier
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.EReference
import org.eclipse.xtext.EcoreUtil2
import org.eclipse.xtext.scoping.Scopes
import org.eclipse.xtext.scoping.impl.AbstractDeclarativeScopeProvider
import org.moflon.tgg.mosl.tgg.CorrTypeDef
import org.moflon.tgg.mosl.tgg.LinkVariablePattern
import org.moflon.tgg.mosl.tgg.ObjectVariablePattern
import org.moflon.tgg.mosl.tgg.ParamValue
import org.moflon.tgg.mosl.tgg.Rule
import org.moflon.tgg.mosl.tgg.Schema
import org.moflon.tgg.mosl.tgg.TggPackage
import org.eclipse.xtext.scoping.IScope
import org.eclipse.xtext.scoping.impl.FilteringScope
import org.eclipse.emf.ecore.EClass
import org.moflon.tgg.mosl.tgg.CorrVariablePattern
import org.moflon.tgg.mosl.tgg.TypeExtension
import org.eclipse.xtext.resource.IEObjectDescription
import org.moflon.tgg.mosl.tgg.impl.CorrTypeDefImpl
import org.moflon.tgg.mosl.tgg.impl.TypeExtensionImpl
import org.moflon.tgg.mosl.tgg.impl.CorrTypeImpl
import org.moflon.tgg.mosl.tgg.CorrType
import org.eclipse.xtext.serializer.sequencer.ISemanticNodeProvider
import org.eclipse.xtext.scoping.impl.ImportScope
import org.eclipse.emf.common.util.URI
import org.moflon.tgg.mosl.tgg.Param
import org.moflon.tgg.mosl.resources.TGGXMIHelper
import org.eclipse.emf.common.util.BasicEList
import org.eclipse.emf.common.util.EList
import org.eclipse.emf.ecore.EcorePackage
import java.util.Collection
import org.eclipse.emf.ecore.EDataType
import org.eclipse.emf.ecore.util.EcoreUtil
import org.moflon.tgg.mosl.tgg.impl.TripleGraphGrammarImpl
import org.moflon.tgg.mosl.tgg.TripleGraphGrammar

/**
 * This class contains custom scoping description.
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#scoping
 * on how and when to use it.
 * 
 */
class TGGScopeProvider extends AbstractDeclarativeScopeProvider {

	override getScope(EObject context, EReference reference) {
		
		System.out.println("*******************************************************");
		System.out.println("EObject context: " + context.toString);
		System.out.println("EReference reference: " + reference.toString);
		
		/* Scopes in Rule */
		
		if (is_type_of_lv(context, reference)){			
			System.out.println("type_of_lv_must_be_a_reference_of_enclosing_ov(context)");
			System.out.println("*******************************************************");
			return type_of_lv_must_be_a_reference_of_enclosing_ov(context)
		} 
			
		
		if(is_target_of_lv(context, reference)){			
			System.out.println("target_of_lv_must_be_compatible_with_its_type(context)");
			System.out.println("*******************************************************");
			return target_of_lv_must_be_compatible_with_its_type(context)			
		} 
			
		
		if (is_attr_cond(context, reference)){			
			System.out.println("attr_in_cond_must_be_an_attr_of_the_ref_ov(context)");
			System.out.println("*******************************************************");
			return attr_in_cond_must_be_an_attr_of_the_ref_ov(context)
		} 
			
		
		if(is_type_of_ov(context, reference)){			
			System.out.println("type_of_ov_must_be_from_correct_domain(context)");
			System.out.println("*******************************************************");
			return type_of_ov_must_be_from_correct_domain(context)
		} 
			
		
		if(is_src_of_corr_ov(context, reference)){			
			System.out.println("src_of_corr_ov_must_be_in_src_domain(context)");
			System.out.println("*******************************************************");
			return src_of_corr_ov_must_be_in_src_domain(context)
//			return src_of_corr_ov_must_be_in_src_domain2(context)
		} 
			
		
		if(is_trg_of_corr_ov(context, reference)){			
			System.out.println("trg_of_corr_ov_must_be_in_trg_domain(context)");
			System.out.println("*******************************************************");
			return trg_of_corr_ov_must_be_in_trg_domain(context)
		} 
			
		
		/* Scopes in Schema */
		
		if (is_src_of_corr_type(context, reference)){			
			System.out.println("src_of_corr_type_must_be_a_src_type(context)");
			System.out.println("*******************************************************");
			return src_of_corr_type_must_be_a_src_type(context)
		} 
		if (is_trg_of_corr_type(context, reference)){			
			System.out.println("trg_of_corr_type_must_be_a_trg_type(context)");
			System.out.println("*******************************************************");
			return trg_of_corr_type_must_be_a_trg_type(context)
		} 
		
		if (is_src_types_of_schema(context, reference) && TGGXMIHelper.serializing){
			return types_of_schema_must_be_imported(context)
		}
		if (is_trg_types_of_schema(context, reference) && TGGXMIHelper.serializing){
			return types_of_schema_must_be_imported(context)
//			System.out.println("trg_types_of_schema_must_be_imported(context)");
//			System.out.println("*******************************************************");
//			return trg_types_of_schema_must_be_imported(context, reference)
		}

		if (is_type_of_param(context, reference)){
			return type_of_param_must_be_edatatype(context)
////			System.out.println(edata);
//			System.out.println("trg_types_of_schema_must_be_imported(context)");
//			System.out.println("*******************************************************");
//			return type_of_param_must_be_edatatype(context, reference)
		}
		if(is_type_of_corr_ov(context, reference) && TGGXMIHelper.serializing){
//			System.out.println("!!! REACHED !!!")
			return type_of_corr_ov_must_be_a_corr_type(context)
		}

		super.getScope(context, reference)
	}
	
	def src_of_corr_ov_must_be_in_src_domain2(EObject context) {
		var rule = context.eContainer as Rule
		var src_domain = rule.sourcePatterns
		var corr_src_type = ((context as CorrVariablePattern).type as CorrTypeDef)
		System.out.println("src_domain: " + src_domain)
		var list = new BasicEList<ObjectVariablePattern>()
		for(ObjectVariablePattern src : src_domain) {
			System.out.println("src: " + src)
			System.out.println("corr_src_type: " + corr_src_type)
			if(src.type == corr_src_type) {
//				System.out.println("!!! REACHED !!!")
				list.add(src)
//				src_domain.remove(src)
			}
		}
		System.out.println("list: " + list)
		var scope = Scopes.scopeFor(list)
		System.out.println(scope)
		return scope
	}
	
	def type_of_corr_ov_must_be_a_corr_type(EObject context) {	
		System.out.println("eResourceSet: " + context.eResource.resourceSet)
		var rule = context.eContainer as Rule
		System.out.println("rule: " + rule)
		var tgg = rule.eContainer as TripleGraphGrammar
		System.out.println("tgg: " + tgg)
//		System.out.println("tgg: " + tgg.eContents)
		var schema = tgg.schema as Schema
		System.out.println("schema: " + schema)
		var corrTypes = schema.correspondenceTypes		
		System.out.println("corrTypes: " + corrTypes)
		var scope = Scopes.scopeFor(schema.correspondenceTypes)
		System.out.println("scope: " + scope)
		return scope
	}
	
	def types_of_schema_must_be_imported(EObject context) {
		var schema = context as Schema
			var packages = new BasicEList<EPackage>()
			var importURIs = TGGXMIHelper.getSchemaImportURIs(schema)
			
			for (URI uri : importURIs) {
				var importResource = schema.eResource().getResourceSet().getResource(uri, true)
				var xmiRes = importResource.getContents()
				for (EObject eObject : xmiRes) {
					if (eObject instanceof EPackage) {
						packages.add(eObject as EPackage)
					}
				}
			}
//			return Scopes.scopeFor(packages)
			
			var scope = Scopes.scopeFor(packages)
			System.out.println(scope)
			return scope
			
	}
	
	def type_of_param_must_be_edatatype(EObject object) {
		var eClassifiers = EcorePackage.eINSTANCE.getEClassifiers()
		var edata = (EcoreUtil.getObjectsByType(eClassifiers, EcorePackage.Literals.EDATA_TYPE) as Object) as Collection<EDataType>
		return Scopes.scopeFor(edata)
	}
	
	def is_type_of_param(EObject context, EReference reference) {
		context instanceof Param && reference == TggPackage.Literals.PARAM__TYPE
	}
	
	def is_type_of_corr_ov(EObject context, EReference reference) {
		context instanceof CorrVariablePattern && reference == TggPackage.Literals.CORR_VARIABLE_PATTERN__TYPE
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
	
	def is_src_types_of_schema(EObject context, EReference reference) {
		context instanceof Schema && reference == TggPackage.Literals.SCHEMA__SOURCE_TYPES
	}
	def is_trg_types_of_schema(EObject context, EReference reference) {
		context instanceof Schema && reference == TggPackage.Literals.SCHEMA__TARGET_TYPES
	}

	def trg_of_corr_ov_must_be_in_trg_domain(EObject context) {
		val typeDef = determineTypeDef(context)
		var rule = context.eContainer as Rule
		var IScope allCandidates = Scopes.scopeFor(rule.targetPatterns)		
		return new FilteringScope(allCandidates, [c | is_equal_or_super_type_of_ov(typeDef.target, c)])
	}
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
	
//	def determineTypeDef(EObject context){
//		var corrOv = context as CorrVariablePattern
//		var type = corrOv.type
////		System.out.println("*******************************************************");
////		System.out.println("CorrVariablePattern.type.eClass: " + type.eClass.toString);
////		System.out.println("CorrVariablePattern.eClass: " + corrOv.eClass.toString);
////		System.out.println("*******************************************************");
////		
////		if(type instanceof CorrType){
////			System.out.println("*******************************************************");
////			System.out.println("CorrVariablePattern.type: instanceof CorrType");
////			System.out.println("*******************************************************");
////		}
////		
////		if(type instanceof CorrTypeImpl){
////			System.out.println("*******************************************************");
////			System.out.println("CorrVariablePattern.type: instanceof CorrTypeImpl");
////			System.out.println("*******************************************************");
////		}
////		
////		if(type instanceof CorrTypeDef){
////			System.out.println("*******************************************************");
////			System.out.println("CorrVariablePattern.type: instanceof CorrTypeDef");
////			System.out.println("*******************************************************");
////		}
////		
////		if(type instanceof TypeExtension){
////			System.out.println("*******************************************************");
////			System.out.println("CorrVariablePattern.type: instanceof TypeExtension");
////			System.out.println("*******************************************************");
////		}
////		
////		if(type instanceof CorrTypeDefImpl){
////			System.out.println("*******************************************************");
////			System.out.println("CorrVariablePattern.type: instanceof CorrTypeDefImpl");
////			System.out.println("*******************************************************");
////		}
////		
////		if(type instanceof TypeExtensionImpl){
////			System.out.println("*******************************************************");
////			System.out.println("CorrVariablePattern.type: instanceof TypeExtensionImpl");
////			System.out.println("*******************************************************");
////		}
//			
//		
//		if(!(type instanceof CorrTypeDef))
//			type = (type as TypeExtension).super as CorrTypeDef	
////			type = type as CorrTypeDef
//		
//		return type as CorrTypeDef
//	}
	
	def src_of_corr_ov_must_be_in_src_domain(EObject context) {
		val typeDef = determineTypeDef(context)
		var rule = context.eContainer as Rule
		var IScope allCandidates = Scopes.scopeFor(rule.sourcePatterns)		
//		System.out.println(allCandidates)
		var scope = new FilteringScope(allCandidates, [c | is_equal_or_super_type_of_ov(typeDef.source, c)])
//		System.out.println(allCandidates)
//		System.out.println(scope.allElements.get(0).EObjectOrProxy)
//		System.out.println(scope.allElements.get(0).EObjectOrProxy.eAdapters)
		System.out.println(scope.allElements)
//		scope.allElements.get(0).EObjectOrProxy.eAdapters
		return scope
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
//		var temp = handleCorrTypeDef(context, [Schema s|s.targetTypes])
//		System.out.println(temp.allElements.size);
//		return temp
		return handleCorrTypeDef(context, [Schema s|s.targetTypes])
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
