package pt.iscte.paddle.codequality.visitors;

import java.util.HashMap;
import java.util.Map;

import pt.iscte.paddle.model.IArrayElementAssignment;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.model.IExpression;
import pt.iscte.paddle.model.IVariable;

public class MagicNumbers implements IVisitor{

	public static MagicNumbers build() {
		return new MagicNumbers();
	}

	Map<IVariable, IExpression> variableAssignments = new HashMap<IVariable, IExpression>();
	Map<IVariable, IExpression> arrayElementAssignments = new HashMap<IVariable, IExpression>();

	@Override
	public boolean visit(IVariableAssignment assignment) {

		if(variableAssignments.containsKey(assignment.getTarget()) 
				&& variableAssignments.get(assignment.getTarget()).equals(assignment.getExpression())) {
			System.out.println("duplicate: " + assignment);
		} else variableAssignments.put(assignment.getTarget(), assignment.getExpression());
		return IVisitor.super.visit(assignment);
	}

	@Override
	public boolean visit(IArrayElementAssignment assignment) {
		System.out.println(assignment.getExpression());
		return IVisitor.super.visit(assignment);
	}
}
