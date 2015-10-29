/*
 * generated by Xtext
 */
package org.moflon.tgg.mosl.serializer;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.serializer.acceptor.ISemanticSequenceAcceptor;
import org.eclipse.xtext.serializer.acceptor.SequenceFeeder;
import org.eclipse.xtext.serializer.diagnostic.ISemanticSequencerDiagnosticProvider;
import org.eclipse.xtext.serializer.diagnostic.ISerializationDiagnostic.Acceptor;
import org.eclipse.xtext.serializer.sequencer.AbstractDelegatingSemanticSequencer;
import org.eclipse.xtext.serializer.sequencer.GenericSequencer;
import org.eclipse.xtext.serializer.sequencer.ISemanticNodeProvider.INodesForEObjectProvider;
import org.eclipse.xtext.serializer.sequencer.ISemanticSequencer;
import org.eclipse.xtext.serializer.sequencer.ITransientValueService;
import org.eclipse.xtext.serializer.sequencer.ITransientValueService.ValueTransient;
import org.moflon.tgg.mosl.services.TGGGrammarAccess;
import org.moflon.tgg.mosl.tgg.AttrCond;
import org.moflon.tgg.mosl.tgg.AttrCondDef;
import org.moflon.tgg.mosl.tgg.CorrTypeDef;
import org.moflon.tgg.mosl.tgg.CorrVariablePattern;
import org.moflon.tgg.mosl.tgg.Import;
import org.moflon.tgg.mosl.tgg.LinkVariablePattern;
import org.moflon.tgg.mosl.tgg.ObjectVariablePattern;
import org.moflon.tgg.mosl.tgg.Operator;
import org.moflon.tgg.mosl.tgg.Param;
import org.moflon.tgg.mosl.tgg.ParamValue;
import org.moflon.tgg.mosl.tgg.Rule;
import org.moflon.tgg.mosl.tgg.Schema;
import org.moflon.tgg.mosl.tgg.TggPackage;
import org.moflon.tgg.mosl.tgg.TripleGraphGrammar;
import org.moflon.tgg.mosl.tgg.TypeExtension;

@SuppressWarnings("all")
public class TGGSemanticSequencer extends AbstractDelegatingSemanticSequencer {

	@Inject
	private TGGGrammarAccess grammarAccess;
	
	@Override
	public void createSequence(EObject context, EObject semanticObject) {
		if(semanticObject.eClass().getEPackage() == TggPackage.eINSTANCE) switch(semanticObject.eClass().getClassifierID()) {
			case TggPackage.ATTR_COND:
				sequence_AttrCond(context, (AttrCond) semanticObject); 
				return; 
			case TggPackage.ATTR_COND_DEF:
				sequence_AttrCondDef(context, (AttrCondDef) semanticObject); 
				return; 
			case TggPackage.CORR_TYPE_DEF:
				sequence_CorrTypeDef(context, (CorrTypeDef) semanticObject); 
				return; 
			case TggPackage.CORR_VARIABLE_PATTERN:
				sequence_CorrVariablePattern(context, (CorrVariablePattern) semanticObject); 
				return; 
			case TggPackage.IMPORT:
				sequence_Import(context, (Import) semanticObject); 
				return; 
			case TggPackage.LINK_VARIABLE_PATTERN:
				sequence_LinkVariablePattern(context, (LinkVariablePattern) semanticObject); 
				return; 
			case TggPackage.OBJECT_VARIABLE_PATTERN:
				sequence_ObjectVariablePattern(context, (ObjectVariablePattern) semanticObject); 
				return; 
			case TggPackage.OPERATOR:
				sequence_Operator(context, (Operator) semanticObject); 
				return; 
			case TggPackage.PARAM:
				sequence_Param(context, (Param) semanticObject); 
				return; 
			case TggPackage.PARAM_VALUE:
				sequence_ParamValue(context, (ParamValue) semanticObject); 
				return; 
			case TggPackage.RULE:
				sequence_Rule(context, (Rule) semanticObject); 
				return; 
			case TggPackage.SCHEMA:
				sequence_Schema(context, (Schema) semanticObject); 
				return; 
			case TggPackage.TRIPLE_GRAPH_GRAMMAR:
				sequence_TripleGraphGrammar(context, (TripleGraphGrammar) semanticObject); 
				return; 
			case TggPackage.TYPE_EXTENSION:
				sequence_TypeExtension(context, (TypeExtension) semanticObject); 
				return; 
			}
		if (errorAcceptor != null) errorAcceptor.accept(diagnosticProvider.createInvalidContextOrTypeDiagnostic(semanticObject, context));
	}
	
