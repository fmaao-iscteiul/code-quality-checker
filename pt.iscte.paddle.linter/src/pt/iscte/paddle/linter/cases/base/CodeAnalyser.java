package pt.iscte.paddle.linter.cases.base;

import java.util.ArrayList;
import java.util.List;

import pt.iscte.paddle.model.IProcedure;

public abstract class CodeAnalyser {

	protected ArrayList<QualityIssue> issues = new ArrayList<QualityIssue>();
	private final IProcedure procedure;
	
	public CodeAnalyser(IProcedure procedure) {
		this.procedure = procedure;
	}
	
	public List<QualityIssue> getQualityIssues(){
		return issues;
	}
	
	public IProcedure getProcedure() {
		return procedure;
	}
}
