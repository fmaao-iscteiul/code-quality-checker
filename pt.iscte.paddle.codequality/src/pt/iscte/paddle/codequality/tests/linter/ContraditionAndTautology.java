package pt.iscte.paddle.codequality.tests.linter;

import static pt.iscte.paddle.model.IType.BOOLEAN;
import static pt.iscte.paddle.model.IType.INT;

import pt.iscte.paddle.model.IBlock;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IReturn;
import pt.iscte.paddle.model.ISelection;
import pt.iscte.paddle.model.IType;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.IVariableDeclaration;
import pt.iscte.paddle.model.tests.BaseTest;

public class ContraditionAndTautology extends BaseTest {

	IProcedure TautologyAndContradiction = module.addProcedure(IType.VOID);
	IBlock body = TautologyAndContradiction.getBody();
	IVariableDeclaration a = body.addVariable(INT);
	IVariableAssignment a1 = body.addAssignment(a, INT.literal(1));
	ISelection selection = body.addSelection(BOOLEAN.literal(true));
	IVariableAssignment a2 = selection.addAssignment(a, INT.literal(2));
}
