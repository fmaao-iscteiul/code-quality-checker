package pt.iscte.paddle.codequality.cases;

import java.util.ArrayList;
import java.util.List;

import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.model.IStatement;

public class MagicNumbers extends BadCodeCase{

	List<IStatement> assignments;

	MagicNumbers(Category category, String explanation, IStatement magicNumber) {
		super(Category.FAULTY_ASSIGNMENT, explanation);
		this.assignments = new ArrayList<IStatement>();
		this.assignments.add(magicNumber);
	}
	
	public List<IStatement> getAssignments() {
		return assignments;
	}

}
