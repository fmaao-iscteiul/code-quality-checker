package pt.iscte.paddle.linter.cases.base;

import pt.iscte.paddle.linter.misc.Classification;
import pt.iscte.paddle.linter.misc.IssueType;
import pt.iscte.paddle.model.IProcedure;

public abstract class QualityIssue {

	private final IssueType type;
	private final Classification classification;
	private final IProcedure procedure;
	
	public QualityIssue(IssueType category, Classification classification, IProcedure procedure) {
		this.type = category;
		this.classification = classification;
		this.procedure = procedure;
	}

	public IssueType getIssueType() {
		return type;
	}
	
	public Classification getClassification() {
		return classification;
	}
	
	public String getIssueTitle() {
		return type.toString();
	}
	
	public IProcedure getProcedure() {
		return procedure;
	}
}
