package pt.iscte.paddle.linter.issues;

import pt.iscte.paddle.linter.cases.base.SingleOcurrenceIssue;
import pt.iscte.paddle.linter.misc.Classification;
import pt.iscte.paddle.linter.misc.IssueType;
import pt.iscte.paddle.model.IExpression;

public class BooleanCheck extends SingleOcurrenceIssue {


	public BooleanCheck(String explanation, IExpression selectionGuard) {
		super(IssueType.FAULTY_BOOLEAN_CHECK, Classification.LIGHT, selectionGuard);
	}

}
