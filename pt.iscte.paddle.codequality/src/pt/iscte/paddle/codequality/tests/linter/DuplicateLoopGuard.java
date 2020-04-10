package pt.iscte.paddle.codequality.tests.linter;

import static pt.iscte.paddle.model.IOperator.ADD;
import static pt.iscte.paddle.model.IOperator.SMALLER;
import static pt.iscte.paddle.model.IType.BOOLEAN;
import static pt.iscte.paddle.model.IType.INT;

import pt.iscte.paddle.model.IArrayElementAssignment;
import pt.iscte.paddle.model.IBlock;
import pt.iscte.paddle.model.ILiteral;
import pt.iscte.paddle.model.ILoop;
import pt.iscte.paddle.model.IOperator;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IReturn;
import pt.iscte.paddle.model.ISelection;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.IVariableDeclaration;
import pt.iscte.paddle.model.tests.BaseTest;

public class DuplicateLoopGuard extends BaseTest {
	
	IProcedure naturals = module.addProcedure(INT.array());
	IVariableDeclaration n = naturals.addParameter(INT);
	IVariableDeclaration b = naturals.addParameter(BOOLEAN);
	IBlock body = naturals.getBody();
	IVariableDeclaration array = body.addVariable(INT.array());
	IVariableAssignment ass1 = body.addAssignment(array, INT.array().stackAllocation(n));
	IVariableDeclaration i = body.addVariable(INT, INT.literal(0));
	ILoop loop = body.addLoop(IOperator.AND.on(IOperator.OR.on(IOperator.GREATER.on(i, INT.literal(10)), IOperator.GREATER.on(n, INT.literal(10))), IOperator.GREATER.on(i, INT.literal(10))));
//	IVariableAssignment ass10 = loop.addAssignment(i, IOperator.ADD.on(INT.literal(0), INT.literal(1)));
//	IVariableAssignment ass6 = loop.addAssignment(i, INT.literal(10));
	ISelection selection_true = loop.addSelection(IOperator.GREATER.on(i, INT.literal(10)));
//	IVariableAssignment i2 = loop.addAssignment(i, INT.literal(10));
	ISelection selection_true2 = loop.addSelection(SMALLER.on(i, n));
	IVariableAssignment i4 = loop.addAssignment(n, INT.literal(10));
	ISelection selection_true3 = loop.addSelection(SMALLER.on(i, n));
	IVariableAssignment dawwadaw = selection_true.addAssignment(i, ADD.on(i, INT.literal(1)));
//	IVariableAssignment dawdwdfff = loop.addAssignment(i, ADD.on(i, INT.literal(1)));
	IReturn addReturn = body.addReturn(array);

}
