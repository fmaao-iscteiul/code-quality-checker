package pt.iscte.paddle.codequality.visitors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import pt.iscte.paddle.codequality.cases.Duplicate;
import pt.iscte.paddle.codequality.cases.FaultyProcedureCall;
import pt.iscte.paddle.codequality.linter.Linter;
import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.codequality.misc.Explanations;
import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.model.IProcedureCall;
import pt.iscte.paddle.model.IType;
import pt.iscte.paddle.model.IVariableAssignment;

public class ProcedureCall implements IVisitor {

	private Set<IProcedureCall> procedureCalls;
	private Duplicate duplicateProcedures;
	
	public ProcedureCall() {
		this.procedureCalls = new HashSet<IProcedureCall>();
	}
	
	public static ProcedureCall build() {
		return new ProcedureCall();
	}
		
	@Override
	public boolean visit(IVariableAssignment assignment) {
//		System.out.println("IVariableAssignment: " + (assignment.getExpression() instanceof IProcedureCall));
		return IVisitor.super.visit(assignment);
	}
	
	@Override
	public boolean visit(IProcedureCall call) {
		boolean exists = false;
		for(IProcedureCall proc: procedureCalls) {
			if(call.isSame(proc)) {
				if(duplicateProcedures == null) {
					duplicateProcedures = new Duplicate(Category.DUPLICATE_PROCEDURE_CALL, Explanations.DUPLICATE_STATEMENT, call);
					duplicateProcedures.addAssignment(proc);
					Linter.getInstance().register(duplicateProcedures);
				} else duplicateProcedures.addAssignment(call);
				exists = true;
			}
		}
		if(!exists) procedureCalls.add(call);
		
		if(!call.getProcedure().getReturnType().equals(IType.VOID)) {
			String explanation = "This method call is being used as if the method was void. The " + call.getProcedure().longSignature() + " method that was called returns the type: " 
					+ call.getProcedure().getReturnType() + ". Non void methods should not be called as void ones because it's return value can be relevant.";
			Linter.getInstance().register(new FaultyProcedureCall(explanation, call));
		}
			
		return false;
	}

}
