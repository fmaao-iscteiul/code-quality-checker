package pt.iscte.paddle.linter.issues;

import java.util.List;
import pt.iscte.paddle.linter.cases.base.MultipleOccurrencesIssue;
import pt.iscte.paddle.linter.misc.Classification;
import pt.iscte.paddle.linter.misc.IssueType;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IProgramElement;

public class Duplicate extends MultipleOccurrencesIssue {

	public Duplicate(IProcedure procedure, List<IProgramElement> occurrences) {
		super(IssueType.REDUNDANT_BRANCHES, Classification.SERIOUS, procedure, occurrences);
	}

	public List<IProgramElement> getDuplicates() {
		return occurrences;
	}

}
