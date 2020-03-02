package pt.iscte.paddle.codequality.cases;

import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.model.IControlStructure;

public class SelectionMisconception extends EmptyBranch {

	public SelectionMisconception(String explanation, IControlStructure element) {
		super(Category.SELECTION_MISCONCEPTION, explanation, element);
	}

}
