package pt.iscte.paddle.linter.issues;

import pt.iscte.paddle.linter.cases.base.SingleOcurrenceIssue;
import pt.iscte.paddle.linter.misc.Classification;
import pt.iscte.paddle.linter.misc.IssueType;
import pt.iscte.paddle.model.IProgramElement;

public class UselessVariableAssignment extends SingleOcurrenceIssue {

	public UselessVariableAssignment(IProgramElement occurrence) {
		super(IssueType.USELESS_CODE, Classification.SERIOUS, occurrence);
	}

}
