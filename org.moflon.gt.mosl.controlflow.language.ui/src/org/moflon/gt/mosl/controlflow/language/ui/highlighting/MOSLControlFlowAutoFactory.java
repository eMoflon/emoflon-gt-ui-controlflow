package org.moflon.gt.mosl.controlflow.language.ui.highlighting;

import java.util.ArrayList;
import java.util.List;

import org.moflon.gt.mosl.ide.ui.highlighting.HighlightAutoFactory;
import org.moflon.gt.mosl.ide.ui.highlighting.rules.AbstractHighlightingRule;

public class MOSLControlFlowAutoFactory extends HighlightAutoFactory {

	@Override
	protected List<Class<? extends AbstractHighlightingRule>> manuallyLoadedClasses() {
		return new ArrayList<Class<? extends AbstractHighlightingRule>>();
	}
}
