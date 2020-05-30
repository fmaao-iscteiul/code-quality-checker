package pt.iscte.paddle.quality.examples;

import static pt.iscte.paddle.model.IType.INT;

import org.junit.Test;

import pt.iscte.paddle.model.IArrayElementAssignment;
import pt.iscte.paddle.model.IBlock;
import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.model.IControlStructure;
import pt.iscte.paddle.model.IOperator;
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
	//	IArrayElementAssignment arrAss = body.addArrayElementAssignment(array, INT.literal(10), INT.literal(0));
	IReturn returnTrue = body.addReturn(array);

	IProcedure uselessAssignments = module.addProcedure(IType.VOID);
	IBlock body2 = uselessAssignments.getBody();

	IVariableDeclaration a = body2.addVariable(INT, INT.literal(10));
	IVariableDeclaration b = body2.addVariable(INT);
	ISelection selection = body2.addSelectionWithAlternative(IOperator.GREATER.on(a, INT.literal(10)));
	IVariableAssignment a1 = selection.addAssignment(a, INT.literal(15));
	IVariableAssignment a2 = selection.getAlternativeBlock().addAssignment(a, INT.literal(20));
	IVariableAssignment a3 = selection.getAlternativeBlock().addAssignment(a, INT.literal(20));
//	IVariableAssignment b1 = selection.getAlternativeBlock().addAssignment(b, a);
	IVariableAssignment a4 = body2.addAssignment(a, INT.literal(20));
	


	@Test
	public void test() {
		uselessAssignments.accept(new IVisitor() {
			@Override
			public boolean visit(IVariableAssignment assignment) {
				System.out.println("------------------------------------");
				System.out.println("assignment: " + assignment.getParent());
				System.out.println("------------------------------------");
				return false;
			}

		});
	}

}
