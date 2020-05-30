package pt.iscte.paddle.quality.issues;

import pt.iscte.paddle.model.IProcedureCall;
import pt.iscte.paddle.quality.cases.base.SingleOcurrenceIssue;
import pt.iscte.paddle.quality.misc.Classification;
import pt.iscte.paddle.quality.misc.IssueType;

public class FaultyProcedureCall extends SingleOcurrenceIssue {

	public FaultyProcedureCall(IProcedureCall element) {
		super(IssueType.FAULTY_METHOD_CALL, Classification.AVERAGE, element);
	}
	
}
