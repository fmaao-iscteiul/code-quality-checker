package pt.iscte.paddle.codequality.tests.linter;

import static pt.iscte.paddle.model.IOperator.ADD;
import static pt.iscte.paddle.model.IOperator.SMALLER;
import static pt.iscte.paddle.model.IType.BOOLEAN;
import static pt.iscte.paddle.model.IType.INT;

import pt.iscte.paddle.model.IArrayElementAssignment;
import pt.iscte.paddle.model.IBlock;
import pt.iscte.paddle.model.ILoop;
import pt.iscte.paddle.model.IOperator;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IReturn;
import pt.iscte.paddle.model.ISelection;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.IVariableDeclaration;
import pt.iscte.paddle.model.tests.BaseTest;

public class UnreachableCode extends BaseTest{
	
	IProcedure naturals = module.addProcedure(INT.array());
	IVariableDeclaration n = naturals.addParameter(INT);
	IVariableDeclaration b = naturals.addParameter(BOOLEAN);
	IBlock body = naturals.getBody();
	IVariableDeclaration array = body.addVariable(INT.array());
	IVariableAssignment ass1 = body.addAssignment(array, INT.array().stackAllocation(n));
	IReturn addReturn1 = body.addReturn(array);
	ISelection selection_true = body.addSelection(IOperator.EQUAL.on(n, INT.literal(10)));
	ISelection selection_false = body.addSelection(IOperator.EQUAL.on(b, BOOLEAN.literal(false)));
	IVariableDeclaration i = body.addVariable(INT, INT.literal(0));
	ILoop loop = body.addLoop(SMALLER.on(i, n));
	IArrayElementAssignment ass2 = loop.addArrayElementAssignment(array, ADD.on(i, INT.literal(1)), i);
	
	IVariableAssignment ass3 = loop.addAssignment(i, ADD.on(i, INT.literal(1)));
	IReturn addReturn2 = body.addReturn(array);

}
