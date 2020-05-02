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
import pt.iscte.paddle.model.ISelection;

public class EmptySelection extends SingleOcurrenceIssue {

	public EmptySelection(String explanation, IProgramElement branch) {
		super(IssueType.EMPTY_SELECTION, Classification.SERIOUS, branch);
	}

	@Override
	public void generateComponent(IClassWidget widget, Composite comp, int style) {
		generateMark(widget, comp, style);
		generateExplanation(widget, comp, style);
	}

	@Override
	protected void generateMark(IClassWidget widget, Composite comp, int style) {
		IWidget w = generateElementWidget(occurrence);
		if(w != null) {
			ICodeDecoration<Canvas> d = w.addMark(getColor());
			d.show();
			getDecorations().add(d);
			ICodeDecoration<Text> d2 = w.addNote("Why is it empty?", ICodeDecoration.Location.RIGHT);
			d2.show();
			getDecorations().add(d2);
		}
	}
	
	@Override
	protected void generateExplanation(IClassWidget widget, Composite comp, int style) {
		IWidget w = IJavardiseService.getWidget(occurrence);
		if(w != null) {
			Link link = new HyperlinkedText(null)
					.words("The highlighted if block has no actions inside. \n ")
					.words("\n - If the condition ").link(((ISelection) occurrence).getGuard().toString(), ()->{})
					.words(" is true, nothing will happen.")
					.words("\n - Empty code blocks don't add actions to the program execution.")
					.words("\n\nSuggestion:\n\n Either Add some logic to the if block, or remove it.")
					.create(comp, SWT.WRAP | SWT.V_SCROLL);

			link.requestLayout();
		}
	}

}
