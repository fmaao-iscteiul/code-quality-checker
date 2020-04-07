package pt.iscte.paddle.codequality.cases;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
import pt.iscte.paddle.codequality.misc.Explanations;
import pt.iscte.paddle.javardise.service.IClassWidget;
import pt.iscte.paddle.javardise.service.ICodeDecoration;
import pt.iscte.paddle.javardise.service.IJavardiseService;
import pt.iscte.paddle.javardise.util.HyperlinkedText;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IProcedureCall;
import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.model.cfg.INode;

public class DuplicateMethodCall extends BadCodeCase {
	
	List<IProgramElement> duplicates;

	public DuplicateMethodCall(Category category, Collection<INode> duplicatesList) {
		super(category, Explanations.DUPLICATE_METHOD_CALL);
		this.duplicates = new ArrayList<IProgramElement>();
		duplicatesList.forEach(node -> this.duplicates.add(node.getElement()));
	}
	
	@Override
	public void generateComponent(IClassWidget widget, org.eclipse.swt.widgets.Composite comp, int style) {
		this.generateMark(widget, comp, style, duplicates);
		this.generateExplanation(widget, comp, style);
	}
	
	@Override
	protected void generateExplanation(IClassWidget widget, Composite comp, int style) {
		Color blue = Display.getDefault().getSystemColor(SWT.COLOR_BLUE);
		IProcedureCall call = (IProcedureCall) duplicates.get(0);
		Link link = new HyperlinkedText(null)
				.words("The method call ")
				.link(call.toString(), () -> {
					ICodeDecoration<Canvas> d0 = widget.getProcedure((IProcedure) call.getProcedure()).getMethodName().addMark(blue);
					d0.show();
					super.getDecorations().add(d0);
				})
				.words(" was called more than once, without changing the value of any of it's parameters. It is being called as a ")
				.link("void", () -> {})
				.words(" method, but it's return is of the type ")
				.link(call.getProcedure().getReturnType().toString(), () -> {
					IJavardiseService.getWidget(call.getProcedure()).addMark(blue).show();
					ICodeDecoration<Text> d0 = widget.getProcedure((IProcedure) call.getProcedure()).getReturnType().addNote("Not void!", ICodeDecoration.Location.TOP);
					ICodeDecoration<Label> d1 = widget.getProcedure((IProcedure) call.getProcedure()).getReturnType()
							.addImage(new Image(Display.getDefault(), "arrow.png"), ICodeDecoration.Location.LEFT);
					d0.show();
					d1.show();
					getDecorations().add(d0);
					getDecorations().add(d1);
				})
				.words(". This can turn the code confusing.")
				.create(comp, SWT.WRAP | SWT.V_SCROLL);

		link.requestLayout();
	}

}
