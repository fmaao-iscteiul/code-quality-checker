package pt.iscte.paddle.codequality.tests.linter;

import static pt.iscte.paddle.model.IType.BOOLEAN;
import static pt.iscte.paddle.model.IType.INT;

import pt.iscte.paddle.model.IBlock;
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
	ISelection selection = body.addSelectionWithAlternative(IOperator.EQUAL.on(BOOLEAN.literal(true), BOOLEAN.literal(true)));
//	IVariableAssignment ass4 = selection.addAssignment(i, INT.literal(10));
	IVariableAssignment ass5 = selection.getAlternativeBlock().addAssignment(i, INT.literal(10));
	IReturn addReturn = body.addReturn(n);

	
}
