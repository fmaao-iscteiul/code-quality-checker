package pt.iscte.paddle.codequality.Icfg;

import pt.iscte.paddle.codequality.cfg.Builder;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.model.cfg.IControlFlowGraph;

public interface IControlFlowGraphBuilder {
	
	/** This will supply the path to which will be highlighted in the UI. */
	public void getErrorPath();
	/** Loads new instances of the Control Flow Graph and the target Procedure's visitor, into the Builder's Object. */
	void build();
	/** Displays the Control Flow Graph by printing its nodes and their connections. */
	void display();
	/**
	 * @return The instance of the built Control Flow Graph.
	 */
	IControlFlowGraph getCFG();
	/**
	 * 
	 * @param procedure The target procedure that will be represented by the new Control Flow Graph. 
	 * @return A new Builder instance responsible for the construction of Control Flow Graph, regarding the supplied procedure's.
	 */
	public static Builder create(IProcedure procedure) {
		return new Builder(procedure);
	}
}
