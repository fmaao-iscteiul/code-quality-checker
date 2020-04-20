package pt.iscte.paddle.codequality.cases;

import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.codequality.misc.Classification;
import pt.iscte.paddle.model.IProgramElement;

public class UselessAssignment extends BadCodeCase {

	UselessAssignment(Category category, IProgramElement element) {
		super(Category.USELESS_CODE, Classification.AVERAGE, element);
	}

}