	/**
	 * Constraint:
	 *     (
	 *         name=ID 
	 *         (params+=Param params+=Param*)? 
	 *         allowedSyncAdornments+=Adornment 
	 *         allowedAdornments+=Adornment* 
	 *         (allowedGenAdornments+=Adornment allowedAdornments+=Adornment*)?
	 *     )
	 */
	protected void sequence_AttrCondDef(EObject context, AttrCondDef semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (name=[AttrCondDef|ID] (values+=ParamValue values+=ParamValue*)?)
	 */
	protected void sequence_AttrCond(EObject context, AttrCond semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (name=ID source=[EClass|QualifiedName] target=[EClass|QualifiedName])
	 */
	protected void sequence_CorrTypeDef(EObject context, CorrTypeDef semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, TggPackage.Literals.CORR_TYPE__NAME) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, TggPackage.Literals.CORR_TYPE__NAME));
			if(transientValues.isValueTransient(semanticObject, TggPackage.Literals.CORR_TYPE_DEF__SOURCE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, TggPackage.Literals.CORR_TYPE_DEF__SOURCE));
			if(transientValues.isValueTransient(semanticObject, TggPackage.Literals.CORR_TYPE_DEF__TARGET) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, TggPackage.Literals.CORR_TYPE_DEF__TARGET));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getCorrTypeDefAccess().getNameIDTerminalRuleCall_0_0(), semanticObject.getName());
		feeder.accept(grammarAccess.getCorrTypeDefAccess().getSourceEClassQualifiedNameParserRuleCall_3_0_1(), semanticObject.getSource());
		feeder.accept(grammarAccess.getCorrTypeDefAccess().getTargetEClassQualifiedNameParserRuleCall_5_0_1(), semanticObject.getTarget());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (op=Operator? name=ID type=[CorrType|QualifiedName] source=[ObjectVariablePattern|ID] target=[ObjectVariablePattern|ID])
	 */
	protected void sequence_CorrVariablePattern(EObject context, CorrVariablePattern semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     importedNamespace=QualifiedNameWithWildcard
	 */
	protected void sequence_Import(EObject context, Import semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, TggPackage.Literals.IMPORT__IMPORTED_NAMESPACE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, TggPackage.Literals.IMPORT__IMPORTED_NAMESPACE));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getImportAccess().getImportedNamespaceQualifiedNameWithWildcardParserRuleCall_1_0(), semanticObject.getImportedNamespace());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (op=Operator? type=[EReference|ID] target=[ObjectVariablePattern|ID])
	 */
	protected void sequence_LinkVariablePattern(EObject context, LinkVariablePattern semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (op=Operator? name=ID type=[EClass|QualifiedName] linkVariablePatterns+=LinkVariablePattern*)
	 */
	protected void sequence_ObjectVariablePattern(EObject context, ObjectVariablePattern semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (value='++ ' | value='! ')
	 */
	protected void sequence_Operator(EObject context, Operator semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (objectVar=[ObjectVariablePattern|ID] type=[EAttribute|ID])
	 */
	protected void sequence_ParamValue(EObject context, ParamValue semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, TggPackage.Literals.PARAM_VALUE__OBJECT_VAR) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, TggPackage.Literals.PARAM_VALUE__OBJECT_VAR));
			if(transientValues.isValueTransient(semanticObject, TggPackage.Literals.PARAM_VALUE__TYPE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, TggPackage.Literals.PARAM_VALUE__TYPE));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getParamValueAccess().getObjectVarObjectVariablePatternIDTerminalRuleCall_0_0_1(), semanticObject.getObjectVar());
		feeder.accept(grammarAccess.getParamValueAccess().getTypeEAttributeIDTerminalRuleCall_2_0_1(), semanticObject.getType());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (name=ID type=[EDataType|QualifiedName])
	 */
	protected void sequence_Param(EObject context, Param semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, TggPackage.Literals.PARAM__NAME) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, TggPackage.Literals.PARAM__NAME));
			if(transientValues.isValueTransient(semanticObject, TggPackage.Literals.PARAM__TYPE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, TggPackage.Literals.PARAM__TYPE));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getParamAccess().getNameIDTerminalRuleCall_0_0(), semanticObject.getName());
		feeder.accept(grammarAccess.getParamAccess().getTypeEDataTypeQualifiedNameParserRuleCall_2_0_1(), semanticObject.getType());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (
	 *         imports+=Import* 
	 *         abstract?='abstract'? 
	 *         name=ID 
	 *         supertypes+=[Rule|ID]? 
	 *         schema=[Schema|QualifiedName] 
	 *         sourcePatterns+=ObjectVariablePattern* 
	 *         targetPatterns+=ObjectVariablePattern* 
	 *         correspondencePatterns+=CorrVariablePattern* 
	 *         attrConditions+=AttrCond*
	 *     )
	 */
	protected void sequence_Rule(EObject context, Rule semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (
	 *         imports+=Import* 
	 *         name=ID 
	 *         sourceTypes+=[EPackage|QualifiedName]* 
	 *         targetTypes+=[EPackage|QualifiedName]* 
	 *         correspondenceTypes+=CorrType* 
	 *         attributeCondDefs+=AttrCondDef*
	 *     )
	 */
	protected void sequence_Schema(EObject context, Schema semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (schema=Schema? rules+=Rule?)
	 */
	protected void sequence_TripleGraphGrammar(EObject context, TripleGraphGrammar semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (name=ID super=[CorrType|ID])
	 */
	protected void sequence_TypeExtension(EObject context, TypeExtension semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, TggPackage.Literals.CORR_TYPE__NAME) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, TggPackage.Literals.CORR_TYPE__NAME));
			if(transientValues.isValueTransient(semanticObject, TggPackage.Literals.TYPE_EXTENSION__SUPER) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, TggPackage.Literals.TYPE_EXTENSION__SUPER));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getTypeExtensionAccess().getNameIDTerminalRuleCall_0_0(), semanticObject.getName());
		feeder.accept(grammarAccess.getTypeExtensionAccess().getSuperCorrTypeIDTerminalRuleCall_2_0_1(), semanticObject.getSuper());
		feeder.finish();
	}
}
