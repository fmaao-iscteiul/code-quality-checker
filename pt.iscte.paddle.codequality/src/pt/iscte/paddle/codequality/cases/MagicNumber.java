package pt.iscte.paddle.codequality.cases;

import java.util.ArrayList;
import java.util.List;

import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.model.IStatement;

public class MagicNumber extends BadCodeCase{

	List<IStatement> assignments;

	public MagicNumber(String explanation, IStatement magicNumber) {
		super(Category.FAULTY_ASSIGNMENT, explanation);
		this.assignments = new ArrayList<IStatement>();
		this.assignments.add(magicNumber);
	}
	
	public List<IStatement> getAssignments() {
		return assignments;
	}
	
	public void addAssignment(IStatement statement) {
		this.assignments.add(statement);
	}

}
