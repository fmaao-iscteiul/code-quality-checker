package pt.iscte.paddle.codequality.visitors;
import pt.iscte.paddle.codequality.cases.BooleanCheck;
import pt.iscte.paddle.codequality.cases.EmptyBranch;

import com.sun.javafx.fxml.expression.BinaryExpression;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import pt.iscte.paddle.codequality.cases.BadCodeCase.Category;
import pt.iscte.paddle.codequality.linter.Linter;
import pt.iscte.paddle.codequality.misc.BranchVerifications;
import pt.iscte.paddle.javali.translator.ElementLocation;
import pt.iscte.paddle.model.ISelection;
import pt.iscte.paddle.model.IBinaryExpression;
import pt.iscte.paddle.model.IBinaryOperator;
import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.model.IControlStructure;
import pt.iscte.paddle.model.IOperator.OperationType;
import pt.iscte.paddle.model.IExpression;

public class Selection implements IVisitor {

	public static Selection build() {
		return new Selection();
	}
	
	@Override
	public boolean visit(IBinaryExpression exp) {
		if(exp.getOperationType().equals(OperationType.RELATIONAL) 
				&& exp.getOperator().equals(IBinaryOperator.EQUAL) &&
				exp.getLeftOperand().getType().isBoolean() && exp.getRightOperand().getType().isBoolean())
			Linter.getInstance().register(new BooleanCheck(Category.FAULTY_BOOLEAN_CHECK, (ElementLocation) exp.getProperty(ElementLocation.Part.WHOLE), "", exp));
		return true;
	}
	@Override
	public boolean visit(ISelection selection) {

		ElementLocation location = (ElementLocation) selection.getProperty(ElementLocation.Part.WHOLE);

		if(selection.getBlock().isEmpty()) {

			final String explanation = "You have an empty If on the line : " +location.getLine() 
			+ "\nInstead of using an if to verify a condition and only writing code in the else statement because we want to target the negative outcome of such condition, "
			+ "the right way to go is by placing the operator ‘!’ behind the condition, like ( ! YOUR_CONDITION ). "
			+ "By doing this, you are targeting the desired condition right away, and there is no need to leave empty code blocks,  "
			+ "like an if statement.";

			EmptyBranch emptyCondition = new EmptyBranch(Category.EMPTY_SELECTION, location, explanation, selection);
			Linter.getInstance().register(emptyCondition);
		}
		return true;
	}


	@Override
	public boolean visitAlternative(ISelection selection) {
		if(selection.isEmpty()) {
			ElementLocation location = (ElementLocation) selection.getProperty(ElementLocation.Part.WHOLE);

			final String explanation = "Bla Bla Bla";

			EmptyBranch emptyCondition = new EmptyBranch(Category.EMPTY_ALTERNATIVE, location, explanation, selection);
			Linter.getInstance().register(emptyCondition);
		}

		return true;
	}



}
