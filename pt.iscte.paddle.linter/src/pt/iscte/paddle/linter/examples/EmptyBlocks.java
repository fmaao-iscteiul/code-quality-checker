package pt.iscte.paddle.linter.examples;

import static pt.iscte.paddle.model.IOperator.GREATER;
import static pt.iscte.paddle.model.IType.INT;

import pt.iscte.paddle.model.IOperator;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IReturn;
import pt.iscte.paddle.model.ISelection;
import pt.iscte.paddle.model.IType;
import pt.iscte.paddle.model.IVariableDeclaration;
import pt.iscte.paddle.model.tests.BaseTest;

public class EmptyBlocks extends BaseTest {

	IProcedure greaterThen = getModule().addProcedure(IType.BOOLEAN);
	IVariableDeclaration a = greaterThen.addParameter(INT);
	IVariableDeclaration b = greaterThen.addParameter(INT);
	ISelection ifstat = greaterThen.getBody().addSelection(GREATER.on(a, b));
	IReturn ra = ifstat.addReturn(IType.BOOLEAN.literal(true));
	ISelection ifstat2 = greaterThen.getBody().addSelection(IOperator.SMALLER.on(a, b));
	
}
