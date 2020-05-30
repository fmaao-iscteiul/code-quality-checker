package pt.iscte.paddle.quality.issues;

import java.util.List;

import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.quality.cases.base.MultipleOccurrencesIssue;
import pt.iscte.paddle.quality.misc.Classification;
import pt.iscte.paddle.quality.misc.IssueType;

public class Duplicate extends MultipleOccurrencesIssue {

	public Duplicate(List<IProgramElement> occurrences) {
		super(IssueType.DUPLICATE_CODE, Classification.SERIOUS, occurrences);
	}

	public List<IProgramElement> getDuplicates() {
		return occurrences;
	}

}
