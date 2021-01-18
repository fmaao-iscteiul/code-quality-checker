package pt.iscte.paddle.linter.issues;

import pt.iscte.paddle.linter.cases.base.SingleOcurrenceIssue;
import pt.iscte.paddle.linter.misc.Classification;
import pt.iscte.paddle.linter.misc.IssueType;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.model.IReturn;

public class UselessReturn extends SingleOcurrenceIssue{

	public UselessReturn(IProcedure procedure, IReturn occ) {
		super(IssueType.USELESS_RETURN, Classification.LIGHT, procedure, occ);
	}

	@Override
	public String getIssueTitle() {
		return "Useless return";
	}
}
