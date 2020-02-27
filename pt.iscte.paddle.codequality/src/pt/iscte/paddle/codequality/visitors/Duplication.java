package pt.iscte.paddle.codequality.visitors;

import pt.iscte.paddle.model.IBinaryExpression;
import pt.iscte.paddle.model.IBlock;
import pt.iscte.paddle.model.IVariable;
import pt.iscte.paddle.model.IVariableAssignment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pt.iscte.paddle.codequality.cases.Duplicate;
import pt.iscte.paddle.codequality.cases.BadCodeCase.Category;
import pt.iscte.paddle.codequality.linter.Linter;
import pt.iscte.paddle.javali.translator.ElementLocation;
import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.model.IExpression;

public class Duplication implements IVisitor{
	
	HashMap<IVariable, IExpression> assignments;
	List<IBinaryExpression> binaryExpressions;
	
	public static Duplication build() {
		return new Duplication();
	}
	
	@Override
	public boolean visit(IBlock block) {
		System.out.println(block);
		return IVisitor.super.visit(block);
	}
		
	private Duplication() {
		this.assignments = new HashMap<IVariable, IExpression>();
		this.binaryExpressions = new ArrayList<IBinaryExpression>();
	}
	
	@Override
	public boolean visit(IBinaryExpression exp) {
		return IVisitor.super.visit(exp);
	}
	
	@Override
	public boolean visit(IVariableAssignment assignment) {
		if(assignments.containsKey(assignment.getTarget()) && assignments.get(assignment.getTarget()).equals(assignment.getExpression()))
			Linter.getInstance().register(new Duplicate(Category.DUPLICATE_CODE, (ElementLocation) assignment.getProperty(ElementLocation.Part.WHOLE), "Duplication of a variable assignment!", assignment));
		else assignments.put(assignment.getTarget(), assignment.getExpression());
		
		return true;
	}
	@Override
	public void visit(IVariable variable) {
//		System.out.println(variable);
		IVisitor.super.visit(variable);
	}
	
	

}
