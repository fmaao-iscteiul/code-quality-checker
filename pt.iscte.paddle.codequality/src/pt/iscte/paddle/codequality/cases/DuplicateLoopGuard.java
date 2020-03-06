package pt.iscte.paddle.codequality.cases;

import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.model.IProgramElement;

public class DuplicateLoopGuard extends BadCodeCase{

	public DuplicateLoopGuard(IProgramElement element) {
		super(Category.DUPLICATE_SELECTION_GUARD, "Duplicate Loop Guard.", element);
	}

}
