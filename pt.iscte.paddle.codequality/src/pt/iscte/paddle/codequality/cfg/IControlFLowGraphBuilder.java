package pt.iscte.paddle.codequality.cfg;

import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.model.cfg.IControlFlowGraph;

public interface IControlFLowGraphBuilder {
	
	/** This will supply the path to which will be highlighted in the UI. */
	void getErrorPath();
	/** Loads a new instance of the Control Flow Graph into the Builder's Object. */
	void loadCFG();
	/** Build the Control Flow Graph */
	void loadVisitors();
	void build();
	void displayCFG();
}
