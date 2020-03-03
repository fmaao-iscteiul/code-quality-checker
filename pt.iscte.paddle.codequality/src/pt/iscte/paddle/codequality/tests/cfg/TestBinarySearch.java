package pt.iscte.paddle.codequality.tests.cfg;

import static org.junit.Assert.assertTrue;
import static pt.iscte.paddle.model.IOperator.ADD;
import static pt.iscte.paddle.model.IOperator.EQUAL;
import static pt.iscte.paddle.model.IOperator.IDIV;
import static pt.iscte.paddle.model.IOperator.SMALLER;
import static pt.iscte.paddle.model.IOperator.SMALLER_EQ;
import static pt.iscte.paddle.model.IOperator.SUB;
import static pt.iscte.paddle.model.IType.BOOLEAN;
import static pt.iscte.paddle.model.IType.INT;

import org.junit.Test;

import pt.iscte.paddle.codequality.cfg.Builder;
import pt.iscte.paddle.model.IBlock;
import pt.iscte.paddle.model.ILoop;
import pt.iscte.paddle.model.IModule;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IReturn;
import pt.iscte.paddle.model.ISelection;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.IVariableDeclaration;
import pt.iscte.paddle.model.cfg.IBranchNode;
import pt.iscte.paddle.model.cfg.IControlFlowGraph;
import pt.iscte.paddle.model.cfg.IStatementNode;
import pt.iscte.paddle.model.tests.BaseTest;

public class TestBinarySearch extends BaseTest {

	IModule module = IModule.create();
	IProcedure binarySearch = module.addProcedure(BOOLEAN);
	IVariableDeclaration array = binarySearch.addParameter(INT.array().reference());
	IVariableDeclaration e = binarySearch.addParameter(INT);
	IBlock body = binarySearch.getBody();

	IVariableDeclaration l = body.addVariable(INT);
	IVariableAssignment ass1 = body.addAssignment(l, INT.literal(0));
	IVariableDeclaration r = body.addVariable(INT);
	IVariableAssignment ass2 = body.addAssignment(l, SUB.on(array.length(), INT.literal(1)));

	ILoop loop = body.addLoop(SMALLER_EQ.on(l, r));
	IVariableDeclaration m = loop.addVariable(INT);
	IVariableAssignment ass3 = body.addAssignment(m, ADD.on(l, IDIV.on(SUB.on(r, l), INT.literal(2))));

	ISelection iffound = loop.addSelection(EQUAL.on(array.element(m), e));
	IReturn retTrue = iffound.getBlock().addReturn(BOOLEAN.literal(true));

	ISelection ifnot = loop.addSelectionWithAlternative(SMALLER.on(array.element(m), e));
	IVariableAssignment lAss = ifnot.getBlock().addAssignment(l, ADD.on(m, INT.literal(1)));
	IVariableAssignment rAss = ifnot.getAlternativeBlock().addAssignment(r, SUB.on(m, INT.literal(1)));

	IReturn ret = body.addReturn(BOOLEAN.literal(false));

	@Test
	public void TestBrinarySearch() {

		super.setup();

		IControlFlowGraph cfg = IControlFlowGraph.create(binarySearch);

		IStatementNode s_l = cfg.newStatement(ass1);
		cfg.getEntryNode().setNext(s_l);

		IStatementNode s_r = cfg.newStatement(ass2);
		s_l.setNext(s_r);

		IBranchNode b_loop = cfg.newBranch(loop.getGuard());
		s_r.setNext(b_loop);

		IStatementNode s_m = cfg.newStatement(ass3);
		b_loop.setNext(s_m);

		IBranchNode b_ifFound = cfg.newBranch(iffound.getGuard());
		b_loop.setBranch(b_ifFound);

		IStatementNode s_retTrue = cfg.newStatement(retTrue);
		b_ifFound.setBranch(s_retTrue);
		s_retTrue.setNext(cfg.getExitNode());

		IBranchNode b_ifNot = cfg.newBranch(ifnot.getGuard());
		b_ifFound.setNext(b_ifNot);

		IStatementNode s_lAss = cfg.newStatement(lAss);
		b_ifNot.setBranch(s_lAss);
		s_lAss.setNext(b_loop);

		IStatementNode s_rAss = cfg.newStatement(rAss);
		b_ifNot.setNext(s_rAss);
		s_rAss.setNext(b_loop);

		IStatementNode s_retFalse = cfg.newStatement(ret);
		s_m.setNext(s_retFalse);

		s_retFalse.setNext(cfg.getExitNode());

		Builder cfgBuilder = new Builder(binarySearch); 
		System.out.println("------------------------------");
		System.out.println(binarySearch);
		cfg.getNodes().forEach(node -> System.out.println(node));
		System.out.println("------------------------------");
		cfgBuilder.display();

		assertTrue("The CFG's are not equal.", cfgBuilder.getCFG().isEquivalentTo(cfg));
	}
}
