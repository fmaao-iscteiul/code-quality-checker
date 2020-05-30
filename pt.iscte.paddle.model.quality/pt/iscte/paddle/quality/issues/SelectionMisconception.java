package pt.iscte.paddle.quality.issues;

import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.quality.cases.base.SingleOcurrenceIssue;
import pt.iscte.paddle.quality.misc.Classification;
import pt.iscte.paddle.quality.misc.IssueType;

public class SelectionMisconception extends SingleOcurrenceIssue {

	public SelectionMisconception(String explanation, IProgramElement element) {
		super(IssueType.SELECTION_MISCONCEPTION, Classification.AVERAGE, element);
	}

}
