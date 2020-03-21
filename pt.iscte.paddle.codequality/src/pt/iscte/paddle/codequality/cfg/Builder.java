package pt.iscte.paddle.codequality.cfg;

import pt.iscte.paddle.codequality.Icfg.IControlFlowGraphBuilder;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IType;
import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.model.cfg.IControlFlowGraph;

public class Builder implements IControlFlowGraphBuilder{

	private IProcedure procedure;
	private IControlFlowGraph cfg;
	private IVisitor visitor;

	public Builder(IProcedure procedure){
		this.procedure = procedure;

		this.cfg = IControlFlowGraph.create(procedure);
		this.visitor = new Visitor(this.cfg);
		
		this.build();
	}

	@Override
	public void build() {
		this.procedure.accept(visitor);
		
		if(this.procedure.getReturnType() == IType.VOID)
			cfg.getNodes().forEach(node -> {
			if(node.getNext() == null && !node.isExit()) node.setNext(cfg.getExitNode());
		});
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
	
	@Override
	public String toString() {
		return this.getCFG().getProcedure().toString();
	}
}
