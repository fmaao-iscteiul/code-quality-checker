package pt.iscte.paddle.codequality.issues;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
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

public class UselessSelfAssignment extends SingleOcurrenceIssue {

	public UselessSelfAssignment(IProgramElement occurrence) {
		super(IssueType.FAULTY_ASSIGNMENT, Classification.AVERAGE, occurrence);
	}


	@Override
	protected void generateMark(IClassWidget widget, Composite comp, int style) {
		IWidget w = generateElementWidget(occurrence);
		if(w != null) {
			ICodeDecoration<Canvas> d = w.addMark(getColor());
			d.show();
			getDecorations().add(d);
			ICodeDecoration<Text> d2 = w.addNote("What does this actually do?", ICodeDecoration.Location.RIGHT);
			d2.show();
			getDecorations().add(d2);
		}
	}

	@Override
	protected void generateExplanation(IClassWidget widget, Composite comp, int style) {
		IWidget w = IJavardiseService.getWidget(occurrence);
		if(w != null) {
			Link link = new HyperlinkedText(null)
					.words("Issue: \n\n")
					.words("The assignment ").link(occurrence.toString(), () -> {
					})
					.words(" means that the variable was assigned with the value that it already had. ")
					.words("\n\n - It is an useless assignment because it has no impact in the program.")
					.words("\n\n Suggestion: \n\n- Remove this useless assignment.")
					.create(comp, SWT.WRAP | SWT.V_SCROLL);

			link.requestLayout();
		}
	}
}
