package pt.iscte.paddle.quality.issues;

import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.quality.cases.base.SingleOcurrenceIssue;
import pt.iscte.paddle.quality.misc.Classification;
import pt.iscte.paddle.quality.misc.IssueType;

public class UnnecessaryReturn extends SingleOcurrenceIssue {

	public UnnecessaryReturn(IProgramElement occ) {
		super(IssueType.USELESS_RETURN, Classification.LIGHT, occ);
	}

}
