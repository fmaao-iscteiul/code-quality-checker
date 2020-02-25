package pt.iscte.paddle.codequality.misc;

import pt.iscte.paddle.codequality.cases.BooleanCheck;
import pt.iscte.paddle.codequality.cases.BadCodeCase.Category;
import pt.iscte.paddle.codequality.linter.Linter;
import pt.iscte.paddle.javali.translator.ElementLocation;
import pt.iscte.paddle.model.IBinaryExpression;
import pt.iscte.paddle.model.IBinaryOperator;
import pt.iscte.paddle.model.IControlStructure;
import pt.iscte.paddle.model.IExpression;
import pt.iscte.paddle.model.IOperator.OperationType;

public class BranchVerifications {
	
	public static void checkBranchParts(IControlStructure selection, IExpression guard) {
		guard.getParts().forEach(part ->{
			if(part.getOperationType().equals(OperationType.RELATIONAL)) {
				IBinaryExpression bExp = IBinaryExpression.create(IBinaryOperator.EQUAL, part.getParts().get(0), part.getParts().get(1));
				if(bExp.getLeftOperand().getType().isBoolean() && bExp.getRightOperand().getType().isBoolean())
					Linter.getInstance().register(new BooleanCheck(Category.FAULTY_BOOLEAN_CHECK, (ElementLocation) selection.getGuard().getProperty(ElementLocation.Part.WHOLE), "", selection.getGuard()));
			}
			else checkBranchParts(selection, part);
		});
	}

}
