package pt.iscte.paddle.codequality.issues;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;

import pt.iscte.paddle.codequality.misc.IssueType;
import pt.iscte.paddle.codequality.cases.base.SingleOcurrenceIssue;
import pt.iscte.paddle.codequality.misc.Classification;
import pt.iscte.paddle.javardise.service.IClassWidget;
import pt.iscte.paddle.javardise.service.ICodeDecoration;
import pt.iscte.paddle.javardise.service.IJavardiseService;
import pt.iscte.paddle.javardise.service.IWidget;
import pt.iscte.paddle.javardise.util.HyperlinkedText;
import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.model.ISelection;

public class BooleanReturnCheck extends SingleOcurrenceIssue {

	public BooleanReturnCheck(IProgramElement selection) {
		super(IssueType.FAULTY_RETURN_BOOLEAN_CHECK, Classification.AVERAGE, selection);
	}

	@Override
	protected void generateMark(IClassWidget widget, Composite comp, int style) {
		super.generateMark(widget, comp, style);
	}

	@Override
	protected void generateExplanation(IClassWidget widget, Composite comp, int style) {
		IWidget w = IJavardiseService.getWidget(occurrence);
		ICodeDecoration<Text> t = w.addNote("Shouldn't you \n return the variable?", ICodeDecoration.Location.RIGHT);
		t.show();
		getDecorations().add(t);
		if(w != null) {
			Link link = new HyperlinkedText(null)
					.words("Issue:\n\n")
					.words("The ").link("if( "+((ISelection) occurrence).getGuard().toString() + " )", ()->{}).words(" condition is unnecessary!\n ")
					.words("\n - There is no need to check the value of a boolean variable before returning true or false.")
					.words("\n - The condition itself already represents the value meant to be returned.")
					.words("\n\nSuggestion:\n\n Return the boolean variable instead of checking it's value and then returning true or false.")
					.create(comp, SWT.WRAP | SWT.SCROLL_LINE | SWT.V_SCROLL);

			link.requestLayout();
		}
	}
}
