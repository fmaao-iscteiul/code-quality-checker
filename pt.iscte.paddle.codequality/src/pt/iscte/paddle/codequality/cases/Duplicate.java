package pt.iscte.paddle.codequality.cases;

import pt.iscte.paddle.javali.translator.ElementLocation;
import pt.iscte.paddle.model.IBlock;
import pt.iscte.paddle.model.IVariableAssignment;

public class Duplicate extends BadCodeCase {
	
	IVariableAssignment assignment;

	public Duplicate(Category category, ElementLocation location, String explanation, IVariableAssignment assignment) {
		super(category, location, explanation, assignment);
	}

}
