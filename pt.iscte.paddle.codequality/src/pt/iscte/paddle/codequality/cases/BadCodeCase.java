package pt.iscte.paddle.codequality.cases;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.javardise.Decoration;
import pt.iscte.paddle.javardise.MarkerService;
import pt.iscte.paddle.javardise.MarkerService.Mark;
import pt.iscte.paddle.javardise.TextWidget;
import pt.iscte.paddle.model.IProgramElement;

public abstract class BadCodeCase {

	public final Category category;
	public String explanation;
	public IProgramElement element;

	private List<Mark> marks = new ArrayList<Mark>();
	private List<Decoration> decorations = new ArrayList<Decoration>();
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

	public Category getCaseCategory() {
		return category;
	}

	public IProgramElement getElement() {
		return element;
	}

	public void generateComponent(Display display, Composite comp, int style) {
		this.generateMark(display, comp, style, element);
		this.generateExplanation(comp, style);
	}

	protected void generateMark(Display display, Composite comp, int style, IProgramElement element) {
		this.marks.add(MarkerService.mark(new Color (display, 255, 0, 0), element));
	}
	
	protected void generateMark(Display display, Composite comp, int style, Iterable<IProgramElement> elements) {
		this.marks.add(MarkerService.mark(new Color (display, 255, 0, 0), elements));
	}
	
	protected void generateExplanation(Composite comp, int style) {
		text = new Text(comp, style);
		if(text != null) text.setText(getExplanation());
	}

	public void hideAll() {
		this.marks.forEach(mark -> mark.unmark());
		this.decorations.forEach(decoration -> decoration.hide());
		if(this.text != null) this.text.dispose();
	}

	public List<Decoration> getDecorations() {
		return decorations;
	}
	public List<Mark> getMarks() {
		return marks;
	}

}
