package pt.iscte.paddle.codequality.cases;

import java.util.EnumSet;
import java.util.Set;

import pt.iscte.paddle.javali.translator.ElementLocation;
import pt.iscte.paddle.model.IBlock;
import pt.iscte.paddle.model.IBlockElement;

public abstract class BadCodeCase {
	
	public enum Category { EMPTY_SELECTION, EMPTY_ALTERNATIVE, EMPTY_LOOP, DUPLICATE_CODE, DEAD_CODE, NESTED_CODE, FAULTY_BOOLEAN_CHECK }
	
	public final Category category;
	public String explanation;
	public IBlockElement element;
	public ElementLocation location;
	
	BadCodeCase(Category category, ElementLocation location, String explanation, IBlockElement element){
		this.category = category;
		this.explanation = explanation;
		this.location = location;
		this.element = element;
	}
	
	public String getExplanation() {
		return explanation;
	}
	
	public ElementLocation getBlockLocation() {
		return location;
	}
	
	public Category getCaseCategory() {
		return category;
	}
	
	public IBlockElement getBlock() {
		return element;
	}
	

}
