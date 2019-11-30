package pt.iscte.paddle.codequality.cfg;

import static pt.iscte.paddle.model.IOperator.GREATER;
import static pt.iscte.paddle.model.IOperator.SMALLER;
import static pt.iscte.paddle.model.IType.INT;

import java.io.File;
import java.util.ArrayList;

import pt.iscte.paddle.javali.translator.Translator;
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

public class Teste {
	
	public static void main(String[] args) {
		
		File codeToCheck = new File("test2.javali");
		Translator translator = new Translator(codeToCheck.getAbsolutePath());
		IModule module = translator.createProgram();
		IProcedure procedure = module.getProcedures().iterator().next(); // first procedure
		
		CFGBuilder cfgBuilder = new CFGBuilder(procedure);
		cfgBuilder.loadCFG();
		cfgBuilder.loadVisitors();
		cfgBuilder.build();
		cfgBuilder.displayCFG();
		

//		IModule module = IModule.create();
//		IProcedure max = module.addProcedure(INT);
//		IVariable array = max.addParameter(INT.array());
//		IBlock body = max.getBody();
//		IVariable m = body.addVariable(INT);
//		IVariableAssignment mAss = body.addAssignment(m, array.element(INT.literal(0)));
//		IVariable i = body.addVariable(INT);
//		IVariableAssignment iAss = body.addAssignment(i, INT.literal(1));
//		ILoop loop = body.addLoop(SMALLER.on(i, array.length()));
//		ISelection ifstat = loop.addSelection(GREATER.on(array.element(i), m));
//		IVariableAssignment mAss_ = ifstat.addAssignment(m, array.element(i));
//		IVariableAssignment iInc = loop.addIncrement(i);
//		IReturn ret = body.addReturn(m);
//		
//
//		max.setId("max");
//		array.setId("array");
//		m.setId("m");
//		i.setId("i");
//
//		IControlFlowGraph cfg = IControlFlowGraph.create(max);
//
//		IStatementNode s_mAss = cfg.newStatement(mAss);
//		cfg.getEntryNode().setNext(s_mAss);
//
//		IStatementNode s_iAss = cfg.newStatement(iAss);
//		s_mAss.setNext(s_iAss);
//
//		IBranchNode b_loop = cfg.newBranch(loop.getGuard());
//		s_iAss.setNext(b_loop);
//
//		IBranchNode b_if = cfg.newBranch(ifstat.getGuard());
//		b_loop.setBranch(b_if);
//
//		IStatementNode s_mAss_ = cfg.newStatement(mAss_);
//		b_if.setBranch(s_mAss_);
//
//		IStatementNode s_iInc = cfg.newStatement(iInc);
//		b_if.setNext(s_iInc);
//		s_mAss_.setNext(s_iInc);
//
//		s_iInc.setNext(b_loop);
//
//		IStatementNode s_ret = cfg.newStatement(ret);
//		b_loop.setNext(s_ret);
//
//		s_ret.setNext(cfg.getExitNode());
//		
//		cfg.getNodes().forEach(n -> System.out.println(n));
	}

}
