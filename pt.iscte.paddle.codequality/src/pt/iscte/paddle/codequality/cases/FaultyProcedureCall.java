package pt.iscte.paddle.codequality.cases;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;

import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.javardise.service.ICodeDecoration;
import pt.iscte.paddle.javardise.service.IDeclarationWidget;
import pt.iscte.paddle.javardise.service.IJavardiseService;
import pt.iscte.paddle.javardise.service.IWidget;
import pt.iscte.paddle.javardise.util.HyperlinkedText;
import pt.iscte.paddle.model.IProcedureCall;

public class FaultyProcedureCall extends BadCodeCase {

	public FaultyProcedureCall(String explanation, IProcedureCall element) {
		super(Category.FAULTY_METHOD_CALL, explanation, element);
	}
	
//	@Override
//	protected void generateExplanation(Display display, Composite comp, int style) {
//		Color blue = Display.getDefault().getSystemColor(SWT.COLOR_BLUE);
//		IWidget w = IJavardiseService.getWidget(super.element);
//		ICodeDecoration dec = w.addMark(blue);
//		dec.show();
//		if(w != null) {
//			Link link = new HyperlinkedText(e -> w.addMark(blue)).create(comp, SWT.WRAP | SWT.V_SCROLL);
//			link.requestLayout();
//		}
//	}
//
//	protected void generateDecoration(Display display, Composite comp, int style) {
//		Color blue = Display.getDefault().getSystemColor(SWT.COLOR_BLUE);
//		Image img = new Image(display, "arrow.png");
//		IWidget w = IJavardiseService.getWidget(element);
//		if(w != null) {
//			ICodeDecoration d = w.addImage(img, ICodeDecoration.Location.LEFT);
//			d.show();
//			super.getDecorations().add(d);
//		}
//	}
}
