package pt.iscte.paddle.codequality.visitors;
import static pt.iscte.paddle.model.IType.BOOLEAN;

import pt.iscte.paddle.codequality.cases.BooleanCheck;
import pt.iscte.paddle.codequality.cases.Contradiction;
import pt.iscte.paddle.codequality.cases.EmptySelection;
import pt.iscte.paddle.codequality.cases.SelectionMisconception;
import pt.iscte.paddle.codequality.cases.Tautology;
import pt.iscte.paddle.codequality.linter.Linter;
import pt.iscte.paddle.codequality.misc.Explanations;
import pt.iscte.paddle.model.IBinaryExpression;
import pt.iscte.paddle.model.IBinaryOperator;
import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.model.IOperator.OperationType;
import pt.iscte.paddle.model.ISelection;
import pt.iscte.paddle.model.IType;

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
		if(!selection.getGuard().isNull()) {
			if(selection.getGuard().isSame(BOOLEAN.literal(true))) 
				Linter.getInstance().register(new Tautology(Explanations.TAUTOLOGY, selection.getGuard()));
			else if(selection.getGuard().isSame(BOOLEAN.literal(false)))
				Linter.getInstance().register(new Contradiction(Explanations.CONTRADICTION, selection.getGuard()));
		}

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
