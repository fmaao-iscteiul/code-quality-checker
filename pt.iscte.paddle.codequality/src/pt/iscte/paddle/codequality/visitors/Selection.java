package pt.iscte.paddle.codequality.visitors;
import pt.iscte.paddle.codequality.cases.BooleanCheck;
import pt.iscte.paddle.codequality.cases.EmptySelection;
import pt.iscte.paddle.codequality.cases.SelectionMisconception;
import pt.iscte.paddle.codequality.linter.Linter;
import pt.iscte.paddle.codequality.misc.Explanations;
import pt.iscte.paddle.model.ISelection;
import pt.iscte.paddle.model.IBinaryExpression;
import pt.iscte.paddle.model.IBinaryOperator;
import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.model.IOperator.OperationType;

public class Selection implements IVisitor {

	public static Selection build() {
		return new Selection();
	}
	
	@Override
	public boolean visit(IBinaryExpression exp) {
		if(exp.getOperationType().equals(OperationType.RELATIONAL) 
				&& exp.getOperator().equals(IBinaryOperator.EQUAL) &&
				exp.getLeftOperand().getType().isBoolean() && exp.getRightOperand().getType().isBoolean())
			Linter.getInstance().register(new BooleanCheck(Explanations.FAULTY_BOOLEAN_CHECK, exp));
		return true;
	}
	@Override
	public boolean visit(ISelection selection) {
		
		if(selection.isEmpty()) {

			Linter.getInstance().register(new EmptySelection(Explanations.EMPTY_SELECTION, selection));
			
			if(selection.hasAlternativeBlock() && !selection.getAlternativeBlock().isEmpty()) {
				System.out.println("jd aj d dwa dawj dwj ");
				Linter.getInstance().register(new SelectionMisconception(Explanations.SELECTION_MISCONCEPTION, selection));
			}
		}
		return true;
	}

	@Override
	public boolean visitAlternative(ISelection selection) {
		if(selection.getAlternativeBlock().isEmpty()) {
			Linter.getInstance().register(new EmptySelection(Explanations.EMPTY_SELECTION, selection));
		}

		return true;
	}



}
