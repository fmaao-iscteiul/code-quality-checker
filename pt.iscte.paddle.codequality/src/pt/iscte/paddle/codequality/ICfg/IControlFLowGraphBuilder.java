package pt.iscte.paddle.codequality.ICfg;

import pt.iscte.paddle.codequality.cfg.Builder;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.model.cfg.IControlFlowGraph;

public interface IControlFLowGraphBuilder {
	
	/** This will supply the path to which will be highlighted in the UI. */
	public void getErrorPath();
	/** Loads a new instance of the Control Flow Graph into the Builder's Object. */
	void load();
	/** Build the Control Flow Graph */
	void build();
	void display();
	IControlFlowGraph getCFG();
	
	static Builder create(IProcedure procedure) {
		return new Builder(procedure);
	}
}
