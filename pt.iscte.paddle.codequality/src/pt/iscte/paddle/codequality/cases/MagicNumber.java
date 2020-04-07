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
		super(Category.MAGIC_NUMBER, explanation);
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
					IWidget w = IJavardiseService.getWidget(occurrences.get(0));
					ICodeDecoration<Text> d2 = w.addNote("What is this number \n used for?", ICodeDecoration.Location.RIGHT);
					d2.show();
					getDecorations().add(d2);
				})
				.words(" represents a magic number. \n - It doesn't provide any context about what it does."
						+ " \n - This can make the code difficult to understand for other programmers as well as making it harder since"
						+ " this number will have to be changed in in all the places that it's used.")
				.create(comp, SWT.WRAP | SWT.V_SCROLL);

		link.requestLayout();
		//		"A magic number is a numeric literal that is used in the middle of a block of code without any kind of explanation."
		//		+ " A number in isolation can be difficult for other programmers to understand because it provides no context. If this value is also duplicated, it becomes"
		//		+ " harder to update the code because it needs to be replaced in multiple places."
	}

	@Override
	protected void generateMark(IClassWidget widget, Composite comp, int style) {
		for (IProgramElement el : occurrences) {
			Color cyan = Display.getDefault().getSystemColor(SWT.COLOR_CYAN);
			IWidget w = IJavardiseService.getWidget(el);
			if(w != null) {
				ICodeDecoration<Canvas> d = w.addMark(cyan);
				d.show();
				getDecorations().add(d);
			}
		}
	}

}
