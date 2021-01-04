package pt.iscte.paddle.linter.issues;

import pt.iscte.paddle.linter.cases.base.SingleOcurrenceIssue;
import pt.iscte.paddle.linter.misc.Classification;
import pt.iscte.paddle.linter.misc.IssueType;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IProgramElement;

public class EmptyBlock extends SingleOcurrenceIssue {

	public EmptyBlock(String explanation, IProcedure procedure, IProgramElement branch) {
		super(IssueType.EMPTY_BLOCK, Classification.SERIOUS, procedure, branch);
	}
}
