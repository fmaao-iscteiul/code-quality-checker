package pt.iscte.paddle.quality.cases.base;

import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.quality.misc.Classification;
import pt.iscte.paddle.quality.misc.IssueType;

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
