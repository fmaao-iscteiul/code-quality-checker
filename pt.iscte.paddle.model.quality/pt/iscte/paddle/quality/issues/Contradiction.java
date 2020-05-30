package pt.iscte.paddle.quality.issues;

import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.quality.cases.base.SingleOcurrenceIssue;
import pt.iscte.paddle.quality.misc.Classification;
import pt.iscte.paddle.quality.misc.IssueType;

public class Contradiction extends SingleOcurrenceIssue {

	public Contradiction(String explanation, IProgramElement element) {
		super(IssueType.CONTRADICTION, Classification.SERIOUS, element);
	}
}
