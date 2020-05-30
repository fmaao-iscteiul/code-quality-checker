package pt.iscte.paddle.quality.examples;

import static pt.iscte.paddle.model.IType.BOOLEAN;
import static pt.iscte.paddle.model.IType.INT;

import pt.iscte.paddle.model.IBlock;
import pt.iscte.paddle.model.ILoop;
import pt.iscte.paddle.model.IOperator;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IReturn;
import pt.iscte.paddle.model.ISelection;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.IVariableDeclaration;
import pt.iscte.paddle.model.tests.BaseTest;

public class SelectionMisconception extends BaseTest {
	
	IProcedure naturals = module.addProcedure(INT);
	IVariableDeclaration n = naturals.addParameter(INT);
	IBlock body = naturals.getBody();
	IVariableDeclaration i = body.addVariable(INT, INT.literal(0));
	IVariableDeclaration b = body.addVariable(BOOLEAN, BOOLEAN.literal(false));
	ILoop loop = body.addLoop(BOOLEAN.literal(false));
	ISelection selection = body.addSelection(IOperator.EQUAL.on(b, BOOLEAN.literal(false)));
	ISelection selection0 = body.addSelection(BOOLEAN.literal(false));
	ISelection selection1 = body.addSelection(BOOLEAN.literal(true));
	ISelection selection2 = body.addSelectionWithAlternative(IOperator.EQUAL.on(b, BOOLEAN.literal(false)));
//	IVariableAssignment ass4 = selection.addAssignment(i, INT.literal(10));
	IVariableAssignment ass5 = selection2.getAlternativeBlock().addAssignment(i, INT.literal(10));
	IVariableAssignment ass4 = body.addAssignment(i, INT.literal(30));
	IReturn addReturn = body.addReturn(n);

	
}
