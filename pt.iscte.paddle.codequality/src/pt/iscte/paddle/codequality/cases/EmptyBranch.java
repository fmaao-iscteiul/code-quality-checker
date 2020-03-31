package pt.iscte.paddle.codequality.cases;

import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.model.IControlStructure;
import pt.iscte.paddle.model.IProgramElement;

public class EmptyBranch extends BadCodeCase{

	private final IProgramElement branch;

	public EmptyBranch(Category category, String explanation, IProgramElement branch) {
		super(category, explanation, branch);
		this.branch = branch;
	}
	
	public IProgramElement getBranch() {
		return branch;
	}
}
