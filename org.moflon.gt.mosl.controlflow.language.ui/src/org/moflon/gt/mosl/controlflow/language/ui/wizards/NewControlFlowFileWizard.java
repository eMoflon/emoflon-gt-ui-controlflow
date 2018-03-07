package org.moflon.gt.mosl.controlflow.language.ui.wizards;

import org.moflon.gt.mosl.ide.ui.wizards.AbstractNewFileInfoPage;
import org.moflon.gt.mosl.ide.ui.wizards.AbstractNewFileWizard;

public class NewControlFlowFileWizard extends AbstractNewFileWizard {

	@Override
	protected AbstractNewFileInfoPage createMainPage() {
		return new NewControlFileInfoPage(this.selection);
	}

}
