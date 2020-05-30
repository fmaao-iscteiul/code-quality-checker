package pt.iscte.paddle.quality.issues;

import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.quality.cases.base.SingleOcurrenceIssue;
import pt.iscte.paddle.quality.misc.Classification;
import pt.iscte.paddle.quality.misc.IssueType;

public class UselessSelfAssignment extends SingleOcurrenceIssue {

	public UselessSelfAssignment(IProgramElement occurrence) {
		super(IssueType.FAULTY_ASSIGNMENT, Classification.AVERAGE, occurrence);
	}

}
