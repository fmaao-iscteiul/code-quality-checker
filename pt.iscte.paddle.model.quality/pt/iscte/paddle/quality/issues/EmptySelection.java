package pt.iscte.paddle.quality.issues;

import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.quality.cases.base.SingleOcurrenceIssue;
import pt.iscte.paddle.quality.misc.Classification;
import pt.iscte.paddle.quality.misc.IssueType;

public class EmptySelection extends SingleOcurrenceIssue {

	public EmptySelection(String explanation, IProgramElement branch) {
		super(IssueType.EMPTY_SELECTION, Classification.SERIOUS, branch);
	}
}
