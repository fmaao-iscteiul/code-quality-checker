package pt.iscte.paddle.quality.issues;

import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.quality.cases.base.SingleOcurrenceIssue;
import pt.iscte.paddle.quality.misc.Classification;
import pt.iscte.paddle.quality.misc.IssueType;

public class UselessVariableAssignment extends SingleOcurrenceIssue {

	public UselessVariableAssignment(IProgramElement occurrence) {
		super(IssueType.USELESS_CODE, Classification.SERIOUS, occurrence);
	}

}
