package pt.iscte.paddle.codequality.cases;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;

import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.javardise.Decoration;
import pt.iscte.paddle.javardise.MarkerService;
import pt.iscte.paddle.javardise.util.HyperlinkedText;
import pt.iscte.paddle.model.IProcedureCall;

public class FaultyProcedureCall extends BadCodeCase {

	public FaultyProcedureCall(String explanation, IProcedureCall element) {
		super(Category.FAULTY_METHOD_CALL, explanation, element);
	}
	
	@Override
	protected void generateExplanation(Display display, Composite comp, Link textWidget, int style) {
		
		Color blue = Display.getDefault().getSystemColor(SWT.COLOR_BLUE);
		Link link = new HyperlinkedText(e -> MarkerService.mark(blue, e)).create(comp, SWT.WRAP | SWT.V_SCROLL);
		
		link.requestLayout();
		
	}

	protected void generateDecoration(Display display, Composite comp, int style) {
		Decoration d = MarkerService.addDecoration(((IProcedureCall) this.element), p -> {
			Label l = new Label(p, SWT.NONE);
			Image img = new Image(display, "arrow.png");
			l.setImage(img);
			return l;
		}, Decoration.Location.LEFT);
		if(d != null) d.show();
		super.getDecorations().add(d);
	}
}
