package pt.iscte.paddle.linter.issues;

import pt.iscte.paddle.linter.cases.base.SingleOcurrenceIssue;
import pt.iscte.paddle.linter.misc.Classification;
import pt.iscte.paddle.linter.misc.IssueType;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IVariableAssignment;

public class UselessVariableAssignment extends SingleOcurrenceIssue {

	public UselessVariableAssignment(IProcedure procedure, IVariableAssignment occurrence) {
		super(IssueType.USELESS_ASSIGN, Classification.SERIOUS, procedure, occurrence);
	}

}
