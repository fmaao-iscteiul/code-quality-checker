package pt.iscte.paddle.linter.cases.base;

import pt.iscte.paddle.linter.misc.Classification;
import pt.iscte.paddle.linter.misc.IssueType;
import pt.iscte.paddle.model.IProgramElement;

public class SingleOcurrenceIssue extends QualityIssue {
	
	protected IProgramElement occurrence;

	protected SingleOcurrenceIssue(IssueType category, Classification classification, IProgramElement occ) {
		super(category, classification);
		this.occurrence = occ;
	}
	
	public IProgramElement getOccurrence() {
		return occurrence;
	}

}
