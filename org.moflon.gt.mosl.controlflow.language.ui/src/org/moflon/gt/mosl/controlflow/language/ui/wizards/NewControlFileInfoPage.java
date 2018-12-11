package org.moflon.gt.mosl.controlflow.language.ui.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.moflon.tie.gt.mosl.ide.ui.wizards.AbstractNewFileInfoPage;
import org.moflon.tie.gt.mosl.ide.ui.wizards.WizardFileTemplate;

public class NewControlFileInfoPage extends AbstractNewFileInfoPage {

	public NewControlFileInfoPage(IStructuredSelection selection) {
		super("New Controlflow file", selection, "mcf");
	}

	@Override
	protected WizardFileTemplate createWizardFileTemplate() {
		return new ControlFileTemplate();
	}

}
