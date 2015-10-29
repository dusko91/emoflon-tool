/*
 * generated by Xtext
 */
package org.moflon.tgg.mosl.serializer;

import com.google.inject.Inject;
import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.IGrammarAccess;
import org.eclipse.xtext.RuleCall;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.serializer.analysis.GrammarAlias.AbstractElementAlias;
import org.eclipse.xtext.serializer.analysis.GrammarAlias.GroupAlias;
import org.eclipse.xtext.serializer.analysis.GrammarAlias.TokenAlias;
import org.eclipse.xtext.serializer.analysis.ISyntacticSequencerPDAProvider.ISynNavigable;
import org.eclipse.xtext.serializer.analysis.ISyntacticSequencerPDAProvider.ISynTransition;
import org.eclipse.xtext.serializer.sequencer.AbstractSyntacticSequencer;
import org.moflon.tgg.mosl.services.TGGGrammarAccess;

@SuppressWarnings("all")
public class TGGSyntacticSequencer extends AbstractSyntacticSequencer {

	protected TGGGrammarAccess grammarAccess;
	protected AbstractElementAlias match_ObjectVariablePattern___LeftCurlyBracketKeyword_4_0_RightCurlyBracketKeyword_4_2__q;
	protected AbstractElementAlias match_Rule___AttributeConditionsKeyword_10_0_LeftCurlyBracketKeyword_10_1_RightCurlyBracketKeyword_10_3__q;
	protected AbstractElementAlias match_Rule___CorrespondenceKeyword_9_0_LeftCurlyBracketKeyword_9_1_RightCurlyBracketKeyword_9_3__q;
	protected AbstractElementAlias match_Rule___SourceKeyword_7_0_LeftCurlyBracketKeyword_7_1_RightCurlyBracketKeyword_7_3__q;
	protected AbstractElementAlias match_Rule___TargetKeyword_8_0_LeftCurlyBracketKeyword_8_1_RightCurlyBracketKeyword_8_3__q;
	protected AbstractElementAlias match_Schema___AttributeConditionsKeyword_6_0_LeftCurlyBracketKeyword_6_1_RightCurlyBracketKeyword_6_3__q;
	protected AbstractElementAlias match_Schema___CorrespondenceKeyword_5_0_LeftCurlyBracketKeyword_5_1_RightCurlyBracketKeyword_5_3__q;
	protected AbstractElementAlias match_Schema___SourceKeyword_3_0_LeftCurlyBracketKeyword_3_1_RightCurlyBracketKeyword_3_3__q;
	protected AbstractElementAlias match_Schema___TargetKeyword_4_0_LeftCurlyBracketKeyword_4_1_RightCurlyBracketKeyword_4_3__q;
	
