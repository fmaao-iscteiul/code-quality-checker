package pt.iscte.paddle.codequality.cases;

import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.model.IProgramElement;

public class FaultyAssignment extends BadCodeCase{

	public FaultyAssignment(String explanation, IProgramElement element) {
		super(Category.FAULTY_ASSIGNMENT, explanation, element);
	}

}
