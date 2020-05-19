package pt.iscte.paddle.linter.issues;

import java.util.List;

import pt.iscte.paddle.linter.cases.base.MultipleOccurrencesIssue;
import pt.iscte.paddle.linter.misc.Classification;
import pt.iscte.paddle.linter.misc.IssueType;
import pt.iscte.paddle.model.IProgramElement;

public class DuplicateGuard extends MultipleOccurrencesIssue {
	
	public DuplicateGuard(List<IProgramElement> occurrence) {
		super(IssueType.DUPLICATE_SELECTION_GUARD, Classification.AVERAGE, occurrence);
	}

}
