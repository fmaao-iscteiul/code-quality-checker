package pt.iscte.paddle.codequality.cases.base;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;

import pt.iscte.paddle.codequality.misc.IssueType;
import pt.iscte.paddle.codequality.misc.Classification;
import pt.iscte.paddle.javardise.service.IClassWidget;
import pt.iscte.paddle.javardise.service.ICodeDecoration;
import pt.iscte.paddle.javardise.service.IJavardiseService;
import pt.iscte.paddle.javardise.service.IWidget;
import pt.iscte.paddle.javardise.util.HyperlinkedText;
import pt.iscte.paddle.model.IProgramElement;

public abstract class QualityIssue {

	private final IssueType type;
	private final Classification classification;


	private List<ICodeDecoration> codeDecorations = new ArrayList<ICodeDecoration>();

	public QualityIssue(IssueType category, Classification classification) {
		this.type = category;
		this.classification = classification;
	}

	public IssueType getIssueType() {
		return type;
	}
	
	public Classification getClassification() {
		return classification;
	}

	public void generateComponent(IClassWidget widget, Composite comp, int style) {
		this.generateMark(widget, comp, style);
		this.generateExplanation(widget, comp, style);
	}

	abstract protected void generateMark(IClassWidget widget, Composite comp, int style);
	abstract protected void generateExplanation(IClassWidget widget, Composite comp, int style);

	protected IWidget generateElementWidget(IProgramElement element) {
		return IJavardiseService.getWidget(element);
	}
	
	protected Color getColor() {
		switch (classification) {
			case AVERAGE: {
				return Display.getDefault().getSystemColor(SWT.COLOR_MAGENTA);
			}
			case LIGHT: {
				return Display.getDefault().getSystemColor(SWT.COLOR_CYAN);
			}
			case SERIOUS: {
				return Display.getDefault().getSystemColor(SWT.COLOR_RED);
			}
		}
		return null;
	}
	

	public void hideAll() {
		this.codeDecorations.forEach(mark -> {
			mark.hide();
		});
	}

	public List<ICodeDecoration> getDecorations() {
		return codeDecorations;
	}

	public void addDecoration(ICodeDecoration d) {
		this.codeDecorations.add(d);
	}

}
