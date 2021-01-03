package pt.iscte.paddle.linter.issues;

import pt.iscte.paddle.linter.cases.base.SingleOcurrenceIssue;
import pt.iscte.paddle.linter.misc.Classification;
import pt.iscte.paddle.linter.misc.IssueType;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IProgramElement;

public class UselessReturn extends SingleOcurrenceIssue{

	public UselessReturn(IProcedure procedure,
			IProgramElement occ) {
		super(IssueType.USELESS_CODE, Classification.LIGHT, procedure, occ);
	}

}
