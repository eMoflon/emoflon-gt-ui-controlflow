package org.moflon.gt.mosl.controlflow.language.ui.highlighting;

import org.moflon.gt.mosl.ide.ui.highlighting.AbstractHighlightProviderController;
import org.moflon.gt.mosl.ide.ui.highlighting.AbstractHighlightingConfiguration;
import org.moflon.gt.mosl.ide.ui.highlighting.AbstractSemanticHighlightingCalculator;
import org.moflon.gt.mosl.ide.ui.highlighting.AbstractTokenMapper;

public class MOSLControlFlowHighlightProviderController extends AbstractHighlightProviderController {

	public MOSLControlFlowHighlightProviderController(Class<? extends AbstractTokenMapper> tokenClass) {
		super(MOSLControlFlowAutoFactory.class,tokenClass);
	}

	@Override
	protected Class<? extends AbstractSemanticHighlightingCalculator> getCalculatorClass() {
		return MOSLControlFlowSemanticHighlightingCalculator.class;
	}

	@Override
	protected Class<? extends AbstractHighlightingConfiguration> getConfigClass() {
		return MOSLControlFlowHighlightingConfiguration.class;
	}

}
