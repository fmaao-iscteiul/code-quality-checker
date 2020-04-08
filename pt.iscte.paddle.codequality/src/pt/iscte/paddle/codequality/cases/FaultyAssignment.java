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

public class FaultyAssignment extends BadCodeCase{

	public FaultyAssignment(String explanation, IProgramElement element) {
		super(Category.FAULTY_ASSIGNMENT, explanation, element);
	}


	@Override
	protected void generateMark(IClassWidget widget, Composite comp, int style) {
		Color cyan = Display.getDefault().getSystemColor(SWT.COLOR_DARK_MAGENTA);
		IWidget w = IJavardiseService.getWidget(element);
		if(w != null) {
			ICodeDecoration<Canvas> d = w.addMark(cyan);
			d.show();
			getDecorations().add(d);
		}
	}

	/**
	 * 
	 *
	 * "The highlighted variable was assigned to itself. This leads to the assignment of a value that"
			+ " the variable already had. With this being, it is considered an useless assignment."
	 */
	
	@Override
	protected void generateExplanation(IClassWidget widget, Composite comp, int style) {
		IWidget w = IJavardiseService.getWidget(super.element);
		if(w != null) {
			Link link = new HyperlinkedText(null)
					.words("The assignment ").link(element.toString(), () -> {
						ICodeDecoration<Text> d2 = w.addNote("What does this actually do?", ICodeDecoration.Location.RIGHT);
						d2.show();
						getDecorations().add(d2);
					})
					.words(" means that the variable was assigned to itself. ")
					.words("\n\n - This means that the variable was assigned with the same value that it already had.")
					.words("\n - It is considered an useless assignment, because it has no impact in the program.")
					.create(comp, SWT.WRAP | SWT.V_SCROLL);

			link.requestLayout();
		}
	}
}
