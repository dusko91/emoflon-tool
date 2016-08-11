/*
 * generated by Xtext
 */
package org.moflon.tgg.mosl.ui.contentassist

import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.Assignment
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalAcceptor
import org.moflon.tgg.mosl.tgg.AttributeExpression

/**
 * See https://www.eclipse.org/Xtext/documentation/304_ide_concepts.html#content-assist
 * on how to customize the content assistant.
 */
class TGGProposalProvider extends AbstractTGGProposalProvider {

	override completeAttributeConstraint_Op(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		super.completeAttributeConstraint_Op(model, assignment, context, acceptor)
		
		val comparisonOps = newArrayList(" == ", " != ", " <= ", " >= "," < ", " > ")
		
		for (opProposal : comparisonOps) {
			acceptor.accept(createCompletionProposal(opProposal, context))
		}
	}
	override completeAttributeExpression_Attribute(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor){
		super.completeAttributeExpression_Attribute(model,assignment,context,acceptor);
		
		if (model instanceof AttributeExpression) {
			var attrVar = model as AttributeExpression
			
			for (attr : attrVar.objectVar.type.EAllAttributes) {
				acceptor.accept(createCompletionProposal(attr.name, context))
			}
		}
	}
}
