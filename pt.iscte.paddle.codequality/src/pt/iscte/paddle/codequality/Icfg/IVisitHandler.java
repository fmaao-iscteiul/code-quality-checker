package pt.iscte.paddle.codequality.Icfg;

import pt.iscte.paddle.codequality.cfg.Handler;
import pt.iscte.paddle.codequality.cfg.Visitor;
import pt.iscte.paddle.model.cfg.IBranchNode;
import pt.iscte.paddle.model.cfg.IControlFlowGraph;
import pt.iscte.paddle.model.cfg.INode;

public interface IVisitHandler {
	
	/**
	 * Method responsible for handling the supplied statement expression visit.
	 * @param statement The visited statement expression Node.
	 */
	void handleStatementVisit(INode statement);
	
	/**
	 * Method responsible for handling the supplied branch expression visit.
	 * @param branch The visited branch expression Branch Node.
	 */
	void handleBranchVisit(IBranchNode branch);
	
	/**
	 * Checks if the conditions to set the next node of the last visited Loop branch, are met, and if so, set's the last loop
	 * next's as the supplied node.
	 * @param node The current visited node.
	 */
	void setLastLoopNext(INode node);
	
	/**
	 * Checks if the conditions to set the next node of the last visited Selection branch, are met, and if so, set's the last loop
	 * next's as the supplied node.
	 * @param node The current visited node.
	 */
	void setLastSelectionNext(INode node);
	
	/**
	 * Checks for the existence of the last break statement node and if it exists, set's it's next as the supplied node.
	 * @param node The current visited node.
	 */
	void setLastBreakNext(INode node);
	
	/**
	 * Checks for the existence of an orphan list and if there is one, proceeds to the adoption of the found orphan nodes.
	 * @param node The current visited node.
	 */
	void handleOrphansAdoption(INode node);
	
	/**
	 * Checks for the existence of an orphan list and if there is one, proceeds to updating the found orphan list,
	 * by adding the supplied orphan node to the list.
	 * @param orphan The last node of the current selection visit.
	 */
	void updateOrphansList(INode orphan);
	
	/**
	 * Set's the visited return statement node's next, as the current Control Flow Graph exit node.
	 * @param ret The current visited return statement.
	 */
	void setReturnStatementNext(INode ret);
	
	/**
	 * 
	 * @param cfg The current Control Flow Graph instance.
	 * @param visitor The current Procedure's visitor.
	 * @return A new instance of the Handler object, associated to the supplied Control Flow Graph and procedure's visitor.
	 */
	static Handler create(IControlFlowGraph cfg, Visitor visitor) {
		return new Handler(cfg, visitor);
	}
}
