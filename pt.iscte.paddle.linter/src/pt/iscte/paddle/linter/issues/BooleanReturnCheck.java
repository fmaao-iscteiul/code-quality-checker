package pt.iscte.paddle.linter.issues;

import pt.iscte.paddle.linter.cases.base.SingleOcurrenceIssue;
import pt.iscte.paddle.linter.misc.Classification;
import pt.iscte.paddle.linter.misc.IssueType;
import pt.iscte.paddle.model.IControlStructure;
import pt.iscte.paddle.model.IExpression;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.model.ISelection;

public class BooleanReturnCheck extends SingleOcurrenceIssue {

	public BooleanReturnCheck(IProcedure procedure, ISelection selection) {
		super(IssueType.USELESS_BOOLEAN_MAP, Classification.AVERAGE, procedure, selection);
	}
	
	public IExpression getGuard() {
		return ((IControlStructure) getOccurrence()).getGuard();
	}
	
	@Override
	public String getIssueTitle() {
		return "Unnecessary if: " + getGuard() ;
	}
}
