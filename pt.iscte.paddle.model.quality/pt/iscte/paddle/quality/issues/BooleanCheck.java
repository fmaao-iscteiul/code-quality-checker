package pt.iscte.paddle.quality.issues;

import pt.iscte.paddle.model.IExpression;
import pt.iscte.paddle.quality.cases.base.SingleOcurrenceIssue;
import pt.iscte.paddle.quality.misc.Classification;
import pt.iscte.paddle.quality.misc.IssueType;

public class BooleanCheck extends SingleOcurrenceIssue {


	public BooleanCheck(String explanation, IExpression selectionGuard) {
		super(IssueType.FAULTY_BOOLEAN_CHECK, Classification.LIGHT, selectionGuard);
	}

}
