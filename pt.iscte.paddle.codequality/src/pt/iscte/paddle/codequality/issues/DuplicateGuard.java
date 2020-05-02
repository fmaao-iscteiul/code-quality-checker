package pt.iscte.paddle.codequality.issues;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;

import pt.iscte.paddle.codequality.cases.base.MultipleOccurrencesIssue;
import pt.iscte.paddle.codequality.misc.Classification;
import pt.iscte.paddle.codequality.misc.IssueType;
import pt.iscte.paddle.javardise.service.IClassWidget;
import pt.iscte.paddle.javardise.service.ICodeDecoration;
import pt.iscte.paddle.javardise.service.IWidget;
import pt.iscte.paddle.javardise.util.HyperlinkedText;
import pt.iscte.paddle.model.IProgramElement;

public class DuplicateGuard extends MultipleOccurrencesIssue {
	
	public DuplicateGuard(List<IProgramElement> occurrence) {
		super(IssueType.DUPLICATE_SELECTION_GUARD, Classification.AVERAGE, occurrence);
	}
	
	@Override
	public void generateComponent(IClassWidget widget, Composite comp, int style) {
		generateMark(widget, comp, style);
		generateExplanation(widget, comp, style);
	}
	
	@Override
	protected void generateExplanation(IClassWidget widget, Composite comp, int style) {
		IWidget w = generateElementWidget(occurrences.get(0));
		ICodeDecoration<Text> d2 = w.addNote("Unnecessary double-check?", ICodeDecoration.Location.RIGHT);
		d2.show();
		getDecorations().add(d2);
		if(w != null) {
			Link link = new HyperlinkedText(null)
					.words("The condition ").link(occurrences.get(0).toString(), () -> {})
					.words(" was found duplicated inside the code block.")
					.words("\n\n - Neither of the variables used in the conditions had their values changed in between.")
					.words("\n - Double checking a condition which parts don't change in between checks, will have the same result.")
					.words("\n - Since the result is the same, it doesn't need to be duplicated.")
					.words("\n\nSolution: \n\n Remove the second check since it isn't necessary.")
					.create(comp, SWT.WRAP | SWT.V_SCROLL);

			link.requestLayout();
		}
	}

}
