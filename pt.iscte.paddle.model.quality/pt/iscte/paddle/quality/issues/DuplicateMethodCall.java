package pt.iscte.paddle.quality.issues;

import java.util.List;

import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.quality.cases.base.MultipleOccurrencesIssue;
import pt.iscte.paddle.quality.misc.Classification;
import pt.iscte.paddle.quality.misc.IssueType;

public class DuplicateMethodCall extends MultipleOccurrencesIssue {
	

	public DuplicateMethodCall(List<IProgramElement> duplicatesList) {	
		super(IssueType.DUPLICATE_SELECTION_GUARD, Classification.SERIOUS, duplicatesList);
	}

}
