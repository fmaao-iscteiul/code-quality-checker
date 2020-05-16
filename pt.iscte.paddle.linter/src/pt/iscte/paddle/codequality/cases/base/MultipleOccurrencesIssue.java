package pt.iscte.paddle.codequality.cases.base;

import java.util.ArrayList;
import java.util.List;

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

public class MultipleOccurrencesIssue extends QualityIssue {

	protected List<IProgramElement> occurrences = new ArrayList<IProgramElement>();

	protected MultipleOccurrencesIssue(IssueType category, Classification classification, IProgramElement... occ) {
		super(category, classification);
		for (IProgramElement occurence : occ) {
			this.occurrences.add(occurence);
		}
	}
	
	protected MultipleOccurrencesIssue(IssueType category, Classification classification, List<IProgramElement> occ) {
		super(category, classification);
		for (IProgramElement occurence : occ) {
			this.occurrences.add(occurence);
		}
	}
	
	public void addOccurence(IProgramElement occurrence) {
		occurrences.add(occurrence);
	}

	public List<IProgramElement> getOccurences() {
		return occurrences;
	}

	@Override
	protected void generateExplanation(IClassWidget widget, Composite comp, int style) {
		Link link = new HyperlinkedText(null).words("").create(comp, SWT.WRAP | SWT.V_SCROLL);
		link.requestLayout();
	}

	@Override
	protected void generateMark(IClassWidget widget, Composite comp, int style) {
		for (IProgramElement el : occurrences) {
			System.out.println(el);
			IWidget w = IJavardiseService.getWidget(el);
			if(w != null) {
				ICodeDecoration<Canvas> d = w.addMark(getColor());
				getDecorations().add(d);
				d.show();
			}
		}
	}

}
