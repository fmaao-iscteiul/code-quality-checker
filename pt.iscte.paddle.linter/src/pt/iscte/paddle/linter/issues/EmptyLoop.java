package pt.iscte.paddle.linter.issues;

import pt.iscte.paddle.linter.cases.base.SingleOcurrenceIssue;
import pt.iscte.paddle.linter.misc.Classification;
import pt.iscte.paddle.linter.misc.IssueType;
import pt.iscte.paddle.model.IControlStructure;

public class EmptyLoop extends SingleOcurrenceIssue {

	public EmptyLoop(IssueType category, String explanation, IControlStructure branch) {
		super(IssueType.EMPTY_LOOP, Classification.SERIOUS, branch);
	}
}
