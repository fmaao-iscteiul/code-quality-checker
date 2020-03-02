package pt.iscte.paddle.codequality.tests.cfg;

import static org.junit.Assert.assertTrue;
import static pt.iscte.paddle.model.IOperator.ADD;
import static pt.iscte.paddle.model.IOperator.DIFFERENT;
import static pt.iscte.paddle.model.IType.DOUBLE;
import static pt.iscte.paddle.model.IType.INT;
import org.junit.Test;

import pt.iscte.paddle.codequality.cfg.Builder;
import pt.iscte.paddle.model.IBlock;
import pt.iscte.paddle.model.ILoop;
import pt.iscte.paddle.model.IModule;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IReturn;
import pt.iscte.paddle.model.IVariable;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.cfg.IBranchNode;
import pt.iscte.paddle.model.cfg.IControlFlowGraph;
import pt.iscte.paddle.model.cfg.IStatementNode;

public class TestSummation {
	IModule module = IModule.create();
	IProcedure summation = module.addProcedure(DOUBLE);
	IVariable array = summation.addParameter(DOUBLE.array().reference());
	IBlock sumBody = summation.getBody();
	IVariable sum = sumBody.addVariable(DOUBLE, DOUBLE.literal(0.0));
	IVariable i = sumBody.addVariable(INT, INT.literal(0));
	ILoop loop = sumBody.addLoop(DIFFERENT.on(i, array.dereference().length()));
	IVariableAssignment ass1 = loop.addAssignment(sum, ADD.on(sum, array.dereference().element(i)));
	IVariableAssignment ass2 = loop.addIncrement(i);
	IReturn ret = sumBody.addReturn(sum);
	
	@Test
	public void TestSomation() {
		
		IControlFlowGraph cfg = IControlFlowGraph.create(summation);
		
		IStatementNode s_sum = cfg.newStatement(sum);
		cfg.getEntryNode().setNext(s_sum);
		
		IStatementNode s_i = cfg.newStatement(i);
		s_sum.setNext(s_i);
		
		IBranchNode b_loop = cfg.newBranch(loop.getGuard());
		s_i.setNext(b_loop);
		
		IStatementNode s_ass1 = cfg.newStatement(ass1);
		b_loop.setBranch(s_ass1);
		
		IStatementNode s_ass2 = cfg.newStatement(ass2);
		s_ass1.setNext(s_ass2);
		
		s_ass2.setNext(b_loop);
		
		IStatementNode s_ret = cfg.newStatement(ret);
		b_loop.setNext(s_ret);
		
		s_ret.setNext(cfg.getExitNode());
		
		Builder cfgBuilder = new Builder(summation); 
		System.out.println("------------------------------");
		cfg.getNodes().forEach(node -> System.out.println(node));
		System.out.println("------------------------------");
		cfgBuilder.display();
		
		assertTrue("The CFG's are not equal.", cfgBuilder.getCFG().isEquivalentTo(cfg));	
	}
}
