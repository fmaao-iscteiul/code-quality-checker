package pt.iscte.paddle.codequality.cfg;
import pt.iscte.paddle.model.ILoop;
import pt.iscte.paddle.model.IReturn;
import pt.iscte.paddle.model.ISelection;
import pt.iscte.paddle.model.IVariableAssignment;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Observable;
import java.util.Queue;

import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.codequality.ICfg.BRANCH_TYPE_STATE;
import pt.iscte.paddle.codequality.ICfg.IVisitHandler;
import pt.iscte.paddle.model.IBlock;
import pt.iscte.paddle.model.IBreak;
import pt.iscte.paddle.model.IConditionalExpression;
import pt.iscte.paddle.model.IContinue;
import pt.iscte.paddle.model.cfg.IBranchNode;
import pt.iscte.paddle.model.cfg.IControlFlowGraph;
import pt.iscte.paddle.model.cfg.INode;
import pt.iscte.paddle.model.cfg.IStatementNode;

public class Visitor implements IVisitor {

	private IControlFlowGraph cfg;
	private IVisitHandler handler;
	
	private INode lastNode = null;
	private SelectionNode lastSelectionNode = null;
	private IBranchNode lastLoopNode = null;
	private IStatementNode lastBreakStatement = null;
	private BRANCH_TYPE_STATE currentBranchType = null;

	private Deque<SelectionNode> selectionNodeStack;
	private Deque<IBranchNode> loopNodeStack;
	private Deque<BreakNode> breakNodeStack;

	static class SelectionNode {
		final IBranchNode node;
		
		final List<INode> orphans;

		SelectionNode(IBranchNode if_branch) {
			node = if_branch;
			orphans = new ArrayList<INode>();
		}
	}
	
	static class BreakNode {
		 IStatementNode node;
		final IBlock parent;

		BreakNode(IStatementNode breakStatement, IBlock parentBlock) {
			node = breakStatement;
			parent = parentBlock;
		}
	}
	

	public Visitor(IControlFlowGraph cfg) { 
		this.cfg = cfg;
		this.handler = IVisitHandler.create(cfg, this);
		
		this.selectionNodeStack = new ArrayDeque<>();
		this.loopNodeStack = new ArrayDeque<>();
		this.breakNodeStack = new ArrayDeque<>();
	}

	@Override
	public boolean visit(IVariableAssignment assignment) {
		IStatementNode assignment_statement = this.cfg.newStatement(assignment);

		handler.handleStatementVisit(assignment_statement);
		setLastNode(assignment_statement);
		return true;
	}

	@Override
	public boolean visit(ISelection selection) {
		IBranchNode if_branch = this.cfg.newBranch(selection.getGuard());

		/* Checks if the previous node is a statement or a branch, and acts accordingly. */
		handler.handleBranchVisit(if_branch);		
		selectionNodeStack.push(new SelectionNode(if_branch));
		/* Pushes the new node into the INode Collection. */
		setLastNode(if_branch);
		setCurrentBranchType(BRANCH_TYPE_STATE.SELECTION);
		return true;
	}

	// Se houver loop acima, definir o next do last node para esse loop, sem por a o lastLoopNode a null
	@Override
	public void endVisit(ISelection selection) {
		currentBranchType = null;
		if(getLastSelectionBranch() != null && getLastSelectionBranch().getNext() == null) 
			this.selectionNodeStack.peek().orphans.add(getLastSelectionBranch());
		setlastSelectionNode(this.selectionNodeStack.pop());
	}

	@Override
	public void endVisitBranch(ISelection selection) {
		if(selectionNodeStack.size() > 0) setCurrentBranchType(BRANCH_TYPE_STATE.SELECTION);
		else currentBranchType = null;
		if(lastNode.getNext() == null 
				&& lastSelectionNode == null 
				&& selectionNodeStack.size() > 0 
				&& !selectionNodeStack.peek().orphans.contains(lastNode)) 
			selectionNodeStack.peek().orphans.add(lastNode);
		
		/* Because of else's after selection, that needs to be set as next.*/
		this.setlastSelectionNode(selectionNodeStack.peek());
	}

	@Override
	public boolean visitAlternative(ISelection selection) {
		setCurrentBranchType(BRANCH_TYPE_STATE.ALTERNATIVE);
		if(lastNode.getNext() == null && selectionNodeStack.size() > 0 && !selectionNodeStack.peek().orphans.contains(lastNode)) {
			selectionNodeStack.peek().orphans.add(lastNode);
		}
		return IVisitor.super.visitAlternative(selection);
	}

	@Override
	public void endVisitAlternative(ISelection selection) {
		if(selectionNodeStack.size() > 0) setCurrentBranchType(BRANCH_TYPE_STATE.SELECTION);
		else setCurrentBranchType(null);

		if(lastNode.getNext() == null && lastSelectionNode == null && 
				!selection.getAlternativeBlock().isEmpty() &&
					selectionNodeStack.size() > 0 && 
						!selectionNodeStack.peek().orphans.contains(lastNode)) {
			selectionNodeStack.peek().orphans.add(lastNode);
		}
	}

