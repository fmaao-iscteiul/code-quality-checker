package pt.iscte.paddle.codequality.cases;

import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.codequality.misc.Classification;
import pt.iscte.paddle.model.IControlStructure;
import pt.iscte.paddle.model.IProgramElement;

public class EmptyBranch extends BadCodeCase{

	private final IProgramElement branch;

	public EmptyBranch(Category category, Classification classification, IProgramElement branch) {
		super(category, classification, branch);
		this.branch = branch;
	}
	
	public IProgramElement getBranch() {
		return branch;
	}
}
