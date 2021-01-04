package pt.iscte.paddle.linter.visitors;

import pt.iscte.paddle.linter.cases.base.CodeAnalyser;
import pt.iscte.paddle.linter.issues.EmptyBlock;
import pt.iscte.paddle.linter.issues.NegativeBooleanCheck;
import pt.iscte.paddle.linter.issues.PositiveBooleanCheck;
import pt.iscte.paddle.linter.issues.IfEmptyElse;
import pt.iscte.paddle.linter.misc.Explanations;
import pt.iscte.paddle.model.IBinaryExpression;
import pt.iscte.paddle.model.IBinaryOperator;
import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.model.IOperator.OperationType;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.ISelection;
import pt.iscte.paddle.model.IType;

public class Selection extends CodeAnalyser implements IVisitor {

	public Selection(IProcedure procedure) {
		super(procedure);
	}

	@Override
	public boolean visit(IBinaryExpression exp) {
		if((exp.getOperator().equals(IBinaryOperator.EQUAL) || exp.getOperator().equals(IBinaryOperator.DIFFERENT))
				&& exp.getLeftOperand().getType().isBoolean() 
				&& exp.getRightOperand().getType().isBoolean()) {


			if (exp.getRightOperand().isSame(IType.BOOLEAN.literal(false))
					||  exp.getLeftOperand().isSame(IType.BOOLEAN.literal(false)) ) {
				issues.add(new NegativeBooleanCheck(getProcedure(), exp));	
			}

			if(exp.getRightOperand().isSame(IType.BOOLEAN.literal(true)) 
					|| exp.getLeftOperand().isSame(IType.BOOLEAN.literal(true)) ) {

				issues.add(new PositiveBooleanCheck(getProcedure(), exp));
			}


		}

		return true;
	}

	@Override
	public boolean visit(ISelection selection) {
		if(selection.isEmpty() && selection.hasAlternativeBlock() && !selection.getAlternativeBlock().isEmpty()) {
			issues.add(new IfEmptyElse(Explanations.SELECTION_MISCONCEPTION, getProcedure(), selection));
		}
		else if(selection.isEmpty()) {
			issues.add(new EmptyBlock(Explanations.EMPTY_SELECTION, getProcedure(), selection));
		}
		else if(selection.hasAlternativeBlock() && selection.getAlternativeBlock().isEmpty()) {
			issues.add(new EmptyBlock(Explanations.EMPTY_SELECTION, getProcedure(), selection.getAlternativeBlock()));
		}
		return true;
	}

//	@Override
//	public boolean visitAlternative(ISelection selection) {
//		if(selection.getAlternativeBlock().isEmpty()) {
//			issues.add(new EmptySelection(Explanations.EMPTY_SELECTION, getProcedure(), selection.getAlternativeBlock()));
//		}
//
//		return true;
//	}
}
