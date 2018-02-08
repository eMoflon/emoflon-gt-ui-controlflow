package org.moflon.gt.mosl.controlflow.language.ui.highlighting;

import org.moflon.gt.mosl.ide.ui.highlighting.AbstractTokenMapper;

import com.google.inject.Inject;

public class MOSLControlFlowTokenMapper extends AbstractTokenMapper {
	@Inject
	static MOSLControlFlowTokenMapper mapper = new MOSLControlFlowTokenMapper();
}
