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

public class SelectionMisconception extends EmptyBranch {

	public SelectionMisconception(String explanation, IProgramElement element) {
		super(Category.SELECTION_MISCONCEPTION, explanation, element);
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
	 * "The code was written in the else block and the if left empty because the"
			+ " target condition was the opposite written in the if guard. This demonstrates a misconception when in comes to handling conditions."
			+ "A negative condition can be used to fight these empty blocks of code.";
	 */
	@Override
	protected void generateExplanation(IClassWidget widget, Composite comp, int style) {
		IWidget w = IJavardiseService.getWidget(super.element);
		if(w != null) {
			Link link = new HyperlinkedText(null)
					.words("The if condition block is empty and the else isn't: \n\n ")
					.link(element.toString(), () -> {
						ICodeDecoration<Text> d2 = w.addNote("Couln't it be simpler \n and smaller?", ICodeDecoration.Location.RIGHT);
						d2.show();
						getDecorations().add(d2);
					})
					.words("\n - The code was written in the else block and the if condition left empty because the target condition is a negation."
							+ "\n - It is never a good practise to leave empty blocks in the code base."
							+ "\n - There are native operators that were designed to deal with this kind of situations.")
					.create(comp, SWT.WRAP | SWT.V_SCROLL);

			link.requestLayout();
		}	
	}

}
