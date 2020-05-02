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

public class FaultyReturns extends BaseTest {
	
	IProcedure naturals = module.addProcedure(BOOLEAN);
	IBlock body = naturals.getBody();
	IVariableDeclaration n = naturals.addParameter(INT);
	IVariableDeclaration b = naturals.addParameter(BOOLEAN);
	IVariableAssignment ass = body.addAssignment(b, BOOLEAN.literal(false));
	ISelection selection = body.addSelectionWithAlternative(b.expression());
	IReturn returnTrue = selection.addReturn(BOOLEAN.literal(true));
	IReturn returnFalse = selection.getAlternativeBlock().addReturn(BOOLEAN.literal(false));
	
}
