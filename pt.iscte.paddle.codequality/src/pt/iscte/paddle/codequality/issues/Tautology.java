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
import pt.iscte.paddle.javardise.service.IWidget;
import pt.iscte.paddle.javardise.util.HyperlinkedText;
import pt.iscte.paddle.model.IProgramElement;

public class Tautology extends SingleOcurrenceIssue {

	public Tautology(String explanation, IProgramElement occurrence) {
		super(IssueType.TALTOLOGY, Classification.SERIOUS, occurrence);
	}
	
	@Override
	protected void generateMark(IClassWidget widget, Composite comp, int style) {
		IWidget w = generateElementWidget(occurrence);
		if(w != null) {
			ICodeDecoration<Canvas> d = w.addMark(getColor());
			d.show();
			getDecorations().add(d);
			ICodeDecoration<Text> d2 = w.addNote("Isn't this always true?", ICodeDecoration.Location.RIGHT);
			d2.show();
			getDecorations().add(d2);
		}
	}
	
	@Override
	protected void generateExplanation(IClassWidget widget, Composite comp, int style) {
		IWidget w = generateElementWidget(occurrence);
		if(w != null) {
			Link link = new HyperlinkedText(null)
					.words("The condition ").link(occurrence.toString(), () -> {}).words(" represents a tautology case. ")
					.words("\n\n - This means that this condition will allways be avaliated as true.")
					.words("\n - The program will always execute the code inside this condition, which means that"
							+ " it is not necessary, or wrong.")
					.words("\n\nSuggestion: Change this condition so that it isn't always true.")
					.create(comp, SWT.WRAP | SWT.V_SCROLL);

			link.requestLayout();
		}
	}
}