	@Inject
	protected void init(IGrammarAccess access) {
		grammarAccess = (TGGGrammarAccess) access;
		match_ObjectVariablePattern___LeftCurlyBracketKeyword_4_0_RightCurlyBracketKeyword_4_2__q = new GroupAlias(false, true, new TokenAlias(false, false, grammarAccess.getObjectVariablePatternAccess().getLeftCurlyBracketKeyword_4_0()), new TokenAlias(false, false, grammarAccess.getObjectVariablePatternAccess().getRightCurlyBracketKeyword_4_2()));
		match_Rule___AttributeConditionsKeyword_10_0_LeftCurlyBracketKeyword_10_1_RightCurlyBracketKeyword_10_3__q = new GroupAlias(false, true, new TokenAlias(false, false, grammarAccess.getRuleAccess().getAttributeConditionsKeyword_10_0()), new TokenAlias(false, false, grammarAccess.getRuleAccess().getLeftCurlyBracketKeyword_10_1()), new TokenAlias(false, false, grammarAccess.getRuleAccess().getRightCurlyBracketKeyword_10_3()));
		match_Rule___CorrespondenceKeyword_9_0_LeftCurlyBracketKeyword_9_1_RightCurlyBracketKeyword_9_3__q = new GroupAlias(false, true, new TokenAlias(false, false, grammarAccess.getRuleAccess().getCorrespondenceKeyword_9_0()), new TokenAlias(false, false, grammarAccess.getRuleAccess().getLeftCurlyBracketKeyword_9_1()), new TokenAlias(false, false, grammarAccess.getRuleAccess().getRightCurlyBracketKeyword_9_3()));
		match_Rule___SourceKeyword_7_0_LeftCurlyBracketKeyword_7_1_RightCurlyBracketKeyword_7_3__q = new GroupAlias(false, true, new TokenAlias(false, false, grammarAccess.getRuleAccess().getSourceKeyword_7_0()), new TokenAlias(false, false, grammarAccess.getRuleAccess().getLeftCurlyBracketKeyword_7_1()), new TokenAlias(false, false, grammarAccess.getRuleAccess().getRightCurlyBracketKeyword_7_3()));
		match_Rule___TargetKeyword_8_0_LeftCurlyBracketKeyword_8_1_RightCurlyBracketKeyword_8_3__q = new GroupAlias(false, true, new TokenAlias(false, false, grammarAccess.getRuleAccess().getTargetKeyword_8_0()), new TokenAlias(false, false, grammarAccess.getRuleAccess().getLeftCurlyBracketKeyword_8_1()), new TokenAlias(false, false, grammarAccess.getRuleAccess().getRightCurlyBracketKeyword_8_3()));
		match_Schema___AttributeConditionsKeyword_6_0_LeftCurlyBracketKeyword_6_1_RightCurlyBracketKeyword_6_3__q = new GroupAlias(false, true, new TokenAlias(false, false, grammarAccess.getSchemaAccess().getAttributeConditionsKeyword_6_0()), new TokenAlias(false, false, grammarAccess.getSchemaAccess().getLeftCurlyBracketKeyword_6_1()), new TokenAlias(false, false, grammarAccess.getSchemaAccess().getRightCurlyBracketKeyword_6_3()));
		match_Schema___CorrespondenceKeyword_5_0_LeftCurlyBracketKeyword_5_1_RightCurlyBracketKeyword_5_3__q = new GroupAlias(false, true, new TokenAlias(false, false, grammarAccess.getSchemaAccess().getCorrespondenceKeyword_5_0()), new TokenAlias(false, false, grammarAccess.getSchemaAccess().getLeftCurlyBracketKeyword_5_1()), new TokenAlias(false, false, grammarAccess.getSchemaAccess().getRightCurlyBracketKeyword_5_3()));
		match_Schema___SourceKeyword_3_0_LeftCurlyBracketKeyword_3_1_RightCurlyBracketKeyword_3_3__q = new GroupAlias(false, true, new TokenAlias(false, false, grammarAccess.getSchemaAccess().getSourceKeyword_3_0()), new TokenAlias(false, false, grammarAccess.getSchemaAccess().getLeftCurlyBracketKeyword_3_1()), new TokenAlias(false, false, grammarAccess.getSchemaAccess().getRightCurlyBracketKeyword_3_3()));
		match_Schema___TargetKeyword_4_0_LeftCurlyBracketKeyword_4_1_RightCurlyBracketKeyword_4_3__q = new GroupAlias(false, true, new TokenAlias(false, false, grammarAccess.getSchemaAccess().getTargetKeyword_4_0()), new TokenAlias(false, false, grammarAccess.getSchemaAccess().getLeftCurlyBracketKeyword_4_1()), new TokenAlias(false, false, grammarAccess.getSchemaAccess().getRightCurlyBracketKeyword_4_3()));
	}
	
