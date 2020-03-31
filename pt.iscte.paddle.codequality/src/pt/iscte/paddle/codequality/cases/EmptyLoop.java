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
import pt.iscte.paddle.model.IControlStructure;

public class EmptyLoop extends EmptyBranch {

	public EmptyLoop(Category category, String explanation, IControlStructure branch) {
		super(Category.EMPTY_LOOP, explanation, branch);
	}


	@Override
	public void generateComponent(IClassWidget widget, Composite comp, int style) {
		generateExplanation(widget, comp, style);
		generateMark(widget, comp, style);
	}

	@Override
	protected void generateMark(IClassWidget widget, Composite comp, int style) {
		Color cyan = Display.getDefault().getSystemColor(SWT.COLOR_DARK_MAGENTA);
		IWidget w = IJavardiseService.getWidget(element);
		if(w != null) {
			ICodeDecoration<Canvas> d = w.addMark(cyan);
			d.show();
			getDecorations().add(d);
			ICodeDecoration<Text> d2 = w.addNote("No actions inside \n this block.", ICodeDecoration.Location.RIGHT);
			d2.show();
			getDecorations().add(d2);
		}
	}
}
