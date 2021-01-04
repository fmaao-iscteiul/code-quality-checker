package pt.iscte.paddle.linter.issues;

import pt.iscte.paddle.linter.cases.base.SingleOcurrenceIssue;
import pt.iscte.paddle.linter.misc.Classification;
import pt.iscte.paddle.linter.misc.IssueType;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IProgramElement;

public class Tautology extends SingleOcurrenceIssue {

	public Tautology(String explanation, IProcedure procedure, IProgramElement occurrence) {
		super(IssueType.TAUTOLOGY, Classification.SERIOUS, procedure, occurrence);
	}
	
}
