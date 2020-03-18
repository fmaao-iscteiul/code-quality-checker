package pt.iscte.paddle.codequality.cases;

import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.model.IProgramElement;

public class FaultyMethodCall extends BadCodeCase{

	public FaultyMethodCall(String explanation, IProgramElement element) {
		super(Category.FAULTY_METHOD_CALL, explanation, element);
	}

}
