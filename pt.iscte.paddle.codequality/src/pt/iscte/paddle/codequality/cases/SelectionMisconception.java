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

public class SelectionMisconception extends EmptyBranch {

	public SelectionMisconception(String explanation, IProgramElement element) {
		super(Category.SELECTION_MISCONCEPTION, Classification.AVERAGE, element);
	}

	@Override
	protected void generateMark(IClassWidget widget, Composite comp, int style) {
		IWidget w = generateElementWidget(element);
		if(w != null) {
			ICodeDecoration<Canvas> d = w.addMark(getColor());
			d.show();
			getDecorations().add(d);
			ICodeDecoration<Text> d2 = w.addNote("Couln't you use \n the '!' operator?", ICodeDecoration.Location.RIGHT);
			d2.show();
			getDecorations().add(d2);
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
					.words("The code was written in the else block and the if block left empty.")
					.words("\n\n - This means that the reponsible for the actions is the else block.")
					.words("\n - This could be done with a single if condition, using the negation (!) operator.")
					.words("\n - It is never a good practise to leave empty blocks in the code base.")
					.create(comp, SWT.WRAP | SWT.V_SCROLL);

			link.requestLayout();
		}	
	}

}
