package pt.iscte.paddle.codequality.cases;

import java.util.Objects;

import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.javali.translator.ElementLocation;
import pt.iscte.paddle.model.IBlockElement;
import pt.iscte.paddle.model.IExpression;
import pt.iscte.paddle.model.IProgramElement;

public class BooleanCheck extends BadCodeCase {

	
	private IExpression expression;
	
	public BooleanCheck(String explanation, IExpression selectionGuard) {
		super(Category.FAULTY_BOOLEAN_CHECK, explanation, selectionGuard);
		this.expression = Objects.requireNonNull(selectionGuard);
	}
}
