package pt.iscte.paddle.codequality.tests.linter;

import static pt.iscte.paddle.model.IType.INT;

import pt.iscte.paddle.model.IArrayElementAssignment;
import pt.iscte.paddle.model.IBlock;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IProcedureCall;
import pt.iscte.paddle.model.IReturn;
import pt.iscte.paddle.model.ISelection;
import pt.iscte.paddle.model.IType;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.IVariableDeclaration;
import pt.iscte.paddle.model.tests.BaseTest;

public class UselessAssignments extends BaseTest {

	IProcedure returnsArray = module.addProcedure(INT.array().reference());
	IBlock body = returnsArray.getBody();
	IVariableDeclaration n = returnsArray.addParameter(INT);
	IVariableDeclaration array = body.addVariable(INT.array().reference(), INT.array().heapAllocation(INT.literal(5)));
	IArrayElementAssignment arrAss = body.addArrayElementAssignment(array, INT.literal(10), INT.literal(0));
	IReturn returnTrue = body.addReturn(array);
	
	IProcedure uselessAssignments = module.addProcedure(IType.VOID);
	IBlock body2 = uselessAssignments.getBody();
	
	IVariableDeclaration array1 = body2.addVariable(INT.array().reference(), INT.array().heapAllocation(INT.literal(5)));
	IVariableDeclaration array2 = body2.addVariable(INT.array().reference(), INT.array().heapAllocation(INT.literal(5)));
	
	ISelection selection = body2.addSelectionWithAlternative(IType.BOOLEAN.literal(true));
	IVariableAssignment useless3 = selection.addAssignment(array2, returnsArray.expression(array1));
	IVariableAssignment useless31 = selection.getAlternativeBlock().addAssignment(array2, array1);
//	IVariableAssignment useless32 = selection.getAlternativeBlock().addAssignment(array2, array1);
	IVariableAssignment useless4 = selection.addAssignment(array1, array2);
//	IVariableAssignment useless45 = selection.addAssignment(array1, array2);
	IVariableAssignment useless312 = body2.addAssignment(array2, array1);
	
	IVariableDeclaration n2 = body2.addVariable(INT, INT.literal(10));
	IProcedureCall call = body2.addCall(returnsArray, n2);
	IVariableAssignment useless5 = body2.addAssignment(n2, INT.literal(20));
	
	IVariableDeclaration var = body2.addVariable(INT);
	IVariableAssignment useless2 = body2.addAssignment(var, var);
	
	
	
}
