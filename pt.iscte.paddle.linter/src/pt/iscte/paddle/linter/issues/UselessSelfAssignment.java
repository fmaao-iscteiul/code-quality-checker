package pt.iscte.paddle.linter.issues;

import pt.iscte.paddle.linter.cases.base.SingleOcurrenceIssue;
import pt.iscte.paddle.linter.misc.Classification;
import pt.iscte.paddle.linter.misc.IssueType;
import pt.iscte.paddle.model.IProgramElement;

public class UselessSelfAssignment extends SingleOcurrenceIssue {

	public UselessSelfAssignment(IProgramElement occurrence) {
		super(IssueType.FAULTY_ASSIGNMENT, Classification.AVERAGE, occurrence);
	}

}
