package org.moflon.gt.mosl.controlflow.language.ui.highlighting;

import org.moflon.gt.mosl.controlflow.language.services.MOSLControlFlowGrammarAccess;
import org.moflon.gt.mosl.ide.ui.highlighting.AbstractSemanticHighlightingCalculator;

import com.google.inject.Inject;

public class MOSLControlFlowSemanticHighlightingCalculator extends AbstractSemanticHighlightingCalculator {
	
	@Inject
	MOSLControlFlowGrammarAccess ga;
}
