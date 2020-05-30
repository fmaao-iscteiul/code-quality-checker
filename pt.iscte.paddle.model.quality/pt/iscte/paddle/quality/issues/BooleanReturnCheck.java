package pt.iscte.paddle.quality.issues;

import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.quality.cases.base.SingleOcurrenceIssue;
import pt.iscte.paddle.quality.misc.Classification;
import pt.iscte.paddle.quality.misc.IssueType;

public class BooleanReturnCheck extends SingleOcurrenceIssue {

	public BooleanReturnCheck(IProgramElement selection) {
		super(IssueType.FAULTY_RETURN_BOOLEAN_CHECK, Classification.AVERAGE, selection);
	}
}
