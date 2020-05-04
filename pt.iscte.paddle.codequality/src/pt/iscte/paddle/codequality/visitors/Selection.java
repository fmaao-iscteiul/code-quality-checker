package pt.iscte.paddle.codequality.visitors;
import static pt.iscte.paddle.model.IType.BOOLEAN;

import pt.iscte.paddle.codequality.issues.BooleanCheck;
import pt.iscte.paddle.codequality.issues.Contradiction;
import pt.iscte.paddle.codequality.issues.EmptySelection;
import pt.iscte.paddle.codequality.issues.SelectionMisconception;
import pt.iscte.paddle.codequality.issues.Tautology;
import pt.iscte.paddle.codequality.linter.Linter;
import pt.iscte.paddle.codequality.misc.Explanations;
import pt.iscte.paddle.model.IBinaryExpression;
import pt.iscte.paddle.model.IBinaryOperator;
import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.model.IOperator.OperationType;
import pt.iscte.paddle.model.ISelection;
import pt.iscte.paddle.model.IType;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.IVariableDeclaration;

public class Selection implements IVisitor {

	public static Selection build() {
		return new Selection();
	}

	@Override
	public boolean visit(IBinaryExpression exp) {
		if(exp.getOperationType().equals(OperationType.RELATIONAL) 
				&& (exp.getOperator().equals(IBinaryOperator.EQUAL) || exp.getOperator().equals(IBinaryOperator.DIFFERENT))
				&& exp.getLeftOperand().getType().isBoolean() 
				&& exp.getRightOperand().getType().isBoolean()
				&& ((exp.getRightOperand().isSame(IType.BOOLEAN.literal(false)) || exp.getRightOperand().isSame(IType.BOOLEAN.literal(true)))
						||  (exp.getLeftOperand().isSame(IType.BOOLEAN.literal(false)) || exp.getLeftOperand().isSame(IType.BOOLEAN.literal(true)))))
			Linter.getInstance().register(new BooleanCheck(Explanations.FAULTY_BOOLEAN_CHECK, exp));
		return true;
	}

	@Override
	public boolean visit(ISelection selection) {
		if(selection.isEmpty()) {

			Linter.getInstance().register(new EmptySelection(Explanations.EMPTY_SELECTION, selection));

			if(selection.hasAlternativeBlock() && !selection.getAlternativeBlock().isEmpty()) {
				Linter.getInstance().register(new SelectionMisconception(Explanations.SELECTION_MISCONCEPTION, selection));
			}
		}
		return true;
	}

	@Override
	public boolean visitAlternative(ISelection selection) {
		if(selection.getAlternativeBlock().isEmpty()) {
			Linter.getInstance().register(new EmptySelection(Explanations.EMPTY_SELECTION, selection.getAlternativeBlock()));
		}

		return true;
	}



}
