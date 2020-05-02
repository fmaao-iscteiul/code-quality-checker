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
import pt.iscte.paddle.model.IType;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.IVariableDeclaration;
import pt.iscte.paddle.model.tests.BaseTest;

public class DuplicateGuard extends BaseTest {
	
	IProcedure conditionDoubleCheck = module.addProcedure(IType.VOID);
	IVariableDeclaration n = conditionDoubleCheck.addParameter(INT);
	IVariableDeclaration b = conditionDoubleCheck.addParameter(BOOLEAN);
	IBlock body = conditionDoubleCheck.getBody();
	IVariableDeclaration i = body.addVariable(INT, INT.literal(0));
	ILoop loop = body.addLoop(IOperator.AND.on(IOperator.GREATER.on(n, i), b));
//	IVariableAssignment ass10 = loop.addAssignment(i, IOperator.ADD.on(INT.literal(0), INT.literal(1)));
//	IVariableAssignment ass6 = loop.addAssignment(i, INT.literal(10));
	ISelection selection_true = loop.addSelection(IOperator.GREATER.on(n, i));
//	IVariableAssignment i2 = loop.addAssignment(i, INT.literal(10));
	IVariableAssignment dawwadaw = selection_true.addAssignment(i, ADD.on(i, INT.literal(1)));
//	IVariableAssignment dawdwdfff = loop.addAssignment(i, ADD.on(i, INT.literal(1)));

}
