package pt.iscte.paddle.codequality.tests.cfg;

import static org.junit.Assert.assertTrue;
import static pt.iscte.paddle.model.IOperator.EQUAL;
import static pt.iscte.paddle.model.IOperator.SMALLER;
import static pt.iscte.paddle.model.IType.INT;

import org.junit.Test;

import pt.iscte.paddle.codequality.cfg.Builder;
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
import pt.iscte.paddle.model.tests.BaseTest;

public class TestArrayCount extends BaseTest{
	
	IModule module = IModule.create();
	IProcedure count = module.addProcedure(INT);
	IVariable array = count.addParameter(INT.array().reference());
	IVariable e = count.addParameter(INT);
	IBlock body = count.getBody();
	IVariable c = body.addVariable(INT);
	IVariableAssignment cAss = body.addAssignment(c, INT.literal(0));
	IVariable i = body.addVariable(INT);
	IVariableAssignment iAss = body.addAssignment(i, INT.literal(0));
	ILoop loop = body.addLoop(SMALLER.on(i, array.length()));
	ISelection ifstat = loop.addSelection(EQUAL.on(array.element(i), e));
	IVariableAssignment cAss_ = ifstat.addIncrement(c);
	IVariableAssignment iInc = loop.addIncrement(i);
	IReturn ret = body.addReturn(c);
	
	IControlFlowGraph cfg = IControlFlowGraph.create(count);	
	
	@Test
	public void TEstArrayCount() {
		
		IStatementNode s_cAss = cfg.newStatement(cAss);
		cfg.getEntryNode().setNext(s_cAss);
		
		IStatementNode s_iAss = cfg.newStatement(iAss);
		s_cAss.setNext(s_iAss);
		
		IBranchNode b_loop = cfg.newBranch(loop.getGuard());
		s_iAss.setNext(b_loop);
		
		IBranchNode b_ifstat = cfg.newBranch(ifstat.getGuard());
		b_loop.setBranch(b_ifstat);
		
		IStatementNode s_cAss_ = cfg.newStatement(cAss_);
		b_ifstat.setBranch(s_cAss_);
		
		IStatementNode s_iInc = cfg.newStatement(iInc);
		s_cAss_.setNext(s_iInc);
		b_ifstat.setNext(s_iInc);
		
		s_iInc.setNext(b_loop);
		
		IStatementNode s_ret = cfg.newStatement(ret);
		b_loop.setNext(s_ret);
		
		s_ret.setNext(cfg.getExitNode());
	
		cfg.getNodes().forEach(node -> System.out.println(node));
		Builder cfgBuilder = new Builder(count);
		assertTrue("The CFG's are not equal.", cfgBuilder.getCFG().isEquivalentTo(cfg));	
	}

}
