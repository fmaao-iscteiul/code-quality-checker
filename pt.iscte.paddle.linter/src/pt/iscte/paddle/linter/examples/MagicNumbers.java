package pt.iscte.paddle.linter.examples;

import static pt.iscte.paddle.model.IType.*;

import pt.iscte.paddle.model.IBlock;
import pt.iscte.paddle.model.IOperator;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IReturn;
import pt.iscte.paddle.model.IVariableDeclaration;
import pt.iscte.paddle.model.roles.IGatherer.Operation;
import pt.iscte.paddle.model.tests.BaseTest;

public class MagicNumbers extends BaseTest {

	
	IProcedure triangleArea = module.addProcedure(DOUBLE);
	IBlock body = triangleArea.getBody();
	IVariableDeclaration radius = triangleArea.addParameter(INT);
	IReturn r = body.addReturn(IOperator.MUL.on(DOUBLE.literal(3.14), IOperator.MUL.on(radius, radius)));
}
