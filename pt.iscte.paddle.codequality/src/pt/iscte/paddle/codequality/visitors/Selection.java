package pt.iscte.paddle.codequality.visitors;
import pt.iscte.paddle.codequality.cases.BooleanCheck;
import pt.iscte.paddle.codequality.cases.EmptySelection;
import pt.iscte.paddle.codequality.cases.SelectionMisconception;
import pt.iscte.paddle.codequality.linter.Linter;
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
			Linter.getInstance().register(new BooleanCheck("dawjnjk", exp));
		return true;
	}
	@Override
	public boolean visit(ISelection selection) {
		
		if(selection.isEmpty()) {

			final String explanation = "You have an empty If on the line : "
			+ "\nInstead of using an if to verify a condition and only writing code in the else statement because we want to target the negative outcome of such condition, "
			+ "the right way to go is by placing the operator ‘!’ behind the condition, like ( ! YOUR_CONDITION ). "
			+ "By doing this, you are targeting the desired condition right away, and there is no need to leave empty code blocks,  "
			+ "like an if statement.";

			Linter.getInstance().register(new EmptySelection(explanation, selection));
			
			if(selection.hasAlternativeBlock() && !selection.getAlternativeBlock().isEmpty()) {
				System.out.println("jd aj d dwa dawj dwj ");
				Linter.getInstance().register(new SelectionMisconception("no bueno if else", selection));
			}
		}
		return true;
	}

	@Override
	public boolean visitAlternative(ISelection selection) {
		if(selection.getAlternativeBlock().isEmpty()) {
			final String explanation = "Bla Bla Bla";
			Linter.getInstance().register(new EmptySelection(explanation, selection));
		}

		return true;
	}



}
