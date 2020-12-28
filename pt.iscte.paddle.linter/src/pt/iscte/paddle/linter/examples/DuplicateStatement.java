package pt.iscte.paddle.linter.examples;

import static pt.iscte.paddle.model.IOperator.ADD;
import static pt.iscte.paddle.model.IOperator.SMALLER;
import static pt.iscte.paddle.model.IType.INT;

import pt.iscte.paddle.model.IArrayElementAssignment;
import pt.iscte.paddle.model.IBlock;
import pt.iscte.paddle.model.ILoop;
import pt.iscte.paddle.model.IOperator;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IReturn;
import pt.iscte.paddle.model.ISelection;
import pt.iscte.paddle.model.IType;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.IVariableDeclaration;
import pt.iscte.paddle.model.tests.BaseTest;

public class DuplicateStatement extends BaseTest{
	
	IProcedure naturals = module.addProcedure(INT.array());
	IVariableDeclaration n = naturals.addParameter(INT);
	IBlock body = naturals.getBody();
	IVariableDeclaration array = body.addVariable(INT.array());
	IVariableAssignment ass1 = body.addAssignment(array, INT.array().heapAllocation(n));
	IVariableDeclaration i = body.addVariable(INT, INT.literal(0));
	ISelection selection = body.addSelectionWithAlternative(IOperator.GREATER.on(i, IType.INT.literal(10)));
	IVariableAssignment ass4 = selection.addAssignment(i, INT.literal(10));
	ISelection selection_else = selection.getAlternativeBlock().addSelectionWithAlternative(IOperator.GREATER.on(i, IType.INT.literal(20)));
	IVariableAssignment ass5 = selection_else.getAlternativeBlock().addAssignment(i, INT.literal(10));
	IVariableAssignment ass6 = selection_else.addAssignment(i, INT.literal(10));
	ILoop loop = body.addLoop(SMALLER.on(i, n));
	IArrayElementAssignment ass2 = loop.addArrayElementAssignment(array, ADD.on(i, INT.literal(1)), i);
	IVariableAssignment ass3 = loop.addAssignment(i, ADD.on(i, INT.literal(1)));
	IVariableAssignment ass7 = loop.addAssignment(i, ADD.on(i, INT.literal(1)));
	IVariableAssignment ass12 = loop.addAssignment(i, ADD.on(i, INT.literal(1)));
	IReturn addReturn = body.addReturn(array);
}
