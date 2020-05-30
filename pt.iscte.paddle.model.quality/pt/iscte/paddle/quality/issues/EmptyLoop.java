package pt.iscte.paddle.quality.issues;

import pt.iscte.paddle.model.IControlStructure;
import pt.iscte.paddle.quality.cases.base.SingleOcurrenceIssue;
import pt.iscte.paddle.quality.misc.Classification;
import pt.iscte.paddle.quality.misc.IssueType;

public class EmptyLoop extends SingleOcurrenceIssue {

	public EmptyLoop(IssueType category, String explanation, IControlStructure branch) {
		super(IssueType.EMPTY_LOOP, Classification.SERIOUS, branch);
	}
}
