package pt.iscte.paddle.codequality.misc;

import static pt.iscte.paddle.model.IType.INT;

import pt.iscte.paddle.model.IBlock;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IType;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.IVariableDeclaration;
import pt.iscte.paddle.model.tests.BaseTest;

public class SelfAssignment extends BaseTest {
	
	IProcedure uselessAssignment = module.addProcedure(IType.VOID);
	IBlock body2 = uselessAssignment.getBody();

	IVariableDeclaration var = body2.addVariable(INT, INT.literal(10));
	IVariableAssignment a1 = body2.addAssignment(var, var);

}
