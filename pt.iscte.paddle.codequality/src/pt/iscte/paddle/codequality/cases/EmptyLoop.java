package pt.iscte.paddle.codequality.cases;

import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.model.IControlStructure;

public class EmptyLoop extends EmptyBranch {

	public EmptyLoop(Category category, String explanation, IControlStructure branch) {
		super(Category.EMPTY_LOOP, explanation, branch);
	}

}
