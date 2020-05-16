package pt.iscte.paddle.codequality.issues;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;
import pt.iscte.paddle.codequality.misc.IssueType;
import pt.iscte.paddle.codequality.cases.base.QualityIssue;
import pt.iscte.paddle.codequality.misc.Classification;
import pt.iscte.paddle.javardise.service.IClassWidget;
import pt.iscte.paddle.javardise.service.ICodeDecoration;
import pt.iscte.paddle.javardise.service.IWidget;
import pt.iscte.paddle.javardise.util.HyperlinkedText;
import pt.iscte.paddle.model.IProgramElement;

public class MagicNumber extends QualityIssue {

	private List<IProgramElement> occurrences = new ArrayList<IProgramElement>();
	private IProgramElement magicNumber;

	public MagicNumber(String explanation, IProgramElement magicNumber) {
		super(IssueType.MAGIC_NUMBER, Classification.AVERAGE);
		this.magicNumber = magicNumber;
		this.occurrences.add(magicNumber);
	}

	public IProgramElement getMagicNumber() {
		return magicNumber;
	}

	public List<IProgramElement> getOccurrences() {
		return occurrences;
	}

	public void addAssignment(IProgramElement statement) {
		this.occurrences.add(statement);
	}

	@Override
	public void generateComponent(IClassWidget widget, org.eclipse.swt.widgets.Composite comp, int style) {
		generateExplanation(widget, comp, style);
		generateMark(widget, comp, style);
	}

	@Override
	protected void generateExplanation(IClassWidget widget, Composite comp, int style) {

		Link link = new HyperlinkedText(null)
				.words("Issue: \n\n")
				.words("The highlighted number " ).link(magicNumber.toString(), () -> {
				})
				.words(" doesn't really tell what it stands for.")
				.words("\n\n - It can be hard for another programmer to look at the number " + magicNumber  + " and figure out what it represents.")
				.words("\n - It can also lead to bugs because it's more difficult to change it in every place that it is used.")
				.words("\n\n Suggestion: \n\n - Try replacing this the number " + magicNumber + " with a well named variable.")
				.create(comp, SWT.WRAP | SWT.V_SCROLL);

		link.requestLayout();
		//		"A magic number is a numeric literal that is used in the middle of a block of code without any kind of explanation."
		//		+ " A number in isolation can be difficult for other programmers to understand because it provides no context. If this value is also duplicated, it becomes"
		//		+ " harder to update the code because it needs to be replaced in multiple places."
	}

	@Override
	protected void generateMark(IClassWidget widget, Composite comp, int style) {
		for (IProgramElement el : occurrences) {
			IWidget w = generateElementWidget(el);
			if(w != null) {
				if(occurrences.indexOf(el) == 0) {
					ICodeDecoration<Text> d2 = w.addNote("What does this number \n represent?", ICodeDecoration.Location.RIGHT);
					d2.show();
					getDecorations().add(d2);
				}
				ICodeDecoration<Canvas> d = w.addMark(getColor());
				d.show();
				getDecorations().add(d);
			}
		}
	}

}
