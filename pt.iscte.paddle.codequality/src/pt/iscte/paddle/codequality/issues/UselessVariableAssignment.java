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

public class UselessVariableAssignment extends SingleOcurrenceIssue {

	public UselessVariableAssignment(IProgramElement occurrence) {
		super(IssueType.USELESS_CODE, Classification.SERIOUS, occurrence);
		System.out.println(" DJWA DJ WAJDNAWJDNJWANDJWANDJNAWJ");
	}
	
	@Override
	protected void generateMark(IClassWidget widget, Composite comp, int style) {
		IWidget w = generateElementWidget(occurrence);
		if(w != null) {
			ICodeDecoration<Canvas> d = w.addMark(getColor());
			d.show();
			getDecorations().add(d);
			ICodeDecoration<Text> d2 = w.addNote("This value won't be used!", ICodeDecoration.Location.RIGHT);
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
					.words(" value was not used. ")
					.words("\n\n - Variables don't need to be initialized before being assigned with a value. ")
					.words("\n - The value was not used before the second assignment, which means that is as no impact. ")
					.words("\n - Unused values are useless to the program.")
					.words("\n\n Suggestion: \n\n- Remove this useless assignment because its value isn't used.")
					.create(comp, SWT.WRAP | SWT.V_SCROLL);

			link.requestLayout();
		}
	}

}
