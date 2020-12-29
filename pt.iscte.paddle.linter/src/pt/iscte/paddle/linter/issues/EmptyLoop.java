package pt.iscte.paddle.linter.issues;

import pt.iscte.paddle.linter.cases.base.SingleOcurrenceIssue;
import pt.iscte.paddle.linter.misc.Classification;
import pt.iscte.paddle.linter.misc.IssueType;
import pt.iscte.paddle.model.IControlStructure;
import pt.iscte.paddle.model.IProcedure;

public class EmptyLoop extends SingleOcurrenceIssue {

	public EmptyLoop(IssueType category, String explanation, IProcedure procedure, IControlStructure branch) {
		super(IssueType.EMPTY_LOOP, Classification.SERIOUS, procedure, branch);
	}
}
