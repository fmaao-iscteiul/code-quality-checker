package pt.iscte.paddle.codequality.cases;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
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
import pt.iscte.paddle.model.IControlStructure;

public class EmptyLoop extends EmptyBranch {

	public EmptyLoop(Category category, String explanation, IControlStructure branch) {
		super(Category.EMPTY_LOOP, explanation, branch);
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

	@Override
	protected void generateExplanation(IClassWidget widget, Composite comp, int style) {
		IWidget w = IJavardiseService.getWidget(super.element);
		if(w != null) {
			Link link = new HyperlinkedText(null)
					.words("The highlighted loop block is empty: \n\n ")
					.link(element.toString(), () -> {
						ICodeDecoration<Text> d2 = w.addNote("Why is this loop empty?", ICodeDecoration.Location.RIGHT);
						d2.show();
						getDecorations().add(d2);
					})
					.words("\n - Empty code blocks are useless while running a program "
							+ "because they don't add any actions to it, ever, and they make the code messy and confusing.")
					.create(comp, SWT.WRAP | SWT.V_SCROLL);

			link.requestLayout();
		}
	}
}
