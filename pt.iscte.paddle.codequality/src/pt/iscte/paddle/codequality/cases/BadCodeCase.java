package pt.iscte.paddle.codequality.cases;

import pt.iscte.paddle.codequality.misc.Category;
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
	

}
