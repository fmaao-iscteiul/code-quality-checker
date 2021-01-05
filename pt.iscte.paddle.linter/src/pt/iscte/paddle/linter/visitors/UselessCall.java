package pt.iscte.paddle.linter.visitors;

import pt.iscte.paddle.linter.cases.base.CodeAnalyser;
import pt.iscte.paddle.linter.issues.FaultyProcedureCall;
import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.model.roles.IFunctionClassifier.Status;
import pt.iscte.paddle.model.roles.impl.FunctionClassifier;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IProcedureCall;

public class UselessCall extends CodeAnalyser implements IVisitor {

	public UselessCall(IProcedure procedure) {
		super(procedure);
	}

	@Override
	public boolean visit(IProcedureCall call) {
		Status classification = new FunctionClassifier((IProcedure) call.getProcedure()).getClassification();
		
		if(classification == Status.FUNCTION)
			issues.add(new FaultyProcedureCall(getProcedure(), call));
		
		return false;
	}
}
