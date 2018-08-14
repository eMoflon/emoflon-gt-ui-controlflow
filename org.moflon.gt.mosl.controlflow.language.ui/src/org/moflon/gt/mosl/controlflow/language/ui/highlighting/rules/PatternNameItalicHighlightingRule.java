package org.moflon.gt.mosl.controlflow.language.ui.highlighting.rules;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.ui.editor.utils.TextStyle;
import org.moflon.gt.mosl.controlflow.language.moslControlFlow.PatternReference;
import org.moflon.gt.mosl.ide.ui.highlighting.AbstractHighlightProviderController;
import org.moflon.gt.mosl.ide.ui.highlighting.RegisterRule;
import org.moflon.gt.mosl.ide.ui.highlighting.rules.AbstractHighlightingRule;

@RegisterRule
public class PatternNameItalicHighlightingRule extends AbstractHighlightingRule {

	public PatternNameItalicHighlightingRule(AbstractHighlightProviderController controller) {
		super(controller);
	}

	@Override
	protected String id() {
		return "PatternNameItalic";
	}

	@Override
	protected String description() {
		return "The name of the Pattern will be italic";
	}

	@Override
	protected TextStyle getTextStyle() {
		TextStyle ts = new TextStyle();
		ts.setStyle(SWT.ITALIC);
		return ts;
	}

	@Override
	protected boolean getHighlightingConditions(EObject moslObject, INode node) {
		if (moslObject instanceof PatternReference) {
			String text = node.getText();
			PatternReference patternReference = PatternReference.class.cast(moslObject);
			if (patternReference.getPattern() != null)
				return patternReference.getPattern().getName().equals(text);
			else
				return false;
		}
		return false;
	}

}
