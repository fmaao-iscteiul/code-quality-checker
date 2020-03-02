package pt.iscte.paddle.codequality.tests.linter;

import static org.junit.Assert.assertEquals;
import static pt.iscte.paddle.model.IOperator.ADD;
import static pt.iscte.paddle.model.IOperator.SMALLER;
import static pt.iscte.paddle.model.IType.INT;
import static pt.iscte.paddle.model.IType.BOOLEAN;

import pt.iscte.paddle.interpreter.IArray;
import pt.iscte.paddle.interpreter.IExecutionData;
import pt.iscte.paddle.model.IArrayElementAssignment;
import pt.iscte.paddle.model.IBlock;
import pt.iscte.paddle.model.ILoop;
import pt.iscte.paddle.model.IOperator;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IReturn;
import pt.iscte.paddle.model.ISelection;
import pt.iscte.paddle.model.IVariable;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.tests.BaseTest;
import pt.iscte.paddle.model.tests.BaseTest.Case;

public class EmptySelection extends BaseTest{
	
	IProcedure naturals = module.addProcedure(INT.array());
	IVariable n = naturals.addParameter(INT);
	IVariable b = naturals.addParameter(BOOLEAN);
	IBlock body = naturals.getBody();
	IVariable array = body.addVariable(INT.array());
	IVariableAssignment ass1 = body.addAssignment(array, INT.array().stackAllocation(n));
	ISelection selection_true = body.addSelection(IOperator.EQUAL.on(n, INT.literal(10)));
	ISelection selection_false = body.addSelection(IOperator.EQUAL.on(b, BOOLEAN.literal(false)));
	IVariable i = body.addVariable(INT, INT.literal(0));
	ILoop loop = body.addLoop(SMALLER.on(i, n));
	IArrayElementAssignment ass2 = loop.addArrayElementAssignment(array, ADD.on(i, INT.literal(1)), i);
	IVariableAssignment ass3 = loop.addAssignment(i, ADD.on(i, INT.literal(1)));
	IReturn addReturn = body.addReturn(array);
	
	@Case("0")
	public void testEmpty(IExecutionData data) {
		IArray array = (IArray) data.getReturnValue();
		assertEquals(0, array.getLength());
	}
	
	@Case("5")
	public void lengthFive(IExecutionData data) {
		IArray array = (IArray) data.getReturnValue();
		assertEquals(5, array.getLength());
		for(int x = 0; x < 5; x++)
			equal(x+1, array.getElement(x));
	}

}
