package org.moflon.gt.mosl.controlflow.language.ui.highlighting;

import java.util.Arrays;
import java.util.List;

import org.moflon.gt.mosl.ide.ui.highlighting.HighlightAutoFactory;
import org.moflon.gt.mosl.ide.ui.highlighting.rules.AbstractHighlightingRule;
import org.moflon.gt.mosl.pattern.language.ui.highlighting.rules.HandleThisHighlightingRule;

public class MOSLControlFlowAutoFactory extends HighlightAutoFactory {

	@Override
	protected List<Class<? extends AbstractHighlightingRule>> manuallyLoadedClasses() {
		return Arrays.asList(HandleThisHighlightingRule.class);
	}
}
