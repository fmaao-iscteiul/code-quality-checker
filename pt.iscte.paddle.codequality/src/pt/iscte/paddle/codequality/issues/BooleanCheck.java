package pt.iscte.paddle.codequality.issues;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
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
import pt.iscte.paddle.model.IExpression;

public class BooleanCheck extends SingleOcurrenceIssue {


	public BooleanCheck(String explanation, IExpression selectionGuard) {
		super(IssueType.FAULTY_BOOLEAN_CHECK, Classification.LIGHT, selectionGuard);
	}

	@Override
	protected void generateMark(IClassWidget widget, org.eclipse.swt.widgets.Composite comp, int style) {
		IWidget w = generateElementWidget(occurrence);
		if(w != null) {
			ICodeDecoration<Canvas> d = w.addMark(getColor());
			d.show();
			getDecorations().add(d);
			ICodeDecoration<Text> d2 = w.addNote("Maybe use \n the ! operator?", ICodeDecoration.Location.RIGHT);
			d2.show();
			getDecorations().add(d2);
		}
	}

	@Override
	protected void generateExplanation(IClassWidget widget, org.eclipse.swt.widgets.Composite comp, int style) {
		IWidget w = IJavardiseService.getWidget(occurrence);
		if(w != null) {
			Link link = new HyperlinkedText(null)
					.words("Issue:\n\n")
					.words("The condition ")
					.link(occurrence.toString(), () -> {}) 
					.words(" could make use of the '!' operator. \n")	
					.words("\n - It represents the comparision between a boolean variable and one of it's binary possible values (true or false).")
					.words("\n - It is more of a styling mather, but this can negatively affect the code readibility, due to the condition's length.")
					.words("\n\nSuggestion:\nTry using the negation (!) operator in order to improve readibility.")
					.create(comp, SWT.WRAP | SWT.V_SCROLL);

			link.requestLayout();
		}
	}

}