	@Override
	protected String getUnassignedRuleCallToken(EObject semanticObject, RuleCall ruleCall, INode node) {
		return "";
	}
	
	
	@Override
	protected void emitUnassignedTokens(EObject semanticObject, ISynTransition transition, INode fromNode, INode toNode) {
		if (transition.getAmbiguousSyntaxes().isEmpty()) return;
		List<INode> transitionNodes = collectNodes(fromNode, toNode);
		for (AbstractElementAlias syntax : transition.getAmbiguousSyntaxes()) {
			List<INode> syntaxNodes = getNodesFor(transitionNodes, syntax);
			if(match_ObjectVariablePattern___LeftCurlyBracketKeyword_4_0_RightCurlyBracketKeyword_4_2__q.equals(syntax))
				emit_ObjectVariablePattern___LeftCurlyBracketKeyword_4_0_RightCurlyBracketKeyword_4_2__q(semanticObject, getLastNavigableState(), syntaxNodes);
			else if(match_Rule___AttributeConditionsKeyword_10_0_LeftCurlyBracketKeyword_10_1_RightCurlyBracketKeyword_10_3__q.equals(syntax))
				emit_Rule___AttributeConditionsKeyword_10_0_LeftCurlyBracketKeyword_10_1_RightCurlyBracketKeyword_10_3__q(semanticObject, getLastNavigableState(), syntaxNodes);
			else if(match_Rule___CorrespondenceKeyword_9_0_LeftCurlyBracketKeyword_9_1_RightCurlyBracketKeyword_9_3__q.equals(syntax))
				emit_Rule___CorrespondenceKeyword_9_0_LeftCurlyBracketKeyword_9_1_RightCurlyBracketKeyword_9_3__q(semanticObject, getLastNavigableState(), syntaxNodes);
			else if(match_Rule___SourceKeyword_7_0_LeftCurlyBracketKeyword_7_1_RightCurlyBracketKeyword_7_3__q.equals(syntax))
				emit_Rule___SourceKeyword_7_0_LeftCurlyBracketKeyword_7_1_RightCurlyBracketKeyword_7_3__q(semanticObject, getLastNavigableState(), syntaxNodes);
			else if(match_Rule___TargetKeyword_8_0_LeftCurlyBracketKeyword_8_1_RightCurlyBracketKeyword_8_3__q.equals(syntax))
				emit_Rule___TargetKeyword_8_0_LeftCurlyBracketKeyword_8_1_RightCurlyBracketKeyword_8_3__q(semanticObject, getLastNavigableState(), syntaxNodes);
			else if(match_Schema___AttributeConditionsKeyword_6_0_LeftCurlyBracketKeyword_6_1_RightCurlyBracketKeyword_6_3__q.equals(syntax))
				emit_Schema___AttributeConditionsKeyword_6_0_LeftCurlyBracketKeyword_6_1_RightCurlyBracketKeyword_6_3__q(semanticObject, getLastNavigableState(), syntaxNodes);
			else if(match_Schema___CorrespondenceKeyword_5_0_LeftCurlyBracketKeyword_5_1_RightCurlyBracketKeyword_5_3__q.equals(syntax))
				emit_Schema___CorrespondenceKeyword_5_0_LeftCurlyBracketKeyword_5_1_RightCurlyBracketKeyword_5_3__q(semanticObject, getLastNavigableState(), syntaxNodes);
			else if(match_Schema___SourceKeyword_3_0_LeftCurlyBracketKeyword_3_1_RightCurlyBracketKeyword_3_3__q.equals(syntax))
				emit_Schema___SourceKeyword_3_0_LeftCurlyBracketKeyword_3_1_RightCurlyBracketKeyword_3_3__q(semanticObject, getLastNavigableState(), syntaxNodes);
			else if(match_Schema___TargetKeyword_4_0_LeftCurlyBracketKeyword_4_1_RightCurlyBracketKeyword_4_3__q.equals(syntax))
				emit_Schema___TargetKeyword_4_0_LeftCurlyBracketKeyword_4_1_RightCurlyBracketKeyword_4_3__q(semanticObject, getLastNavigableState(), syntaxNodes);
			else acceptNodes(getLastNavigableState(), syntaxNodes);
		}
	}

	/**
	 * Ambiguous syntax:
	 *     ('{' '}')?
	 *
	 * This ambiguous syntax occurs at:
	 *     type=[EClass|QualifiedName] (ambiguity) (rule end)
	 */
	protected void emit_ObjectVariablePattern___LeftCurlyBracketKeyword_4_0_RightCurlyBracketKeyword_4_2__q(EObject semanticObject, ISynNavigable transition, List<INode> nodes) {
		acceptNodes(transition, nodes);
	}
	
	/**
	 * Ambiguous syntax:
	 *     ('attribute conditions' '{' '}')?
	 *
	 * This ambiguous syntax occurs at:
	 *     correspondencePatterns+=CorrVariablePattern '}' (ambiguity) (rule end)
	 *     schema=[Schema|QualifiedName] ('source' '{' '}')? ('target' '{' '}')? ('correspondence' '{' '}')? (ambiguity) (rule end)
	 *     sourcePatterns+=ObjectVariablePattern '}' ('target' '{' '}')? ('correspondence' '{' '}')? (ambiguity) (rule end)
	 *     targetPatterns+=ObjectVariablePattern '}' ('correspondence' '{' '}')? (ambiguity) (rule end)
	 */
	protected void emit_Rule___AttributeConditionsKeyword_10_0_LeftCurlyBracketKeyword_10_1_RightCurlyBracketKeyword_10_3__q(EObject semanticObject, ISynNavigable transition, List<INode> nodes) {
		acceptNodes(transition, nodes);
	}
	
