package pt.iscte.paddle.codequality.examples;

import static pt.iscte.paddle.model.IType.INT;

import pt.iscte.paddle.model.ILoop;
import pt.iscte.paddle.model.IOperator;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IReturn;
import pt.iscte.paddle.model.ISelection;
import pt.iscte.paddle.model.IType;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.IVariableDeclaration;
import pt.iscte.paddle.model.tests.BaseTest;

public class BooleanConditionVerbose extends BaseTest {

	IProcedure doesExist = getModule().addProcedure(IType.BOOLEAN);
	IVariableDeclaration a = doesExist.addParameter(INT.array().reference());
	IVariableDeclaration b = doesExist.addParameter(INT);
	IVariableDeclaration i = doesExist.getBody().addVariable(INT, INT.literal(0));
	IVariableDeclaration exists = doesExist.getBody().addVariable(IType.BOOLEAN, IType.BOOLEAN.literal(false));
	
	ILoop loop = doesExist.getBody().addLoop(IOperator.SMALLER.on(i, a.length()));
	ISelection ifstat = loop.addSelection(IOperator.EQUAL.on(b, a.element(i)));
	IVariableAssignment a1 = ifstat.addAssignment(exists, IType.BOOLEAN.literal(true));
	
	ISelection if2 = doesExist.getBody().addSelectionWithAlternative(IOperator.EQUAL.on(exists.expression(), IType.BOOLEAN.literal(true)));
	IReturn r = if2.addReturn(IType.BOOLEAN.literal(true));
	IReturn r2 = if2.getAlternativeBlock().addReturn(IType.BOOLEAN.literal(false));
}
