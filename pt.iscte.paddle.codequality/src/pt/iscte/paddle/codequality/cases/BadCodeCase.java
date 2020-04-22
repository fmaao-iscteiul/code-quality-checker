package pt.iscte.paddle.codequality.cases;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;

import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.codequality.misc.Classification;
import pt.iscte.paddle.javardise.service.IClassWidget;
import pt.iscte.paddle.javardise.service.ICodeDecoration;
import pt.iscte.paddle.javardise.service.IJavardiseService;
import pt.iscte.paddle.javardise.service.IWidget;
import pt.iscte.paddle.javardise.util.HyperlinkedText;
import pt.iscte.paddle.model.IProgramElement;

public abstract class BadCodeCase {

	private final Category category;
	private final Classification classification;

	public IProgramElement element;

	private List<ICodeDecoration> codeDecorations = new ArrayList<ICodeDecoration>();
	private Text text;

	BadCodeCase(Category category, Classification classification, IProgramElement element){
		this.category = category;
		this.classification = classification;
		this.element = element;
	}

	public BadCodeCase(Category category, Classification classification) {
		this.category = category;
		this.classification = classification;
	}

	public Category getCaseCategory() {
		return category;
	}
	
	public Classification getClassification() {
		return classification;
	}

	public IProgramElement getElement() {
		return element;
	}

	public void generateComponent(IClassWidget widget, Composite comp, int style) {
		this.generateMark(widget, comp, style);
		this.generateExplanation(widget, comp, style);
	}

	protected void generateMark(IClassWidget widget, Composite comp, int style) {
		IWidget w = IJavardiseService.getWidget(element);
		if(w != null) {
			ICodeDecoration<Canvas> d = w.addMark(new Color (Display.getDefault(), 255, 0, 0));
			this.codeDecorations.add(d);
			d.show();
		}
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
	
	protected IWidget generateElementWidget(IProgramElement element) {
		return IJavardiseService.getWidget(element);
	}

	protected void generateMark(IClassWidget widget, Composite comp, int style, List<IProgramElement> elements) {
		for (IProgramElement el : elements) {
			System.out.println(el);
			IWidget w = IJavardiseService.getWidget(el);
			if(w != null) {
				ICodeDecoration<Canvas> d = w.addMark(getColor());
				this.codeDecorations.add(d);
				d.show();
			}
		}
	}

	protected void generateExplanation(IClassWidget widget, Composite comp, int style) {
		Link link = new HyperlinkedText(null).words("").create(comp, SWT.WRAP | SWT.V_SCROLL);
		link.requestLayout();
	}

	public void hideAll() {
		this.codeDecorations.forEach(mark -> {
			mark.hide();
		});
		if(this.text != null) this.text.dispose();
	}

	public List<ICodeDecoration> getDecorations() {
		return codeDecorations;
	}

	public void addDecoration(ICodeDecoration d) {
		this.codeDecorations.add(d);
	}

}