	/**
	 * Ambiguous syntax:
	 *     ('correspondence' '{' '}')?
	 *
	 * This ambiguous syntax occurs at:
	 *     schema=[Schema|QualifiedName] ('source' '{' '}')? ('target' '{' '}')? (ambiguity) 'attribute conditions' '{' attrConditions+=AttrCond
	 *     schema=[Schema|QualifiedName] ('source' '{' '}')? ('target' '{' '}')? (ambiguity) ('attribute conditions' '{' '}')? (rule end)
	 *     sourcePatterns+=ObjectVariablePattern '}' ('target' '{' '}')? (ambiguity) 'attribute conditions' '{' attrConditions+=AttrCond
	 *     sourcePatterns+=ObjectVariablePattern '}' ('target' '{' '}')? (ambiguity) ('attribute conditions' '{' '}')? (rule end)
	 *     targetPatterns+=ObjectVariablePattern '}' (ambiguity) 'attribute conditions' '{' attrConditions+=AttrCond
	 *     targetPatterns+=ObjectVariablePattern '}' (ambiguity) ('attribute conditions' '{' '}')? (rule end)
	 */
	protected void emit_Rule___CorrespondenceKeyword_9_0_LeftCurlyBracketKeyword_9_1_RightCurlyBracketKeyword_9_3__q(EObject semanticObject, ISynNavigable transition, List<INode> nodes) {
		acceptNodes(transition, nodes);
	}
	
	/**
	 * Ambiguous syntax:
	 *     ('source' '{' '}')?
	 *
	 * This ambiguous syntax occurs at:
	 *     schema=[Schema|QualifiedName] (ambiguity) 'target' '{' targetPatterns+=ObjectVariablePattern
	 *     schema=[Schema|QualifiedName] (ambiguity) ('target' '{' '}')? 'correspondence' '{' correspondencePatterns+=CorrVariablePattern
	 *     schema=[Schema|QualifiedName] (ambiguity) ('target' '{' '}')? ('correspondence' '{' '}')? 'attribute conditions' '{' attrConditions+=AttrCond
	 *     schema=[Schema|QualifiedName] (ambiguity) ('target' '{' '}')? ('correspondence' '{' '}')? ('attribute conditions' '{' '}')? (rule end)
	 */
	protected void emit_Rule___SourceKeyword_7_0_LeftCurlyBracketKeyword_7_1_RightCurlyBracketKeyword_7_3__q(EObject semanticObject, ISynNavigable transition, List<INode> nodes) {
		acceptNodes(transition, nodes);
	}
	
	/**
	 * Ambiguous syntax:
	 *     ('target' '{' '}')?
	 *
	 * This ambiguous syntax occurs at:
	 *     schema=[Schema|QualifiedName] ('source' '{' '}')? (ambiguity) 'correspondence' '{' correspondencePatterns+=CorrVariablePattern
	 *     schema=[Schema|QualifiedName] ('source' '{' '}')? (ambiguity) ('correspondence' '{' '}')? 'attribute conditions' '{' attrConditions+=AttrCond
	 *     schema=[Schema|QualifiedName] ('source' '{' '}')? (ambiguity) ('correspondence' '{' '}')? ('attribute conditions' '{' '}')? (rule end)
	 *     sourcePatterns+=ObjectVariablePattern '}' (ambiguity) 'correspondence' '{' correspondencePatterns+=CorrVariablePattern
	 *     sourcePatterns+=ObjectVariablePattern '}' (ambiguity) ('correspondence' '{' '}')? 'attribute conditions' '{' attrConditions+=AttrCond
	 *     sourcePatterns+=ObjectVariablePattern '}' (ambiguity) ('correspondence' '{' '}')? ('attribute conditions' '{' '}')? (rule end)
	 */
	protected void emit_Rule___TargetKeyword_8_0_LeftCurlyBracketKeyword_8_1_RightCurlyBracketKeyword_8_3__q(EObject semanticObject, ISynNavigable transition, List<INode> nodes) {
		acceptNodes(transition, nodes);
	}
	
