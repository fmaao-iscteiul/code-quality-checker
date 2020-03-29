package pt.iscte.paddle.codequality.cases;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;

import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.javardise.service.ICodeDecoration;
import pt.iscte.paddle.javardise.service.IJavardiseService;
import pt.iscte.paddle.javardise.service.IWidget;
import pt.iscte.paddle.javardise.util.HyperlinkedText;
import pt.iscte.paddle.model.IProgramElement;

public abstract class BadCodeCase {

	public final Category category;
	public String explanation;
	public IProgramElement element;

	private List<ICodeDecoration> codeDecorations = new ArrayList<ICodeDecoration>();
	private Text text;

	BadCodeCase(Category category, String explanation, IProgramElement element){
		this.category = category;
		this.explanation = explanation;
		this.element = element;
	}

	public BadCodeCase(Category category, String explanation) {
		this.category = category;
		this.explanation = explanation;
	}

	public String getExplanation() {
		return explanation;
	}
	
	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public Category getCaseCategory() {
		return category;
	}

	public IProgramElement getElement() {
		return element;
	}

	public void generateComponent(Display display, Composite comp, int style) {
		this.generateMark(display, comp, style);
		this.generateDecoration(display, comp, style);
		this.generateExplanation(display, comp, style);
	}

	protected void generateMark(Display display, Composite comp, int style) {
		IWidget w = IJavardiseService.getWidget(element);
		if(w != null) {
			ICodeDecoration d = w.addMark(new Color (display, 255, 0, 0));
			this.codeDecorations.add(d);
			d.show();
		}
	}

	protected void generateMark(Display display, Composite comp, int style, Iterable<IProgramElement> elements) {
		elements.forEach(el -> {
			IWidget w = IJavardiseService.getWidget(el);
			if(w != null) {
				ICodeDecoration d = w.addMark(new Color (display, 155, 0, 0));
				this.codeDecorations.add(d);
				d.show();
			}
		});		
	}

	protected void generateExplanation(Display display, Composite comp, int style) {
		Color blue = Display.getDefault().getSystemColor(SWT.COLOR_BLUE);
		Link link = new HyperlinkedText(null).words(getExplanation()).create(comp, SWT.WRAP | SWT.V_SCROLL);
		link.requestLayout();
	}
	
	protected void generateDecoration(Display display, Composite comp, int style) {}

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
