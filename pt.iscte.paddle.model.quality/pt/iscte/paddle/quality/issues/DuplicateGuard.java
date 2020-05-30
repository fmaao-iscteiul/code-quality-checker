package pt.iscte.paddle.quality.issues;

import java.util.List;

import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.quality.cases.base.MultipleOccurrencesIssue;
import pt.iscte.paddle.quality.misc.Classification;
import pt.iscte.paddle.quality.misc.IssueType;

public class DuplicateGuard extends MultipleOccurrencesIssue {
	
	public DuplicateGuard(List<IProgramElement> occurrence) {
		super(IssueType.DUPLICATE_SELECTION_GUARD, Classification.AVERAGE, occurrence);
	}

}
