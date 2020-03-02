package pt.iscte.paddle.codequality.visitors;

import pt.iscte.paddle.model.ILoop;
import pt.iscte.paddle.model.IRecordFieldAssignment;
import pt.iscte.paddle.model.IVariable;
import pt.iscte.paddle.model.IVariableAssignment;

import java.util.ArrayList;
import java.util.List;

import pt.iscte.paddle.codequality.cases.Duplicate;
import pt.iscte.paddle.codequality.cases.BadCodeCase.Category;
import pt.iscte.paddle.codequality.linter.Linter;
import pt.iscte.paddle.javali.translator.ElementLocation;
import pt.iscte.paddle.model.IBlock.IVisitor;

public class Assignment implements IVisitor{
	
	List<IVariableAssignment> assignments;
	
	public static Assignment build() {
		return new Assignment();
	}
	
	private Assignment() {
		this.assignments = new ArrayList<IVariableAssignment>();
	}
	
	@Override
	public boolean visit(IRecordFieldAssignment assignment) {
		System.out.println(assignment);
		return IVisitor.super.visit(assignment);
	}
	
	
	@Override
	public boolean visit(IVariableAssignment assignment) {
		if(assignments.contains(assignment)) {
			Linter.getInstance().register(new Duplicate(Category.DUPLICATE, (ElementLocation) assignment.getProperty(ElementLocation.Part.WHOLE), ""));
		}
		else assignments.add(assignment);
		return true;
	}
	@Override
	public void visit(IVariable variable) {
//		System.out.println(variable);
		IVisitor.super.visit(variable);
	}
	
	

}
