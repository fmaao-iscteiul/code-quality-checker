package pt.iscte.paddle.codequality.cases;


import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.javardise.Decoration;
import pt.iscte.paddle.javardise.MarkerService;
import pt.iscte.paddle.javardise.MarkerService.Mark;
import pt.iscte.paddle.model.IProgramElement;

public abstract class BadCodeCase {
	
	public final Category category;
	public String explanation;
	public IProgramElement element;
	
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
	
	public Category getCaseCategory() {
		return category;
	}
	
	public IProgramElement getElement() {
		return element;
	}
	
	public void generateComponent(Display display, Composite comp, int style) {
		this.generateMark(display, comp, style, element);
	}
	
	protected void generateMark(Display display, Composite comp, int style, IProgramElement element) {
		Mark mark = MarkerService.mark(new Color (display, 255, 0, 0), element);
		Decoration d = MarkerService.addDecoration(element, "Magic Number", Decoration.Location.RIGHT);
	}
	
}
