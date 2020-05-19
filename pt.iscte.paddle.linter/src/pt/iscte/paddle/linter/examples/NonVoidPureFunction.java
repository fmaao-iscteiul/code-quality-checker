package pt.iscte.paddle.linter.examples;

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
	
//	IVariableAssignment ass123 = body.addAssignment(n, ADD.on(n, INT.literal(1)));
//	IVariableAssignment ass12 = body.addAssignment(n, ADD.on(n, INT.literal(1)));
	
	
	IVariableAssignment i2 = body.addAssignment(i, i);
	ILoop loop = body.addLoop(SMALLER.on(i, n));
	IArrayElementAssignment ass2 = loop.addArrayElementAssignment(array, ADD.on(i, INT.literal(1)), i);
	IVariableAssignment ass3 = loop.addAssignment(i, ADD.on(i, INT.literal(1)));
	IReturn addReturn = body.addReturn(array);
	
	IProcedure nonVoidPureFunctionText = module.addProcedure(IType.INT.array());
	IBlock body2 = nonVoidPureFunctionText.getBody();
	IVariableDeclaration naturalsArray = body2.addVariable(INT.array());
	IVariableAssignment ass112 = body2.addAssignment(naturalsArray, INT.array().stackAllocation(n));
	IVariableDeclaration b = body2.addVariable(INT, INT.literal(10)); 
	
	IProcedureCall proCall = body2.addCall(naturals, b);
	IVariableAssignment b1 = body2.addAssignment(b, INT.literal(20));
	IProcedureCall proCall2 = body2.addCall(naturals, b);
	
	IReturn r = body2.addReturn(naturalsArray);
//	IVariableAssignment ass8 = body2.addAssignment(n2, ADD.on(n, INT.literal(1)));
//	IProcedureCall proCal2 = body2.addCall(naturals, n2);
//	IVariableAssignment ass9 = body2.addAssignment(n2, ADD.on(n, INT.literal(1)));
//	IProcedureCall proCall3 = body2.addCall(naturals, n2);
//	
//	IProcedureCall proCall4 = body2.addCall(naturals, INT.literal(40));
//	IVariableAssignment ass5 = body2.addAssignment(n2, INT.literal(19));
//	IProcedureCall proCall5 = body2.addCall(naturals, INT.literal(40));
//	
//	IVariableDeclaration n3 = body2.addVariable(INT.array());
//	IVariableAssignment ass7 = body2.addAssignment(n3, naturals.expression(INT.literal(20)));
//	IVariableAssignment ass10 = body2.addAssignment(n3, naturals.expression(INT.literal(20)));
	 
	 
	@Case("123")
	void test(){
		module.setId("NonVoidPureFunction");
		
		System.out.println(nonVoidPureFunctionText);
		
	}
	
}