	/**
	 * Ambiguous syntax:
	 *     ('attribute conditions' '{' '}')?
	 *
	 * This ambiguous syntax occurs at:
	 *     correspondenceTypes+=CorrType '}' (ambiguity) (rule end)
	 *     name=ID ('source' '{' '}')? ('target' '{' '}')? ('correspondence' '{' '}')? (ambiguity) (rule end)
	 *     sourceTypes+=[EPackage|QualifiedName] '}' ('target' '{' '}')? ('correspondence' '{' '}')? (ambiguity) (rule end)
	 *     targetTypes+=[EPackage|QualifiedName] '}' ('correspondence' '{' '}')? (ambiguity) (rule end)
	 */
	protected void emit_Schema___AttributeConditionsKeyword_6_0_LeftCurlyBracketKeyword_6_1_RightCurlyBracketKeyword_6_3__q(EObject semanticObject, ISynNavigable transition, List<INode> nodes) {
		acceptNodes(transition, nodes);
	}
	
	/**
	 * Ambiguous syntax:
	 *     ('correspondence' '{' '}')?
	 *
	 * This ambiguous syntax occurs at:
	 *     name=ID ('source' '{' '}')? ('target' '{' '}')? (ambiguity) 'attribute conditions' '{' attributeCondDefs+=AttrCondDef
	 *     name=ID ('source' '{' '}')? ('target' '{' '}')? (ambiguity) ('attribute conditions' '{' '}')? (rule end)
	 *     sourceTypes+=[EPackage|QualifiedName] '}' ('target' '{' '}')? (ambiguity) 'attribute conditions' '{' attributeCondDefs+=AttrCondDef
	 *     sourceTypes+=[EPackage|QualifiedName] '}' ('target' '{' '}')? (ambiguity) ('attribute conditions' '{' '}')? (rule end)
	 *     targetTypes+=[EPackage|QualifiedName] '}' (ambiguity) 'attribute conditions' '{' attributeCondDefs+=AttrCondDef
	 *     targetTypes+=[EPackage|QualifiedName] '}' (ambiguity) ('attribute conditions' '{' '}')? (rule end)
	 */
	protected void emit_Schema___CorrespondenceKeyword_5_0_LeftCurlyBracketKeyword_5_1_RightCurlyBracketKeyword_5_3__q(EObject semanticObject, ISynNavigable transition, List<INode> nodes) {
		acceptNodes(transition, nodes);
	}
	
	/**
	 * Ambiguous syntax:
	 *     ('source' '{' '}')?
	 *
	 * This ambiguous syntax occurs at:
	 *     name=ID (ambiguity) 'target' '{' targetTypes+=[EPackage|QualifiedName]
	 *     name=ID (ambiguity) ('target' '{' '}')? 'correspondence' '{' correspondenceTypes+=CorrType
	 *     name=ID (ambiguity) ('target' '{' '}')? ('correspondence' '{' '}')? 'attribute conditions' '{' attributeCondDefs+=AttrCondDef
	 *     name=ID (ambiguity) ('target' '{' '}')? ('correspondence' '{' '}')? ('attribute conditions' '{' '}')? (rule end)
	 */
	protected void emit_Schema___SourceKeyword_3_0_LeftCurlyBracketKeyword_3_1_RightCurlyBracketKeyword_3_3__q(EObject semanticObject, ISynNavigable transition, List<INode> nodes) {
		acceptNodes(transition, nodes);
	}
	
	/**
	 * Ambiguous syntax:
	 *     ('target' '{' '}')?
	 *
	 * This ambiguous syntax occurs at:
	 *     name=ID ('source' '{' '}')? (ambiguity) 'correspondence' '{' correspondenceTypes+=CorrType
	 *     name=ID ('source' '{' '}')? (ambiguity) ('correspondence' '{' '}')? 'attribute conditions' '{' attributeCondDefs+=AttrCondDef
	 *     name=ID ('source' '{' '}')? (ambiguity) ('correspondence' '{' '}')? ('attribute conditions' '{' '}')? (rule end)
	 *     sourceTypes+=[EPackage|QualifiedName] '}' (ambiguity) 'correspondence' '{' correspondenceTypes+=CorrType
	 *     sourceTypes+=[EPackage|QualifiedName] '}' (ambiguity) ('correspondence' '{' '}')? 'attribute conditions' '{' attributeCondDefs+=AttrCondDef
	 *     sourceTypes+=[EPackage|QualifiedName] '}' (ambiguity) ('correspondence' '{' '}')? ('attribute conditions' '{' '}')? (rule end)
	 */
	protected void emit_Schema___TargetKeyword_4_0_LeftCurlyBracketKeyword_4_1_RightCurlyBracketKeyword_4_3__q(EObject semanticObject, ISynNavigable transition, List<INode> nodes) {
		acceptNodes(transition, nodes);
	}
	
}
