package org.moflon.gt.mosl.controlflow.language.ui.highlighting.rules;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.ui.editor.utils.TextStyle;
import org.moflon.gt.mosl.controlflow.language.moslControlFlow.CalledParameter;
import org.moflon.gt.mosl.controlflow.language.moslControlFlow.MethodParameter;
import org.moflon.gt.mosl.controlflow.language.moslControlFlow.ObjectVariableStatement;
import org.moflon.gt.mosl.ide.ui.highlighting.AbstractHighlightProviderController;
import org.moflon.gt.mosl.ide.ui.highlighting.RegisterRule;
import org.moflon.gt.mosl.ide.ui.highlighting.rules.AbstractHighlightingRule;
import org.moflon.gt.mosl.ide.ui.highlighting.utils.XtextColor;
@RegisterRule
public class MarkLocalStatementsHighlightingRule extends AbstractHighlightingRule {

	public MarkLocalStatementsHighlightingRule(AbstractHighlightProviderController controller) {
		super(controller);
	}

	@Override
	protected String id() {
		return "MarkLocalStatement";
	}

	@Override
	protected String description() {
		return "Local Statements are brown";
	}

	@Override
	protected TextStyle getTextStyle() {
		TextStyle ts = new TextStyle();
		ts.setColor(controller.getColorManager().getColor(XtextColor.BROWN));
		return ts;
	}

	@Override
	protected boolean getHighlightingConditions(EObject moslObject, INode node) {
		String text = node.getText();
		if(moslObject instanceof ObjectVariableStatement) {
			
			ObjectVariableStatement objectVariableStatement = ObjectVariableStatement.class.cast(moslObject);
			return objectVariableStatement.getName().equals(text);
		} else if(moslObject instanceof CalledParameter) {
			CalledParameter calledParameter = CalledParameter.class.cast(moslObject);
			return (calledParameter.getObject() != null && ETypedElement.class.cast(calledParameter.getObject()).getName().equals(text)) 
					|| (calledParameter.getCreate() != null && calledParameter.getCreate().getName().equals(text));
		} else if (moslObject instanceof MethodParameter) {
			MethodParameter methodParameter = MethodParameter.class.cast(moslObject);
			return methodParameter.getName().equals(text);
		}
		return false;
	}

}
