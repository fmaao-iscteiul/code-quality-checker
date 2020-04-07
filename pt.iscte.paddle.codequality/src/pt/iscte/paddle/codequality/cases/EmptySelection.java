package pt.iscte.paddle.codequality.cases;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;

import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.javardise.service.IClassWidget;
import pt.iscte.paddle.javardise.service.ICodeDecoration;
import pt.iscte.paddle.javardise.service.IJavardiseService;
import pt.iscte.paddle.javardise.service.IWidget;
import pt.iscte.paddle.javardise.util.HyperlinkedText;
import pt.iscte.paddle.model.IControlStructure;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IProcedureCall;
import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.model.ISelection;

public class EmptySelection extends EmptyBranch {

	public EmptySelection(String explanation, IProgramElement branch) {
		super(Category.EMPTY_SELECTION, explanation, branch);
	}

	@Override
	public void generateComponent(IClassWidget widget, Composite comp, int style) {
		generateMark(widget, comp, style);
		generateExplanation(widget, comp, style);
	}

	@Override
	protected void generateMark(IClassWidget widget, Composite comp, int style) {
		Color blue = Display.getDefault().getSystemColor(SWT.COLOR_BLUE);
		IWidget w = IJavardiseService.getWidget(element);
		if(w != null) {
			ICodeDecoration<Canvas> d = w.addMark(blue);
			d.show();
			getDecorations().add(d);
		}
	}
	
	@Override
	protected void generateExplanation(IClassWidget widget, Composite comp, int style) {
		IWidget w = IJavardiseService.getWidget(super.element);
		if(w != null) {
			Link link = new HyperlinkedText(null)
					.words("The highlighted if block is empty: \n\n ")
					.link(element.toString(), ()-> {
						ICodeDecoration<Text> d2 = w.addNote("No actions inside \n this block", ICodeDecoration.Location.RIGHT);
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