	@Override
	public boolean visit(ILoop loop) {
		IBranchNode loop_branch = this.cfg.newBranch(loop.getGuard());
		
		loopNodeStack.push(loop_branch);	
		handler.handleBranchVisit(loop_branch);
		
		setLastNode(loop_branch);
		return true;
	}

	@Override
	public void endVisit(ILoop loop) {
				
		IBranchNode finishedLoopBranch = this.loopNodeStack.pop();
		
		handler.setLastBreakNext(finishedLoopBranch);			
		handler.handleOrphansAdoption(finishedLoopBranch);
		
		if(lastLoopNode != null && lastLoopNode.getNext() == null){
			lastLoopNode.setNext(finishedLoopBranch);
		}
		if(lastNode != null && lastNode.getNext() == null) lastNode.setNext(finishedLoopBranch);
		/* So that the last Node can point to the beginning of the next loop. */
		setLastLoopNode(finishedLoopBranch);
		
		if(breakNodeStack.size() > 0 )
			setLastBreakStatement(this.breakNodeStack.pollLast().node);
	}

	@Override
	public boolean visit(IReturn returnStatement) {
		IStatementNode ret = this.cfg.newStatement(returnStatement);
		
		if(lastNode instanceof IBranchNode && !((IBranchNode) lastNode).hasBranch()) ((IBranchNode) lastNode).setBranch(ret);
		else if(lastNode != null && lastNode.getNext() == null) lastNode.setNext(ret);
		else lastNode.getNext().setNext(ret);
		
		ret.setNext(this.cfg.getExitNode());
		setLastNode(ret);
		return true;
	}

	@Override
	public void visit(IContinue continueStatement) {
		IStatementNode continue_statement = cfg.newStatement(continueStatement);
		if(lastNode instanceof IBranchNode) ((IBranchNode) lastNode).setBranch(continue_statement);
		else lastNode.setNext(continue_statement);
		if(loopNodeStack.peek() != null) continue_statement.setNext(loopNodeStack.peek());
		setLastNode(continue_statement);
	}

	@Override
	public void visit(IBreak breakStatement) {
		IStatementNode break_statement = cfg.newStatement(breakStatement);
		
		if(lastNode instanceof IBranchNode) ((IBranchNode) lastNode).setBranch(break_statement);
		else lastNode.setNext(break_statement);
		
		this.breakNodeStack.add(new BreakNode(break_statement, breakStatement.getParent()));
	}
	
	public IBranchNode getLastLoopNode() {
		return lastLoopNode;
	}
	public void setCurrentBranchType(BRANCH_TYPE_STATE currentBranchType) {
		this.currentBranchType = currentBranchType;
	}
	public BRANCH_TYPE_STATE getCurrentBranchType() {
		return currentBranchType;
	}
	public Deque<SelectionNode> getSelectionNodeStack() {
		return selectionNodeStack;
	}
	public void setSelectionNodeStack(Deque<SelectionNode> selectionNodeStack) {
		this.selectionNodeStack = selectionNodeStack;
	}
	public Deque<IBranchNode> getLoopNodeStack() {
		return loopNodeStack;
	}
	public void setLoopNodeStack(Deque<IBranchNode> loopNodeStack) {
		this.loopNodeStack = loopNodeStack;
	}
	public Deque<BreakNode> getBreakNodeStack() {
		return breakNodeStack;
	}
	public void setBreakNodeStack(Deque<BreakNode> breakNodeStack) {
		this.breakNodeStack = breakNodeStack;
	}
	public INode getLastNode() {
		return lastNode;
	}
	public IStatementNode getLastBreakStatement() {
		return lastBreakStatement;
	}
	public void setLastSelectionNode(SelectionNode lastSelectionNode) {
		this.lastSelectionNode = lastSelectionNode;
	}
	public SelectionNode getLastSelectionNode() {
		return lastSelectionNode;
	}
	public IBranchNode getLastSelectionBranch() {
		if(this.lastSelectionNode != null && lastSelectionNode.node != null) return lastSelectionNode.node;
		else return null;
	}
	public List<INode> getLastSelectionOrphans() {
		if(this.lastSelectionNode != null && lastSelectionNode.orphans != null) return lastSelectionNode.orphans;
		else return null;
	}
	protected void setlastSelectionNode(SelectionNode newSelectionNode) {
		if(newSelectionNode != null && lastSelectionNode != null && lastSelectionNode.orphans.size() > 0)
			newSelectionNode.orphans.addAll(lastSelectionNode.orphans);
		this.lastSelectionNode = newSelectionNode;
	}
	void setLastLoopNode(IBranchNode lastLoopNode) {
		this.lastLoopNode = lastLoopNode;
	}
	public void setLastBreakStatement(IStatementNode lastBreakStatement) {
		this.lastBreakStatement = lastBreakStatement;
	}
	private void setLastNode(INode lastNode) {
		this.lastNode = lastNode;
	}
	protected void adoptOrphans(SelectionNode selection, INode parent) {
		if(getCurrentBranchType() != BRANCH_TYPE_STATE.ALTERNATIVE)
			selection.orphans.forEach(node -> node.setNext(parent));
		selection.orphans.clear();
	}
}
