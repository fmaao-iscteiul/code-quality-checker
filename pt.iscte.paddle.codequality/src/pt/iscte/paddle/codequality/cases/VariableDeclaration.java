package pt.iscte.paddle.codequality.cases;

import pt.iscte.paddle.model.IVariable;
import pt.iscte.paddle.model.IBlock.IVisitor;


public class VariableDeclaration implements IVisitor{

	@Override
	public void visit(IVariable exp) {
		System.out.println(exp.getDeclaration());
		IVisitor.super.visit(exp);
	}
}
