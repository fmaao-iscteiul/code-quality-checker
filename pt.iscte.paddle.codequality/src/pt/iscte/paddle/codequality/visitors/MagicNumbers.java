package pt.iscte.paddle.codequality.visitors;

import pt.iscte.paddle.codequality.cases.MagicNumber;
import pt.iscte.paddle.codequality.linter.Linter;
import pt.iscte.paddle.interpreter.IValue;
import pt.iscte.paddle.model.IArrayElementAssignment;
import pt.iscte.paddle.model.ILiteral;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.IVariableDeclaration;
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
	public void visit(IVariableDeclaration variable) {
		IVisitor.super.visit(variable);
	}

	@Override
	public boolean visit(IVariableAssignment assignment) {
		if(assignment.getExpression().getType().isNumber() && !assignment.getExpression().isDecomposable() && 
				Integer.parseInt(assignment.getExpression().toString()) > 1){
			if(magicNumber == null) {
				magicNumber = new MagicNumber("magic number explanation", assignment);
				Linter.getInstance().register(magicNumber);
			} else magicNumber.addAssignment(assignment);
		}
		return true;
	}
	
	//	@Override
	//	public void visit(IVariableExpression exp) {
	//		System.out.println("variable expression: "+ exp);
	//		IVisitor.super.visit(exp);
	//	}

	@Override
	public boolean visit(IArrayElementAssignment assignment) {
		if(assignment.getExpression().getType().isNumber() && !assignment.getExpression().isDecomposable() && 
				Integer.parseInt(assignment.getExpression().toString()) > 1){
			if(magicNumber == null) {
				magicNumber = new MagicNumber("magic number explanation", assignment);
				Linter.getInstance().register(magicNumber);
			} else magicNumber.addAssignment(assignment);
		}
		return true;
	}
}
