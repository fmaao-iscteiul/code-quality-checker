package pt.iscte.paddle.linter.issues;

import java.util.List;

import pt.iscte.paddle.linter.cases.base.MultipleOccurrencesIssue;
import pt.iscte.paddle.linter.misc.Classification;
import pt.iscte.paddle.linter.misc.IssueType;
import pt.iscte.paddle.model.IProgramElement;

public class UnreachableCode extends MultipleOccurrencesIssue {

	public UnreachableCode(List<IProgramElement> occurrences) {
		super(IssueType.UNREACHABLE_CODE, Classification.SERIOUS, occurrences);
	}

}
