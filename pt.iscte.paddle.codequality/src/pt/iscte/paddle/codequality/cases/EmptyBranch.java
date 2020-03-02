package pt.iscte.paddle.codequality.cases;

import pt.iscte.paddle.javali.translator.ElementLocation;
import pt.iscte.paddle.model.IControlStructure;
import pt.iscte.paddle.model.IExpression;

public class EmptyBranch extends BadCodeCase{

	private final IControlStructure branch;

	public EmptyBranch(Category category, ElementLocation location, String explanation, IControlStructure branch) {
		super(category, location, explanation);
		this.branch = branch;
	}
	
	public IControlStructure getBranch() {
		return branch;
	}
}
