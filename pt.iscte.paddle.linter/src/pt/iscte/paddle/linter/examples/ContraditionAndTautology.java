package pt.iscte.paddle.linter.examples;

import static pt.iscte.paddle.model.IType.BOOLEAN;
import static pt.iscte.paddle.model.IType.INT;

import pt.iscte.paddle.model.IBlock;
import pt.iscte.paddle.model.IOperator;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.ISelection;
import pt.iscte.paddle.model.IType;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.IVariableDeclaration;
import pt.iscte.paddle.model.tests.BaseTest;

public class ContraditionAndTautology extends BaseTest {

	IProcedure TautologyAndContradiction = module.addProcedure(IType.VOID);
	IBlock body = TautologyAndContradiction.getBody();
	IVariableDeclaration a = body.addVariable(INT, INT.literal(1));
	IVariableDeclaration b = body.addVariable(BOOLEAN, BOOLEAN.literal(true));
	IVariableDeclaration c = body.addVariable(INT, INT.literal(10));
//	IVariableAssignment c1 = body.addAssignment(c, b.expression());
	ISelection selection = body.addSelectionWithAlternative(b.expression());
	IVariableAssignment a2 = selection.addAssignment(a, INT.literal(2));
	IVariableAssignment a3 = selection.getAlternativeBlock().addAssignment(c, a.expression());
//	IVariableAssignment c2 = body.addAssignment(c, INT.literal(20));
}
