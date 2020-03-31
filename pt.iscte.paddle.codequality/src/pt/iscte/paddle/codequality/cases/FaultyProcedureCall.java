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
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IProcedureCall;

public class FaultyProcedureCall extends BadCodeCase {

	public FaultyProcedureCall(String explanation, IProcedureCall element) {
		super(Category.FAULTY_METHOD_CALL, explanation, element);
	}

	@Override
	protected void generateExplanation(IClassWidget widget, Composite comp, int style) {
		IProcedureCall call = (IProcedureCall) element;
		Color blue = Display.getDefault().getSystemColor(SWT.COLOR_BLUE);
		IWidget w = IJavardiseService.getWidget(super.element);
		if(w != null) {
			Link link = new HyperlinkedText(null)
					.words("A non void method was called has a void one should be. The method ")
					.link(call.toString(), () -> {
						ICodeDecoration<Canvas> d0 = widget.getProcedure((IProcedure) call.getProcedure()).getMethodName().addMark(blue);
						d0.show();
						super.getDecorations().add(d0);
					})
					.words(", returns the type: ")
					.link(call.getProcedure().getReturnType().toString(), () -> {
						Image img = new Image(Display.getDefault(), "arrow.png");
						ICodeDecoration<Canvas> d2 = widget.getProcedure((IProcedure) call.getProcedure()).getReturnType().addMark(blue);
						ICodeDecoration<Text> d3 = widget.getProcedure((IProcedure) call.getProcedure()).getReturnType().addNote("Not void!", ICodeDecoration.Location.TOP);
						ICodeDecoration<Label> d4 = widget.getProcedure((IProcedure) call.getProcedure()).getReturnType().addImage(img,  ICodeDecoration.Location.LEFT);
						d2.show();
						d3.show();
						d4.show();
						super.getDecorations().add(d2);
						super.getDecorations().add(d3);
						super.getDecorations().add(d4);

					})
					.words(". With this being, you should consider if this method should return anything at all, or investigate if it is being used the right way.")
					.create(comp, SWT.WRAP | SWT.V_SCROLL);

			link.requestLayout();
		}
	}

	protected void generateDecoration(IClassWidget widget, Composite comp, int style) {
		IProcedureCall call = (IProcedureCall) element;
		Color blue = Display.getDefault().getSystemColor(SWT.COLOR_BLUE);
		Image img = new Image(Display.getDefault(), "arrow.png");
		IWidget w = IJavardiseService.getWidget(element);
		ICodeDecoration<Label> d = w.addImage(img, ICodeDecoration.Location.LEFT);
		ICodeDecoration<Text> d1 = w.addNote("Doesn't this method \n return something?", ICodeDecoration.Location.RIGHT);
		d.show();
		d1.show();
		super.getDecorations().add(d);
		super.getDecorations().add(d1);
	}
}
