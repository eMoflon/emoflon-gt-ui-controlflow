/*
 * generated by Xtext 2.11.0
 */
package org.moflon.gt.mosl.controlflow.language.ide

import com.google.inject.Guice
import org.eclipse.xtext.util.Modules2
import org.moflon.gt.mosl.controlflow.language.MOSLControlFlowRuntimeModule
import org.moflon.gt.mosl.controlflow.language.MOSLControlFlowStandaloneSetup
import org.moflon.gt.mosl.controlflow.language.ide.MOSLControlFlowIdeModule
/**
 * Initialization support for running Xtext languages as language servers.
 */
class MOSLControlFlowIdeSetup extends MOSLControlFlowStandaloneSetup {

	override createInjector() {
		Guice.createInjector(Modules2.mixin(new MOSLControlFlowRuntimeModule, new MOSLControlFlowIdeModule))
	}
	
}
