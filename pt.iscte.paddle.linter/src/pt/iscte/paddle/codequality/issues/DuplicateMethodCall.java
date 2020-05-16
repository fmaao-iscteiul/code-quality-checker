package pt.iscte.paddle.codequality.issues;

import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;
import pt.iscte.paddle.codequality.misc.IssueType;
import pt.iscte.paddle.codequality.cases.base.MultipleOccurrencesIssue;
import pt.iscte.paddle.codequality.misc.Classification;
import pt.iscte.paddle.javardise.service.IClassWidget;
import pt.iscte.paddle.javardise.service.ICodeDecoration;
import pt.iscte.paddle.javardise.service.IJavardiseService;
import pt.iscte.paddle.javardise.util.HyperlinkedText;
import pt.iscte.paddle.model.IProcedureCall;
import pt.iscte.paddle.model.IProgramElement;

public class DuplicateMethodCall extends MultipleOccurrencesIssue {
	

	public DuplicateMethodCall(List<IProgramElement> duplicatesList) {	
		super(IssueType.DUPLICATE_SELECTION_GUARD, Classification.SERIOUS, duplicatesList);
	}
	
	@Override
	public void generateComponent(IClassWidget widget, org.eclipse.swt.widgets.Composite comp, int style) {
		this.generateMark(widget, comp, style);
		this.generateExplanation(widget, comp, style);
	}
	
	@Override
	protected void generateExplanation(IClassWidget widget, Composite comp, int style) {
		IProcedureCall call = (IProcedureCall) occurrences.get(0);
		Link link = new HyperlinkedText(null)
				.words("Issue:\n\n")
				.words("The function ")
				.link(call.toString(), () -> {
					ICodeDecoration<Canvas> d = IJavardiseService.getWidget(call.getProcedure()).addMark(getColor());
					d.show();
					getDecorations().add(d);
				})
				.words(" was called more than once. \n\n - Its arguments values aren't changed between calls.")
				.words("\n - The method doesn't change any variable that affects the program execution flow.")
				.words("\n - This means that both calls will result in the same.")
				.words("\n - There is no point on having two " + call + " calls that return the exact same result.")
				.words("\n\nSuggestion: \n\nSince both calls result in the same, you should remove one of them.")
				.create(comp, SWT.WRAP | SWT.V_SCROLL);

		link.requestLayout();
	}

}
