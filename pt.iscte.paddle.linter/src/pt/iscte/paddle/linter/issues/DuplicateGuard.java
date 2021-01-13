package pt.iscte.paddle.linter.issues;

import java.util.List;

import pt.iscte.paddle.linter.cases.base.MultipleOccurrencesIssue;
import pt.iscte.paddle.linter.misc.Classification;
import pt.iscte.paddle.linter.misc.IssueType;
import pt.iscte.paddle.model.IExpression;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IProgramElement;

public class DuplicateGuard extends MultipleOccurrencesIssue {
	
	public DuplicateGuard(IProcedure procedure, List<IExpression> occurrence) {
		super(IssueType.REDUNDANT_GUARD, Classification.AVERAGE, procedure, occurrence);
	}

	
	public IExpression getDuplicateExpression() {
		return (IExpression) getOccurrences().get(0);
	}
	@Override
	public String getIssueTitle() {
		return "Redundant guard: " + getDuplicateExpression();
	}
}
