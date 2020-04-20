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
import pt.iscte.paddle.codequality.misc.Classification;
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
		super(category, Classification.SERIOUS);
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
		IProcedureCall call = (IProcedureCall) duplicates.get(0);
		Link link = new HyperlinkedText(null)
				.words("The function ")
				.link(call.toString(), () -> {
					ICodeDecoration<Canvas> d = IJavardiseService.getWidget(call.getProcedure()).addMark(getColor());
					d.show();
					getDecorations().add(d);
				})
				.words(" was called more than once. \n\n  - Its arguments didn't change values between calls, which means that the result will be the same.")
				.words("\n - There is no point on having two " + call + " calls that return the exact same result.")
				.create(comp, SWT.WRAP | SWT.V_SCROLL);

		link.requestLayout();
	}

}
