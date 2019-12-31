package pt.iscte.paddle.codequality.cfgtests;

import static org.junit.Assert.assertTrue;
import static pt.iscte.paddle.model.IOperator.AND;
import static pt.iscte.paddle.model.IOperator.EQUAL;
import static pt.iscte.paddle.model.IOperator.NOT;
import static pt.iscte.paddle.model.IOperator.SMALLER;
import static pt.iscte.paddle.model.IType.BOOLEAN;
import static pt.iscte.paddle.model.IType.INT;

import org.junit.Test;

import pt.iscte.paddle.codequality.cfg.CFGBuilder;
import pt.iscte.paddle.model.IBlock;
import pt.iscte.paddle.model.ILoop;
import pt.iscte.paddle.model.IModule;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IReturn;
import pt.iscte.paddle.model.ISelection;
import pt.iscte.paddle.model.IVariable;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.cfg.IBranchNode;
import pt.iscte.paddle.model.cfg.IControlFlowGraph;
import pt.iscte.paddle.model.cfg.IStatementNode;

public class TestArrayFind {

	
	IModule module = IModule.create();
	IProcedure exists = module.addProcedure(BOOLEAN);
	IVariable array = exists.addParameter(INT.array().reference());
	IVariable e = exists.addParameter(INT);
	IBlock body = exists.getBody();
	IVariable found = body.addVariable(BOOLEAN);
	IVariableAssignment foundAss = body.addAssignment(found, BOOLEAN.literal(false));
	IVariable i = body.addVariable(INT);
	IVariableAssignment iAss = body.addAssignment(i, INT.literal(0));
	ILoop loop = body.addLoop(AND.on(NOT.on(found), SMALLER.on(i, array.length())));
	ISelection ifstat = loop.addSelection(EQUAL.on(array.element(i), e));
	IVariableAssignment foundAss_ = ifstat.addAssignment(found, BOOLEAN.literal(true));
	IVariableAssignment iInc = loop.addIncrement(i);
	IReturn ret = body.addReturn(found);
	
	@Test
	public void TestArrayFind() {
		
		IControlFlowGraph cfg = IControlFlowGraph.create(exists);
		
		IStatementNode s_foundAss = cfg.newStatement(foundAss);
		cfg.getEntryNode().setNext(s_foundAss);
		
		IStatementNode s_iAss = cfg.newStatement(iAss);
		s_foundAss.setNext(s_iAss);
		
		IBranchNode b_loop = cfg.newBranch(loop.getGuard());
		s_iAss.setNext(b_loop);
		
		IBranchNode b_ifstat = cfg.newBranch(ifstat.getGuard());
		b_loop.setBranch(b_ifstat);
		
		IStatementNode s_foundAss_ = cfg.newStatement(foundAss_);
		b_ifstat.setBranch(s_foundAss_);
		
		IStatementNode s_iInc = cfg.newStatement(iInc);
		s_foundAss_.setNext(s_iInc);
		b_ifstat.setNext(s_iInc);
		
		s_iInc.setNext(b_loop);
		
		IStatementNode s_ret = cfg.newStatement(ret);
		b_loop.setNext(s_ret);
		
		s_ret.setNext(cfg.getExitNode());
		
		CFGBuilder cfgBuilder = new CFGBuilder(exists); 
		System.out.println("------------------------------");
		cfg.getNodes().forEach(node -> System.out.println(node));
		System.out.println("------------------------------");
		cfgBuilder.display();
		
		assertTrue("The CFG's are not equal.", cfgBuilder.getCFG().isEquivalentTo(cfg));	
	}
}
