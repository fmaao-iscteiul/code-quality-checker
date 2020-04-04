package pt.iscte.paddle.codequality.misc;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pt.iscte.paddle.model.IBinaryExpression;
import pt.iscte.paddle.model.IExpression;
import pt.iscte.paddle.model.IUnaryExpression;
import pt.iscte.paddle.model.IVariableExpression;

public class Compability {

	
	public static Set<IExpression> extractVariables(List<IExpression> expressions) {
		Set<IExpression> variables = new HashSet<IExpression>();
		
		expressions.forEach(part -> {
			if(part instanceof IBinaryExpression) variableSearcher(variables, part);
			else variables.add(part);
		});
		return variables;
	}

	private static void variableSearcher(Set<IExpression> vars, IExpression exp) {
		IBinaryExpression bExp = (IBinaryExpression) exp;
		
		if(bExp.getLeftOperand() instanceof IBinaryExpression) variableSearcher(vars, bExp.getLeftOperand());
		else vars.add((IExpression) bExp.getLeftOperand());
		if(bExp.getRightOperand() instanceof IBinaryExpression) variableSearcher(vars, bExp.getRightOperand());
		else vars.add((IExpression) bExp.getRightOperand());
	}
	
}
