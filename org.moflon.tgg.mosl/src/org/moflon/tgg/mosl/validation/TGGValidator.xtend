/*
 * generated by Xtext
 */
package org.moflon.tgg.mosl.validation

import java.util.ArrayList
import java.util.List
import org.eclipse.emf.common.util.BasicEList
import org.eclipse.xtext.validation.Check
import org.moflon.tgg.mosl.tgg.Adornment
import org.moflon.tgg.mosl.tgg.AttributeExpression
import org.moflon.tgg.mosl.tgg.ObjectVariablePattern
import org.moflon.tgg.mosl.tgg.Rule
import org.moflon.tgg.mosl.tgg.TggPackage
import org.moflon.tgg.mosl.tgg.AttributeVariable
import org.moflon.tgg.mosl.tgg.NamedElements
import java.util.Map
import org.eclipse.emf.ecore.EObject
import java.util.HashMap
import org.moflon.tgg.mosl.tgg.TripleGraphGrammarFile
import org.eclipse.xtext.EcoreUtil2

/**
 * This class contains custom validation rules. 
 *
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#validation
 */
class TGGValidator extends AbstractTGGValidator {

  public static val INVALID_ADORNMENT = 'invalidAdornmentValue'
  public static val INVALID_ATTRIBUTE_VARIABLE = 'invalidAttributeVariableAttribute'
  public static val NOT_UNIQUE_NAME = 'notUniqueName'
  public static val TYPE_IS_ABSTRACT = 'typeIsAbstract'
  public static val RULE_REFINEMENT_CREATES_A_CYCLE = 'RuleRefinementCreatesACycle'



	@Check
	def checkAdornmentValue(Adornment adornment){
		for (character : adornment.value.toCharArray) {
			if(character.compareTo('B')!=0 && character.compareTo('F')!=0){
				error("Adornment value may only consist of the capital letters B or F", TggPackage.Literals.ADORNMENT__VALUE, TGGValidator.INVALID_ADORNMENT);
			}
		}
	}
	
	@Check
	def checkAttributeExpression(AttributeExpression attrVar){
		var attrNames = new BasicEList()
		for (attr : attrVar.objectVar.type.EAllAttributes) {
			attrNames.add(attr)
		}
		if (!attrNames.contains(attrVar.attribute)) {
			error("EClass " + attrVar.objectVar.type.name + " does not contain EAttribute " + attrVar.attribute.name + ".", TggPackage.Literals.ATTRIBUTE_EXPRESSION__ATTRIBUTE, TGGValidator.INVALID_ATTRIBUTE_VARIABLE);
		}
	}
	
	@Check
	def checkAttributeVariable(AttributeVariable attrVar){
		var attrNames = new BasicEList()
		for (attr : attrVar.objectVar.type.EAllAttributes) {
			attrNames.add(attr.name)
		}
		if (!attrNames.contains(attrVar.attribute)) {
			error("EClass " + attrVar.objectVar.type.name + " does not contain EAttribute " + attrVar.attribute + ".", TggPackage.Literals.ATTRIBUTE_VARIABLE__ATTRIBUTE, TGGValidator.INVALID_ATTRIBUTE_VARIABLE);
		}
	}
	
	Map<String, Map<EObject, Map<Class<? extends EObject>, EObject>>> names = new HashMap<String, Map<EObject, Map<Class<? extends EObject>, EObject>>>();
	
	
	@Check
	def checkForUniqueNames(TripleGraphGrammarFile tggFile){
		names.clear();
		for(NamedElements ne : EcoreUtil2.getAllContentsOfType(tggFile, NamedElements)){
			checkForUniqueNames(ne);
		}
		
	}
	
	//FIXME Sascha: This does not work -- please test some more!
	def checkForUniqueNames(NamedElements ne){
//		if(names.containsKey(ne.name)){
//			var containers = names.get(ne.name);
//			if(containers.containsKey(ne.eContainer)){
//				var classes = containers.get(ne.eContainer);
//				
//				if(classes.containsKey(ne.class)){
//					var object = classes.get(ne.class);
//					if(!object.equals(ne)){
//						error("Names must be unique. The Name '" + ne.name + "' already used", ne,  TggPackage.Literals.NAMED_ELEMENTS__NAME, TGGValidator.NOT_UNIQUE_NAME);
//						error("Names must be unique. The Name '" + ne.name + "' already used", object,  TggPackage.Literals.NAMED_ELEMENTS__NAME, TGGValidator.NOT_UNIQUE_NAME);
//						classes.remove(ne.class);
//						containers.put(ne.eContainer, classes);
//						names.put(ne.name, containers)
//					}				
//				}
//				else{
//					classes.put(ne.class, ne);
//					containers.put(ne.eContainer, classes);
//					names.put(ne.name, containers);
//				}
//			}
//			else{
//				var classes = new HashMap<Class<? extends EObject>, EObject>();
//				classes.put(ne.class,ne);
//				containers.put(ne.eContainer, classes);
//				names.put(ne.name, containers);
//			}			
//		}
//		else{
//			var classes = new HashMap<Class<? extends EObject>, EObject>();
//			classes.put(ne.class,ne);			
//			var containers = new HashMap<EObject, Map<Class<? extends EObject>, EObject>>();
//			containers.put(ne.eContainer, classes);
//			names.put(ne.name, containers);
//		}
	}
	
	@Check
	def checkIfTheTryingToGenerateObjectVariableIsAbstract(ObjectVariablePattern objectVariablePattern){
		val type = objectVariablePattern.type
		val eContainer = objectVariablePattern.eContainer;		
		if(eContainer instanceof Rule){
		  var rule = eContainer as Rule;
		  var operator = objectVariablePattern.op;
		  var ruleIsAbstract = rule.abstractRule;
		  var typeIsAbstract = type.abstract;
		  var isGeneration = operator != null && operator.value != null && operator.value.equalsIgnoreCase("++ ");
		  var isAnError = !ruleIsAbstract && typeIsAbstract && isGeneration;
		  if(isAnError){
		  	error("The type of the object variable '" + objectVariablePattern.name + "' is abstract and the rule '" + Rule.name + "' is not abstract", TggPackage.Literals.OBJECT_VARIABLE_PATTERN__TYPE, TGGValidator.TYPE_IS_ABSTRACT);
		  }
		  
		}		
	} 
	
	def boolean findCycleInRule(Rule rule, List<Rule> visited){
		if(visited.contains(rule)){
			return true;
		}else {
			visited.add(rule);
			for(superType : rule.supertypes){
				if (findCycleInRule(superType, visited)){
					return true;
				}
			}
			visited.remove(rule)			
			return false;
		}
	}
	
	@Check
	def checkForCycleRefinments(Rule rule){
		var foundSuperTypes = new ArrayList<Rule>();
		if(findCycleInRule(rule, foundSuperTypes)){
			var refinementName = "<Placeholder>";
			if(foundSuperTypes.size() >= 2)
				refinementName = foundSuperTypes.get(1).name;
			error("The rule '" + rule.name + "' creates a cycle with the refinement '" + refinementName +"'", TggPackage.Literals.RULE__SUPERTYPES, TGGValidator.RULE_REFINEMENT_CREATES_A_CYCLE);
		}
	}
	
}
