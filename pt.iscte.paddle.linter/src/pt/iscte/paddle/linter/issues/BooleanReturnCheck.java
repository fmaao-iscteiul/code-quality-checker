package pt.iscte.paddle.linter.issues;

import pt.iscte.paddle.linter.cases.base.SingleOcurrenceIssue;
import pt.iscte.paddle.linter.misc.Classification;
import pt.iscte.paddle.linter.misc.IssueType;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IProgramElement;

public class BooleanReturnCheck extends SingleOcurrenceIssue {

	public BooleanReturnCheck(IProcedure procedure, IProgramElement selection) {
		super(IssueType.FAULTY_RETURN_BOOLEAN_CHECK, Classification.AVERAGE, procedure, selection);
	}
}
