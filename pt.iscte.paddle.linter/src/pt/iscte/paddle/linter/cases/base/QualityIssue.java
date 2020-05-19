package pt.iscte.paddle.linter.cases.base;

import pt.iscte.paddle.linter.misc.Classification;
import pt.iscte.paddle.linter.misc.IssueType;

public abstract class QualityIssue {

	private final IssueType type;
	private final Classification classification;

	public QualityIssue(IssueType category, Classification classification) {
		this.type = category;
		this.classification = classification;
	}

	public IssueType getIssueType() {
		return type;
	}
	
	public Classification getClassification() {
		return classification;
	}
}
