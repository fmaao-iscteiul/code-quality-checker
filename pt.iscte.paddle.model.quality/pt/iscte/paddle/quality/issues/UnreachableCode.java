package pt.iscte.paddle.quality.issues;

import java.util.List;

import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.quality.cases.base.MultipleOccurrencesIssue;
import pt.iscte.paddle.quality.misc.Classification;
import pt.iscte.paddle.quality.misc.IssueType;

public class UnreachableCode extends MultipleOccurrencesIssue {

	public UnreachableCode(List<IProgramElement> occurrences) {
		super(IssueType.UNREACHABLE_CODE, Classification.SERIOUS, occurrences);
	}

}
