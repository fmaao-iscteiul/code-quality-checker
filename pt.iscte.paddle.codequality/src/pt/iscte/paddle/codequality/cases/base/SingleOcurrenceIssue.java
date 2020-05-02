package pt.iscte.paddle.codequality.cases.base;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;

import pt.iscte.paddle.codequality.misc.Classification;
import pt.iscte.paddle.codequality.misc.IssueType;
import pt.iscte.paddle.javardise.service.IClassWidget;
import pt.iscte.paddle.javardise.service.ICodeDecoration;
import pt.iscte.paddle.javardise.service.IJavardiseService;
import pt.iscte.paddle.javardise.service.IWidget;
import pt.iscte.paddle.javardise.util.HyperlinkedText;
import pt.iscte.paddle.model.IProgramElement;

public class SingleOcurrenceIssue extends QualityIssue {
	
	protected IProgramElement occurrence;

	protected SingleOcurrenceIssue(IssueType category, Classification classification, IProgramElement occ) {
		super(category, classification);
		this.occurrence = occ;
	}
	
	public void generateComponent(IClassWidget widget, Composite comp, int style) {
		this.generateMark(widget, comp, style);
		this.generateExplanation(widget, comp, style);
	}

	protected void generateMark(IClassWidget widget, Composite comp, int style) {
		IWidget w = IJavardiseService.getWidget(occurrence);
		if(w != null) {
			ICodeDecoration<Canvas> d = w.addMark(getColor());
			getDecorations().add(d);
			d.show();
		}
	}

	@Override
	protected void generateExplanation(IClassWidget widget, Composite comp, int style) {
		Link link = new HyperlinkedText(null).words("").create(comp, SWT.WRAP | SWT.V_SCROLL);
		link.requestLayout();		
	}

}
