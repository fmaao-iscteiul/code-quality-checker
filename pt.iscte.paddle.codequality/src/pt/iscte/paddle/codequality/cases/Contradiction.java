package pt.iscte.paddle.codequality.cases;

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

public class Contradiction extends BadCodeCase {

	public Contradiction(String explanation, IProgramElement element) {
		super(Category.CONTRADICTION, Classification.SERIOUS, element);
	}
	
	@Override
	protected void generateMark(IClassWidget widget, Composite comp, int style) {
		IWidget w = generateElementWidget(element);
		if(w != null) {
			ICodeDecoration<Canvas> d = w.addMark(getColor());
			d.show();
			getDecorations().add(d);
			ICodeDecoration<Text> d2 = w.addNote("This code will \n never run!", ICodeDecoration.Location.RIGHT);
			d2.show();
			getDecorations().add(d2);
		}
	}
	
	@Override
	protected void generateExplanation(IClassWidget widget, Composite comp, int style) {
		IWidget w = IJavardiseService.getWidget(super.element);
		if(w != null) {
			Link link = new HyperlinkedText(null)
					.words("The condition ").link(element.toString(), () -> {
						
					})
					.words(" represents a contradiction case. ")
					.words("\n\n - This means that this condition will allways be avaliated as false.")
					.words("\n - The program will never execute the code inside this condition, which is useless.")
					.create(comp, SWT.WRAP | SWT.V_SCROLL);

			link.requestLayout();
		}
	}
}
