package pt.iscte.paddle.linter.issues;

import pt.iscte.paddle.linter.cases.base.SingleOcurrenceIssue;
import pt.iscte.paddle.linter.misc.Classification;
import pt.iscte.paddle.linter.misc.IssueType;
import pt.iscte.paddle.model.IProcedureCall;

public class FaultyProcedureCall extends SingleOcurrenceIssue {

	public FaultyProcedureCall(IProcedureCall element) {
		super(IssueType.FAULTY_METHOD_CALL, Classification.AVERAGE, element);
	}
	
	@Override
	public String getIssueTitle() {
		return super.getIssueTitle() + " " + occurrence;
	}
	
}
