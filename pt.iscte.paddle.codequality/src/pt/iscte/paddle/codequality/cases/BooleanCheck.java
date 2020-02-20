package pt.iscte.paddle.codequality.cases;

import java.util.Objects;

import pt.iscte.paddle.javali.translator.ElementLocation;
import pt.iscte.paddle.model.IExpression;

public class BooleanCheck extends BadCodeCase {

	
	private IExpression guard;
	
	private BooleanCheck(Category category, ElementLocation location, String explanation, IExpression selectionGuard) {
		super(category, location, explanation);
		this.guard = Objects.requireNonNull(selectionGuard);
	}
}
