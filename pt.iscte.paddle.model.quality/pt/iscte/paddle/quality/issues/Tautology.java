package pt.iscte.paddle.quality.issues;

import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.quality.cases.base.SingleOcurrenceIssue;
import pt.iscte.paddle.quality.misc.Classification;
import pt.iscte.paddle.quality.misc.IssueType;

public class Tautology extends SingleOcurrenceIssue {

	public Tautology(String explanation, IProgramElement occurrence) {
		super(IssueType.TALTOLOGY, Classification.SERIOUS, occurrence);
	}
	
}
