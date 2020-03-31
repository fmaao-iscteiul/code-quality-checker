package pt.iscte.paddle.codequality.cases;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.javardise.service.IClassWidget;
import pt.iscte.paddle.javardise.service.ICodeDecoration;
import pt.iscte.paddle.javardise.service.IJavardiseService;
import pt.iscte.paddle.javardise.service.IWidget;
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
		super.generateExplanation(widget, comp, style);
		generateMark(widget, comp, style);
	}

	@Override
	protected void generateMark(IClassWidget widget, Composite comp, int style) {
		boolean first = true;
		for (IProgramElement el : occurrences) {
			Color cyan = Display.getDefault().getSystemColor(SWT.COLOR_CYAN);
			IWidget w = IJavardiseService.getWidget(el);
			if(w != null) {
				ICodeDecoration<Canvas> d = w.addMark(cyan);
				d.show();
				getDecorations().add(d);
				if(first) {
					ICodeDecoration<Text> d2 = w.addNote("What does this \n number represent?", ICodeDecoration.Location.RIGHT);
					d2.show();
					getDecorations().add(d2);
					first = false;
				}
			}
		}
	}

}
