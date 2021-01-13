package pt.iscte.paddle.linter.issues;

import pt.iscte.paddle.linter.cases.base.SingleOcurrenceIssue;
import pt.iscte.paddle.linter.misc.Classification;
import pt.iscte.paddle.linter.misc.IssueType;
import pt.iscte.paddle.model.IBinaryExpression;
import pt.iscte.paddle.model.IExpression;
import pt.iscte.paddle.model.ILiteral;
import pt.iscte.paddle.model.IProcedure;

public class BooleanCheck extends SingleOcurrenceIssue {

	private ILiteral literal;

	private IExpression expression;
	private IBinaryExpression wholeExpression;
	
	public BooleanCheck(IProcedure procedure, IBinaryExpression wholeExpression) {
		super(IssueType.USELESS_COMPARISON, Classification.LIGHT, procedure, findExpression(wholeExpression));
		this.wholeExpression = wholeExpression;
		literal = findLiteral(wholeExpression);
		expression = findExpression(wholeExpression);
	}
	
	public ILiteral getLiteral() {
		return literal;
	}
	
	private static ILiteral findLiteral(IBinaryExpression e) {
		if(e.getLeftOperand() instanceof ILiteral)
			return (ILiteral) e.getLeftOperand();
		else
			return (ILiteral) e.getRightOperand();
			
	}
	
	private static IExpression findExpression(IBinaryExpression e) {
		if(e.getLeftOperand() instanceof IExpression)
			return (IExpression) e.getLeftOperand();
		else
			return (IExpression) e.getRightOperand();
			
	}
	
	public IExpression getExpression() {
		return expression;
	}

	@Override
	public String getIssueTitle() {
		return "Useless comparison: " + wholeExpression;
	}
}
