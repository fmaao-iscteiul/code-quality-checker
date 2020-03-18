package pt.iscte.paddle.codequality.tests.linter;

import static org.junit.Assert.assertTrue;
import static pt.iscte.paddle.model.IOperator.ADD;
import static pt.iscte.paddle.model.IOperator.SMALLER;
import static pt.iscte.paddle.model.IType.INT;

import org.junit.Test;

import pt.iscte.paddle.model.IArrayElementAssignment;
import pt.iscte.paddle.model.IBlock;
import pt.iscte.paddle.model.ILoop;
import pt.iscte.paddle.model.IModule;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IProcedureCall;
import pt.iscte.paddle.model.IReturn;
import pt.iscte.paddle.model.IType;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.IVariableDeclaration;
import pt.iscte.paddle.model.tests.BaseTest;
import pt.iscte.paddle.model.tests.TestNaturals;

public class NonVoidPureFunction extends BaseTest{

	IProcedure naturals = module.addProcedure(INT.array());
	IVariableDeclaration n = naturals.addParameter(INT);
	IBlock body = naturals.getBody();
	IVariableDeclaration array = body.addVariable(INT.array());
	IVariableAssignment ass1 = body.addAssignment(array, INT.array().stackAllocation(n));
	IVariableDeclaration i = body.addVariable(INT, INT.literal(0));
	
	IVariableAssignment i2 = body.addAssignment(i, INT.literal(0));
	ILoop loop = body.addLoop(SMALLER.on(i, n));
	IArrayElementAssignment ass2 = loop.addArrayElementAssignment(array, ADD.on(i, INT.literal(1)), i);
	IVariableAssignment ass3 = loop.addAssignment(i, ADD.on(i, INT.literal(1)));
	IReturn addReturn = body.addReturn(array);
	
	IProcedure nonVoidPureFunctionText = module.addProcedure(IType.VOID);
	IBlock body2 = nonVoidPureFunctionText.getBody();
	IVariableDeclaration n2 = body2.addVariable(INT);
	IVariableAssignment ass4 = body2.addAssignment(n2, INT.literal(19));
	IVariableAssignment ass5 = body2.addAssignment(n2, INT.literal(19));
	IProcedureCall proCall = body2.addCall(naturals, INT.literal(10));
	
	IVariableDeclaration n3 = body2.addVariable(INT.array());
	IVariableAssignment ass7 = body2.addAssignment(n3, proCall);
	 
	 
	@Case("123")
	void test(){
		module.setId("NonVoidPureFunction");
		
		System.out.println(nonVoidPureFunctionText);
		
	}
	
}
