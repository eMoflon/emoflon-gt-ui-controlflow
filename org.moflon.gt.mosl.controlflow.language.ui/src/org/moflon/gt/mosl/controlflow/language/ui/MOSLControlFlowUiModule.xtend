/*
 * generated by Xtext 2.11.0
 */
package org.moflon.gt.mosl.controlflow.language.ui

import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor
import org.moflon.gt.mosl.controlflow.language.ui.AbstractMOSLControlFlowUiModule
import org.moflon.gt.mosl.controlflow.language.ui.highlighting.MOSLControlFlowHighlightProviderController
import org.moflon.gt.mosl.controlflow.language.ui.highlighting.MOSLControlFlowTokenMapper
import com.google.inject.Binder

/**
 * Use this class to register components to be used within the Eclipse IDE.
 */
@FinalFieldsConstructor
class MOSLControlFlowUiModule extends AbstractMOSLControlFlowUiModule {
	var controller = new MOSLControlFlowHighlightProviderController(MOSLControlFlowTokenMapper)

	override configure (Binder binder){
		controller.bind(binder)
		super.configure(binder)
	}
}
