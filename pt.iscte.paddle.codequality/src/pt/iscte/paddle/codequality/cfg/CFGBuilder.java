package pt.iscte.paddle.codequality.cfg;

import static pt.iscte.paddle.model.IOperator.GREATER;
import static pt.iscte.paddle.model.IOperator.SMALLER;
import static pt.iscte.paddle.model.IType.INT;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import pt.iscte.paddle.model.cfg.impl.*;
import pt.iscte.paddle.javali.translator.Translator;
import pt.iscte.paddle.model.IBlock;
import pt.iscte.paddle.model.ILoop;
import pt.iscte.paddle.model.IModule;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IReturn;
import pt.iscte.paddle.model.ISelection;
import pt.iscte.paddle.model.IVariable;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.model.cfg.IBranchNode;
import pt.iscte.paddle.model.cfg.IControlFlowGraph;
import pt.iscte.paddle.model.cfg.IStatementNode;

public class CFGBuilder implements IControlFLowGraphBuilder{

	private IProcedure procedure;
	private IControlFlowGraph CFG;

	/**
	 * This visitors will search the code for the correspondent structures and feed the CFG accordingly.
	 */
	private ArrayList<IVisitor> visitors;


	public CFGBuilder(IProcedure procedure){
		this.procedure = procedure;
		this.visitors = new ArrayList<IVisitor>();
	}

	@Override
	public void loadCFG(){
		this.CFG = IControlFlowGraph.create(procedure);
	}

	@Override
	public void loadVisitors() {
		this.visitors.add(new CFGVisitor(this.CFG));
	}

	@Override
	public void build() {
		for (IVisitor visitor : this.visitors) {
			this.procedure.accept(visitor);
		}
	}

	@Override
	public void getErrorPath() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void displayCFG() {
		this.CFG.getNodes().forEach(n -> System.out.println(n));
	}

	public IControlFlowGraph getCFG() {
		return this.CFG;
	}

	public IProcedure getProcedure() {
		return this.procedure;
	}


	public static void main(String[] args) {

//		Translator translator = new Translator(new File("test.javali").getAbsolutePath());
//		IModule modelo = translator.createProgram();
//		IProcedure procedure = modelo.getProcedures().iterator().next(); // first procedure
//
//		CFGBuilder cfgBuilder = new CFGBuilder(procedure);
//		cfgBuilder.loadCFG();
//		cfgBuilder.loadVisitors();
//
//		cfgBuilder.build();
//		
//		cfgBuilder.displayCFG();

		IModule module = IModule.create();
		IProcedure max = module.addProcedure(INT);
		IVariable array = max.addParameter(INT.array());
		IBlock body = max.getBody();
		IVariable m = body.addVariable(INT);
		IVariableAssignment mAss = body.addAssignment(m, array.element(INT.literal(0)));
		IVariable i = body.addVariable(INT);
		IVariableAssignment iAss = body.addAssignment(i, INT.literal(1));
		ILoop loop = body.addLoop(SMALLER.on(i, array.length()));
		ISelection ifstat = loop.addSelection(GREATER.on(array.element(i), m));
		IVariableAssignment mAss_ = ifstat.addAssignment(m, array.element(i));
		IVariableAssignment iInc = loop.addIncrement(i);
		IReturn ret = body.addReturn(m);
		

		max.setId("max");
		array.setId("array");
		m.setId("m");
		i.setId("i");
//		System.out.println(max);
		
		File codeToCheck = new File("test.javali");
		Translator translator = new Translator(codeToCheck.getAbsolutePath());
		IModule module1 = translator.createProgram();
		IProcedure procedure1 = module1.getProcedures().iterator().next(); // first procedure
		
		CFGBuilder cfgBuilder = new CFGBuilder(procedure1);
		cfgBuilder.loadCFG();
		cfgBuilder.loadVisitors();
 
		cfgBuilder.build();
		
		cfgBuilder.displayCFG();

		IControlFlowGraph cfg = IControlFlowGraph.create(max);

		IStatementNode s_mAss = cfg.newStatement(mAss);
		cfg.getEntryNode().setNext(s_mAss);

		IStatementNode s_iAss = cfg.newStatement(iAss);
		s_mAss.setNext(s_iAss);

		IBranchNode b_loop = cfg.newBranch(loop.getGuard());
		s_iAss.setNext(b_loop);

		IBranchNode b_if = cfg.newBranch(ifstat.getGuard());
		b_loop.setBranch(b_if);

		IStatementNode s_mAss_ = cfg.newStatement(mAss_);
		b_if.setBranch(s_mAss_);

		IStatementNode s_iInc = cfg.newStatement(iInc);
		b_if.setNext(s_iInc);
		s_mAss_.setNext(s_iInc);

		s_iInc.setNext(b_loop);

		IStatementNode s_ret = cfg.newStatement(ret);
		b_loop.setNext(s_ret);

		s_ret.setNext(cfg.getExitNode());
		
		System.out.println("------------------------------------------------------");

		cfg.getNodes().forEach(n -> System.out.println(n));
	}
}
