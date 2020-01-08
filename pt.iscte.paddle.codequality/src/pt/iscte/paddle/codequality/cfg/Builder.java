package pt.iscte.paddle.codequality.cfg;

import pt.iscte.paddle.codequality.ICfg.IControlFLowGraphBuilder;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.model.cfg.IControlFlowGraph;

public class Builder implements IControlFLowGraphBuilder{

	private IProcedure procedure;
	private IControlFlowGraph cfg;
	private IVisitor visitor;

	public Builder(IProcedure procedure){
		this.procedure = procedure;
		
		this.load();
		build();
	}
	
	@Override
	public void load(){
		this.cfg = IControlFlowGraph.create(procedure);
		this.visitor = new Visitor(this.cfg);
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
		this.cfg.getNodes().forEach(n -> System.out.println(n));
	}

	public IControlFlowGraph getCFG() {
		return this.cfg;
	}

	public IProcedure getProcedure() {
		return this.procedure;
	}
}
