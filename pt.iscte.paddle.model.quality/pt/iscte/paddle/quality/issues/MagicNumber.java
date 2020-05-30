package pt.iscte.paddle.quality.issues;


import java.util.List;

import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.quality.cases.base.MultipleOccurrencesIssue;
import pt.iscte.paddle.quality.misc.Classification;
import pt.iscte.paddle.quality.misc.IssueType;

public class MagicNumber extends MultipleOccurrencesIssue {


	public MagicNumber(String explanation, IProgramElement magicNumber) {
		super(IssueType.MAGIC_NUMBER, Classification.AVERAGE);
		this.occurrences.add(magicNumber);
	}

	public List<IProgramElement> getOccurrences() {
		return occurrences;
	}

	public void addAssignment(IProgramElement statement) {
		this.occurrences.add(statement);
	}

}
