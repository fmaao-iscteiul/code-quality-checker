package pt.iscte.paddle.linter.issues;

import pt.iscte.paddle.model.IExpression;
import pt.iscte.paddle.model.IProcedure;

public class NegativeBooleanCheck extends BooleanCheck {

	public NegativeBooleanCheck(IProcedure procedure, IExpression selectionGuard) {
		super(procedure, selectionGuard);
	}

}
