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
import pt.iscte.paddle.codequality.misc.Classification;
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
		super(Category.EMPTY_SELECTION, Classification.SERIOUS, branch);
	}

	@Override
	public void generateComponent(IClassWidget widget, Composite comp, int style) {
		generateMark(widget, comp, style);
		generateExplanation(widget, comp, style);
	}

	@Override
	protected void generateMark(IClassWidget widget, Composite comp, int style) {
		IWidget w = generateElementWidget(element);
		if(w != null) {
			ICodeDecoration<Canvas> d = w.addMark(getColor());
			d.show();
			getDecorations().add(d);
			ICodeDecoration<Text> d2 = w.addNote("Nothing happens inside \n this block", ICodeDecoration.Location.RIGHT);
			d2.show();
			getDecorations().add(d2);
		}
	}
	
	@Override
	protected void generateExplanation(IClassWidget widget, Composite comp, int style) {
		IWidget w = IJavardiseService.getWidget(super.element);
		if(w != null) {
			Link link = new HyperlinkedText(null)
					.words("The highlighted if block has no actions inside. \n ")
					.words("\n - If the condition ").link(((ISelection) element).getGuard().toString(), ()->{})
					.words(" is true, nothing will happen.")
					.words("\n - Empty code blocks don't add actions to the program execution.")
					.words("\n - You should avoid empty blocks by adding some logic, or removing them.")
					.create(comp, SWT.WRAP | SWT.V_SCROLL);

			link.requestLayout();
		}
	}

}
