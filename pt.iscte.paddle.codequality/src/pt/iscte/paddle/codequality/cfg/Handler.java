package pt.iscte.paddle.codequality.cfg;

import pt.iscte.paddle.codequality.ICfg.BRANCH_TYPE_STATE;
import pt.iscte.paddle.codequality.ICfg.IVisitHandler;
import pt.iscte.paddle.model.cfg.IBranchNode;
import pt.iscte.paddle.model.cfg.IControlFlowGraph;
import pt.iscte.paddle.model.cfg.INode;

public class Handler implements IVisitHandler{
	
	IControlFlowGraph cfg;
	Visitor visitor;
	
	public Handler(IControlFlowGraph cfg, Visitor visitor) {
		this.cfg = cfg;
		this.visitor = visitor;
	}

	/**
	 * @function HandleStatementVisit
	 * @description Checks for the type of the previous node, and acts accordingly. This meaning that it will place itself has the next Statement node for the previous one.
	 * @param INode The statement that is has been visited and will be inserted in the Control Flow Graph.
	 */
	@Override
	public void handleStatementVisit(INode statement) {
	
		if(visitor.getCurrentBranchType() != BRANCH_TYPE_STATE.ALTERNATIVE)
			this.handleOrphansAdoption(statement);

		this.setLastBreakNext(statement);
		this.setLastSelectionNext(statement);		
		this.setLastLoopNext(statement);
		
		INode lastNode = visitor.getLastNode();
		if(lastNode == null) 
			this.cfg.getEntryNode().setNext(statement);
		else if(lastNode instanceof IBranchNode && !((IBranchNode) lastNode).hasBranch()) 
			((IBranchNode) lastNode).setBranch(statement);
		/* The middle condition is duo to the possibility of having an assignment inside an else, that can't be set as the lastNode's next.*/
		else if(lastNode != null && !(lastNode instanceof IBranchNode) 
				&& (visitor.getSelectionNodeStack().size() == 0 || !visitor.getSelectionNodeStack().peek().orphans.contains(lastNode)) 
					&& lastNode.getNext() == null)
			lastNode.setNext(statement);
	}

	/**
	 * @function handleLoopBranchVisit
	 * @description Checks for the type of the previous node, and acts accordingly. 
	 * This meaning that it will place itself has the next loop Branch node for the previous one.
	 * @param IBranchNode branch The statement that is has been visited and will be inserted in the Control Flow Graph.
	 */
	@Override
	public void handleBranchVisit(IBranchNode branch) {		
		this.handleOrphansAdoption(branch);
		this.setLastBreakNext(branch);
		this.setLastSelectionNext(branch);
		this.setLastLoopNext(branch);

		INode lastNode = visitor.getLastNode();
		if(lastNode instanceof IBranchNode && !((IBranchNode) lastNode).hasBranch()) ((IBranchNode) lastNode).setBranch(branch);
		else if(lastNode != null && lastNode.getNext() == null) lastNode.setNext(branch);
		else if( lastNode == null ) this.cfg.getEntryNode().setNext(branch);
	}
	
	@Override
	public void setLastLoopNext(INode node) {
		if(visitor.getLastLoopNode() != null && visitor.getLastLoopNode().hasBranch() && visitor.getLastNode().getNext() != null) {
			visitor.getLastLoopNode().setNext(node);
			visitor.setLastLoopNode(null);
		}
	}
	
	public void setLastSelectionNext(INode node) {
		if(visitor.getLastSelectionBranch() != null && visitor.getLastSelectionBranch().hasBranch() && visitor.getLastSelectionBranch().getNext() == null) {
			visitor.getLastSelectionBranch().setNext(node);
			visitor.setlastSelectionNode(null);
		}
	}
	
	
	public void setLastBreakNext(INode node) {
		if(visitor.getLastBreakStatement() != null) {
			visitor.getLastBreakStatement().setNext(node);
			visitor.setLastBreakStatement(null);
		}
	}
	
	public void handleOrphansAdoption(INode node) {
		if(visitor.getLastSelectionNode() != null && visitor.getLastSelectionOrphans().size() > 0) {
			visitor.adoptOrphans(visitor.getLastSelectionNode(), node);
		}
			
	}	

}
