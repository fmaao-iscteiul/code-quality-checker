package pt.iscte.paddle.codequality.visitors;

import pt.iscte.paddle.model.IVariableAddress;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.IBlock.IVisitor;

public class DeadCode implements IVisitor{
	
	public static DeadCode build() {
		return new DeadCode();
	}

	@Override
	public boolean visit(IVariableAssignment assignment) {
//		System.out.println("IVariableAssignment: " + assignment);
		return IVisitor.super.visit(assignment);
	}
	
	
	@Override
	public void visit(IVariableAddress exp) {
//		System.out.println("IVariableAddress: " + exp);
		IVisitor.super.visit(exp);
	}
}
