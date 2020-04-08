package pt.iscte.paddle.codequality.cases;

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
			d.show();
			getDecorations().add(d);
		}
	}
	
	@Override
	protected void generateExplanation(IClassWidget widget, Composite comp, int style) {
		IWidget w = IJavardiseService.getWidget(super.element);
		if(w != null) {
			Link link = new HyperlinkedText(null)
					.words("The condition statement ").link(element.toString(), () -> {
						ICodeDecoration<Text> d2 = w.addNote("Won't this execute everytime?", ICodeDecoration.Location.RIGHT);
						d2.show();
						getDecorations().add(d2);
					})
					.words(" represents a tautology case. ")
					.words("\n\n - This means that this condition will allways be avaliated as true.")
					.words("\n - The program will always execute the code inside this condition, which means that"
							+ " it is not necessary, or wrong.")
					.create(comp, SWT.WRAP | SWT.V_SCROLL);

			link.requestLayout();
		}
	}
}
