package pt.iscte.paddle.linter.issues;

import pt.iscte.paddle.linter.cases.base.SingleOcurrenceIssue;
import pt.iscte.paddle.linter.misc.Classification;
import pt.iscte.paddle.linter.misc.IssueType;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IProcedureCall;
import pt.iscte.paddle.model.roles.impl.FunctionClassifier;

public class FaultyProcedureCall extends SingleOcurrenceIssue {

	public FaultyProcedureCall(IProcedure procedure, IProcedureCall element) {
		super(IssueType.USELESS_CALL, Classification.AVERAGE, procedure, element);
	}
	
	@Override
	public String getIssueTitle() {
		return super.getIssueTitle() + " " + new FunctionClassifier((IProcedure) ((IProcedureCall) occurrence).getProcedure()).getClassification();
	}
	
}
