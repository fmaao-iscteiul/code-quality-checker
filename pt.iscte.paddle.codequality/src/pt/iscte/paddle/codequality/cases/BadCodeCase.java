package pt.iscte.paddle.codequality.cases;

import java.util.EnumSet;
import java.util.Set;

import pt.iscte.paddle.javali.translator.ElementLocation;
import pt.iscte.paddle.model.IBlock;

public abstract class BadCodeCase {
	
	public enum Category { EMPTY_SELECTION, EMPTY_ALTERNATIVE, EMPTY_LOOP, DUPLICATE, DEAD_CODE, FAULTY_BOOLEAN_CHECK }
	public final Category category;
	public String explanation;
	public IBlock block;
	public ElementLocation location;
	
	BadCodeCase(Category category, ElementLocation location, String explanation){
		this.category = category;
		this.explanation = explanation;
		this.location = location;
	}
	
	public String getExplanation() {
		return explanation;
	}
	
	public ElementLocation getBlockLocation() {
		return location;
	}
	
	public Category getCaseTypes() {
		return category;
	}
	

}
