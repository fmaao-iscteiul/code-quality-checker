package pt.iscte.paddle.codequality.visitors;

import pt.iscte.paddle.codequality.cases.FaultyAssignment;
import pt.iscte.paddle.codequality.cases.MagicNumber;
import pt.iscte.paddle.codequality.linter.Linter;
import pt.iscte.paddle.codequality.misc.Explanations;
import pt.iscte.paddle.interpreter.IValue;
import pt.iscte.paddle.model.IArrayAllocation;
import pt.iscte.paddle.model.IArrayElementAssignment;
import pt.iscte.paddle.model.IBinaryExpression;
import pt.iscte.paddle.model.ILiteral;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.IVariableDeclaration;
import pt.iscte.paddle.model.IVariableDereference;
import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.model.IOperator;
import pt.iscte.paddle.model.IStatement;
import pt.iscte.paddle.model.IType;

public class MagicNumbers implements IVisitor{

	private MagicNumber magicNumber;

	public static MagicNumbers build() {
		return new MagicNumbers();
	}

	@Override
	public boolean visit(IVariableAssignment assignment) {
		if(assignment.getTarget().toString().equals(assignment.getExpression().toString()))
			Linter.getInstance().register(new FaultyAssignment(Explanations.SELF_ASSIGNMENT, assignment));
		else if(assignment.getExpression().getType().isNumber() 
				&& !assignment.getExpression().isDecomposable()  
				&& Integer.parseInt(assignment.getExpression().toString()) > 1){
			if(magicNumber == null) {
				magicNumber = new MagicNumber(Explanations.MAGIC_NUMBER, assignment);
				Linter.getInstance().register(magicNumber);
			} else magicNumber.addAssignment(assignment);
		}
		return true;
	}

	@Override
	public boolean visit(IArrayElementAssignment assignment) {
		if(assignment.getExpression().getType().isNumber() && !assignment.getExpression().isDecomposable() && 
				Integer.parseInt(assignment.getExpression().toString()) > 1)
			Linter.getInstance().register(new MagicNumber(Explanations.MAGIC_NUMBER, assignment));

		return true;
	}
}
