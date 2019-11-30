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
}
