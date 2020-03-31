package pt.iscte.paddle.codequality.cases;

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

public class Tautology extends BadCodeCase {

	public Tautology(String explanation, IProgramElement element) {
		super(Category.TALTOLOGY, explanation, element);
	}
	
	@Override
	protected void generateMark(IClassWidget widget, Composite comp, int style) {
		Color cyan = Display.getDefault().getSystemColor(SWT.COLOR_CYAN);
		IWidget w = IJavardiseService.getWidget(element);
		if(w != null) {
			ICodeDecoration<Canvas> d = w.addMark(cyan);
			ICodeDecoration<Text> d2 = w.addNote("Won't this execute everytime?", ICodeDecoration.Location.RIGHT);
			d.show();
			d2.show();
			getDecorations().add(d);
			getDecorations().add(d2);
		}
	}
}
