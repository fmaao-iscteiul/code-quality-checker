package issues;

import pt.iscte.paddle.model.IBlock;
import pt.iscte.paddle.model.ILoop;
import pt.iscte.paddle.model.IOperator;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IReturn;
import pt.iscte.paddle.model.ISelection;
import pt.iscte.paddle.model.IType;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.IVariableDeclaration;
import pt.iscte.paddle.model.tests.BaseTest;

public class BooleanReturnCheck extends BaseTest {

	IProcedure contains = module.addProcedure(IType.BOOLEAN);
	IVariableDeclaration list = contains.addParameter(IType.INT.array().reference());
	IVariableDeclaration number = contains.addParameter(IType.INT);
	IBlock body = contains.getBody();
	IVariableDeclaration c = body.addVariable(IType.BOOLEAN);
	IVariableDeclaration i = body.addVariable(IType.INT);
	ILoop loop = body.addLoop(IOperator.SMALLER.on(i, list.length()));
	ISelection selection = loop.addSelection(IOperator.EQUAL.on(list.element(i), number));
	IVariableAssignment ass = selection.addAssignment(c, IType.BOOLEAN.literal(true));
	ISelection selection2 = body.addSelectionWithAlternative(c.expression());
	IReturn return1 = selection2.addReturn(IType.BOOLEAN.literal(true));
	IReturn return2 = selection2.getAlternativeBlock().addReturn(IType.BOOLEAN.literal(true));
	
}