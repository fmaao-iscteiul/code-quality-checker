package pt.iscte.paddle.codequality.issues;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;
import pt.iscte.paddle.codequality.misc.IssueType;
import pt.iscte.paddle.codequality.cases.base.MultipleOccurrencesIssue;
import pt.iscte.paddle.codequality.misc.Classification;
import pt.iscte.paddle.javardise.service.IClassWidget;
import pt.iscte.paddle.javardise.service.ICodeDecoration;
import pt.iscte.paddle.javardise.service.IJavardiseService;
import pt.iscte.paddle.javardise.service.IWidget;
import pt.iscte.paddle.javardise.util.HyperlinkedText;
import pt.iscte.paddle.model.IProgramElement;

public class Duplicate extends MultipleOccurrencesIssue {

	public Duplicate(List<IProgramElement> occurrences) {
		super(IssueType.DUPLICATE_CODE, Classification.SERIOUS, occurrences);
	}

	@Override
	protected void generateExplanation(IClassWidget widget, Composite comp, int style) {
		IWidget w = IJavardiseService.getWidget(occurrences.get(0));
		ICodeDecoration<Text> t = w.addNote("Couldn't this be \n anywhere else?", ICodeDecoration.Location.RIGHT);
		t.show();
		getDecorations().add(t);
		if(w != null) {
			Link link = new HyperlinkedText(null)
					.words("The statement ")
					.link(occurrences.get(0).toString(), () -> {
						
					})
					.words(" was found in a condition and it's alternatives (elses).")
					.words("\n\n - This generates code duplication that should be avoided in order to maintain a good quality code.")
					.words("\n - Try extrating the duplicates from the condition blocks, this will help preventing code duplication.")
					.create(comp, SWT.WRAP | SWT.SCROLL_LINE | SWT.V_SCROLL);

			link.requestLayout();
		}
	}

	/**
	 * "The highlighted statements are duplicated. This"
			+ "logic could be simplified in order to remove code duplication and improve the code base quality.";
	 * @return
	 */
	
	public List<IProgramElement> getDuplicates() {
		return occurrences;
	}

}
