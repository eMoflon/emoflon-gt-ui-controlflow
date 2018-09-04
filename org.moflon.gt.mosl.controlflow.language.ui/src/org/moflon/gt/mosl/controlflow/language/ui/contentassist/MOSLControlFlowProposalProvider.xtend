/*
 * generated by Xtext 2.11.0
 */
package org.moflon.gt.mosl.controlflow.language.ui.contentassist

import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.Assignment
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalAcceptor
import org.moflon.gt.mosl.controlflow.language.moslControlFlow.PatternStatement
import org.emoflon.ibex.gt.editor.gT.EditorParameterOrNode
import org.eclipse.jface.text.contentassist.ICompletionProposal

/**
 * See https://www.eclipse.org/Xtext/documentation/304_ide_concepts.html#content-assist
 * on how to customize the content assistant.
 */
class MOSLControlFlowProposalProvider extends AbstractMOSLControlFlowProposalProvider {

    /**
     * This custom content proposal provider collects the names of all editor parameters and
     * nodes of the referenced pattern
     */
    override void completeCalledPatternParameter_Parameter(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
        super.completeCalledPatternParameter_Parameter(model, assignment, context, acceptor)
        val patternStatement = model as PatternStatement
        val editorPattern = patternStatement?.patternReference?.pattern
        val proposals=<ICompletionProposal>newArrayList
        proposals.addAll(editorPattern.nodes.map[editorNode | createCompletionProposal(editorNode, context)])
        proposals.addAll(editorPattern.parameters.map[parameter | createCompletionProposal(parameter, context)])
        proposals.forEach[proposal | acceptor.accept(proposal)]
    }

    /**
     * Maps the given EditorParameterOrNode to a proposal that contains the name of the EditorParameterOrNode
     */
    private def ICompletionProposal createCompletionProposal(EditorParameterOrNode parameterOrNode, ContentAssistContext context) {
        createCompletionProposal(parameterOrNode.name, context)
    }
}
