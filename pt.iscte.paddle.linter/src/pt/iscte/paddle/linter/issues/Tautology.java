package pt.iscte.paddle.linter.issues;

import pt.iscte.paddle.linter.cases.base.SingleOcurrenceIssue;
import pt.iscte.paddle.linter.misc.Classification;
import pt.iscte.paddle.linter.misc.IssueType;
import pt.iscte.paddle.model.IProgramElement;

public class Tautology extends SingleOcurrenceIssue {

	public Tautology(String explanation, IProgramElement occurrence) {
		super(IssueType.TALTOLOGY, Classification.SERIOUS, occurrence);
	}
	
}
