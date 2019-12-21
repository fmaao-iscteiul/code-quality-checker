package pt.iscte.paddle.codequality.cfg;

import static pt.iscte.paddle.model.IOperator.GREATER;
import static pt.iscte.paddle.model.IOperator.SMALLER;
import static pt.iscte.paddle.model.IType.INT;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
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
import pt.iscte.paddle.model.cfg.INode;
import pt.iscte.paddle.model.cfg.IStatementNode;

public class CFGBuilder implements IControlFLowGraphBuilder{

	private IProcedure procedure;
	private IControlFlowGraph CFG;

	private CFGVisitor visitor;

	public CFGBuilder(IProcedure procedure){
		this.procedure = procedure;
		
		load();
		loadVisitor();
		build();
	}
	
	@Override
	public void load(){
		this.CFG = IControlFlowGraph.create(procedure);
	}

	@Override
	public void loadVisitor() {
		this.visitor = new CFGVisitor(this.CFG);
	}

	@Override
	public void build() {
		this.procedure.accept(visitor);
	}

	@Override
	public void getErrorPath() {
	}

	@Override
	public void display() {
		this.CFG.getNodes().forEach(n -> System.out.println(n));
	}

	public IControlFlowGraph getCFG() {
		return this.CFG;
	}

	public IProcedure getProcedure() {
		return this.procedure;
	}
}
