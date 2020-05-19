package pt.iscte.paddle.linter.issues;

import pt.iscte.paddle.linter.cases.base.SingleOcurrenceIssue;
import pt.iscte.paddle.linter.misc.Classification;
import pt.iscte.paddle.linter.misc.IssueType;
import pt.iscte.paddle.model.IProgramElement;

public class EmptySelection extends SingleOcurrenceIssue {

	public EmptySelection(String explanation, IProgramElement branch) {
		super(IssueType.EMPTY_SELECTION, Classification.SERIOUS, branch);
	}
}
