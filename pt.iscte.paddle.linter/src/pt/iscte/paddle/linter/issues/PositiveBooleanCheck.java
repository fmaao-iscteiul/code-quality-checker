package pt.iscte.paddle.linter.issues;

import pt.iscte.paddle.model.IExpression;
import pt.iscte.paddle.model.IProcedure;

public class PositiveBooleanCheck extends BooleanCheck {

	public PositiveBooleanCheck(IProcedure procedure, IExpression selectionGuard) {
		super(procedure, selectionGuard);
	}

}
