package pt.iscte.paddle.codequality.tests.linter;

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

public class MagicNumbers extends BaseTest {

	
	IProcedure magicNumbers = module.addProcedure(IType.VOID);
	IBlock body = magicNumbers.getBody();
	IVariableDeclaration a = body.addVariable(INT, INT.literal(10));
	IVariableDeclaration b = body.addVariable(INT, IOperator.ADD.on(a, INT.literal(10)));
	
}
