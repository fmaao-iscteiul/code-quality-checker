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
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IProcedureCall;

public class FaultyProcedureCall extends BadCodeCase {

	public FaultyProcedureCall(String explanation, IProcedureCall element) {
		super(Category.FAULTY_METHOD_CALL, Classification.AVERAGE, element);
	}

	@Override
	protected void generateExplanation(IClassWidget widget, Composite comp, int style) {
		IProcedureCall call = (IProcedureCall) element;
		IWidget w = generateElementWidget(element);
		if(w != null) {
			ICodeDecoration<Text> t = w.addNote("Doesn't this return \n something?", ICodeDecoration.Location.RIGHT);
			t.show();
			getDecorations().add(t);
			Link link = new HyperlinkedText(null)
					.words("The method's return value is not being assigned or used. \n\n - The method ")
					.link(call.toString(), () -> {
						ICodeDecoration<Canvas> d0 = widget.getProcedure((IProcedure) call.getProcedure()).getMethodName().addMark(getColor());
						d0.show();
						super.getDecorations().add(d0);
					})
					.words(" isn't void and its return value is of type ")
					.link(call.getProcedure().getReturnType().toString(), () -> {

						ICodeDecoration<Canvas> d2 = widget.getProcedure((IProcedure) call.getProcedure()).getReturnType().addMark(getColor());
						ICodeDecoration<Text> d3 = widget.getProcedure((IProcedure) call.getProcedure()).getReturnType().addNote("Return not used!", ICodeDecoration.Location.TOP);
						d2.show();
						d3.show();
						super.getDecorations().add(d2);
						super.getDecorations().add(d3);
					})
					.words("\n - If the method's " + call + " return value isn't used, you should consider if there is the need for the method to return it.")
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
