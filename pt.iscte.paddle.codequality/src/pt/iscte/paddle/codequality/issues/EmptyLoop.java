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
import pt.iscte.paddle.model.IControlStructure;
import pt.iscte.paddle.model.ILoop;

public class EmptyLoop extends SingleOcurrenceIssue {

	public EmptyLoop(IssueType category, String explanation, IControlStructure branch) {
		super(IssueType.EMPTY_LOOP, Classification.SERIOUS, branch);
	}

	@Override
	protected void generateMark(IClassWidget widget, Composite comp, int style) {
		IWidget w = generateElementWidget(occurrence);
		if(w != null) {
			ICodeDecoration<Canvas> d = w.addMark(getColor());
			d.show();
			getDecorations().add(d);
			ICodeDecoration<Text> d2 = w.addNote("Why is this loop empty?", ICodeDecoration.Location.RIGHT);
			d2.show();
			getDecorations().add(d2);
		}
	}

	@Override
	protected void generateExplanation(IClassWidget widget, Composite comp, int style) {
		IWidget w = IJavardiseService.getWidget(occurrence);
		if(w != null) {
			Link link = new HyperlinkedText(null)
					.words("The highlighted loop block has no actions inside. \n ")
					.words("\n - If the condition ").link(((ILoop) occurrence).getGuard().toString(), ()->{})
					.words(" is true, nothing but empty iterations will happen.")
					.words("\n - Empty code blocks don't add actions to the program execution.")
					.words("\n\nSuggestion: \n\n You should avoid empty blocks by adding some logic, or removing them.")
					.create(comp, SWT.WRAP | SWT.V_SCROLL);

			link.requestLayout();
		}
	}
}
