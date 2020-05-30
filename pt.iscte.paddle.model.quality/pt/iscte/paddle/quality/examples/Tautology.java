package pt.iscte.paddle.quality.examples;

import static pt.iscte.paddle.model.IType.*;

import pt.iscte.paddle.model.IBlock;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IReturn;
import pt.iscte.paddle.model.ISelection;
import pt.iscte.paddle.model.IType;
import pt.iscte.paddle.model.IVariableDeclaration;
import pt.iscte.paddle.model.tests.BaseTest;

public class Tautology extends BaseTest {
	
	IProcedure isFirstElement = getModule().addProcedure(IType.BOOLEAN);
	IBlock body = isFirstElement.getBody();
	IVariableDeclaration a = isFirstElement.addParameter(INT.array().reference());
	IVariableDeclaration b = isFirstElement.addParameter(INT);
	IVariableDeclaration isFirst = body.addVariable(BOOLEAN, BOOLEAN.literal(false));
	ISelection selection = body.addSelection(isFirst.expression());
	IReturn r = selection.addReturn(BOOLEAN.literal(true));
	IReturn r2 = body.addReturn(BOOLEAN.literal(false));

}
