package pt.iscte.paddle.codequality.cases;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;

import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.codequality.misc.Classification;
import pt.iscte.paddle.javardise.service.IClassWidget;
import pt.iscte.paddle.javardise.service.ICodeDecoration;
import pt.iscte.paddle.javardise.service.IJavardiseService;
import pt.iscte.paddle.javardise.service.IWidget;
import pt.iscte.paddle.javardise.util.HyperlinkedText;
import pt.iscte.paddle.model.IProgramElement;

public class MagicNumber extends BadCodeCase{

	private List<IProgramElement> occurrences = new ArrayList<IProgramElement>();
	private IProgramElement magicNumber;

	public MagicNumber(String explanation, IProgramElement magicNumber) {
		super(Category.MAGIC_NUMBER, Classification.AVERAGE);
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
				.words("The highlighted number " ).link(magicNumber.toString(), () -> {
				})
				.words(" doesn't provide any context.")
				.words("\n\n - It can be hard for another programmer to look at the number " + magicNumber  + " and figure out what it represents.")
				.words("\n - It's also harder to change the number since in in all the places that it's used.")
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
					ICodeDecoration<Text> d2 = w.addNote("What is this number \n used for?", ICodeDecoration.Location.RIGHT);
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
