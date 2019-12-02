package pt.iscte.paddle.codequality.cfg;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import pt.iscte.paddle.model.IBlock;
import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.model.IBreak;
import pt.iscte.paddle.model.IConditionalExpression;
import pt.iscte.paddle.model.IContinue;
import pt.iscte.paddle.model.ILoop;
import pt.iscte.paddle.model.IReturn;
import pt.iscte.paddle.model.ISelection;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.cfg.IBranchNode;
import pt.iscte.paddle.model.cfg.IControlFlowGraph;
import pt.iscte.paddle.model.cfg.INode;
import pt.iscte.paddle.model.cfg.IStatementNode;

public class CFGVisitor implements IVisitor {

	private IControlFlowGraph CFG;
	//	private boolean isFirst = true;
	private INode lastNode = null; 
	
	private IBranchNode lastBranchNode = null;
	private IBranchNode lastLoopNode = null;
	
	private Deque<IBranchNode> selection_stack;
	private Deque<IBranchNode> loop_stack;
	

	public CFGVisitor(IControlFlowGraph CFG) {
		this.CFG = CFG;
		this.selection_stack = new ArrayDeque<>();
		this.loop_stack = new ArrayDeque<>();
	}
	
	@Override
	public boolean visit(IVariableAssignment assignment) {
		IStatementNode assign_statement = this.CFG.newStatement(assignment);
		if(lastNode == null)
			this.CFG.getEntryNode().setNext(assign_statement);
		else if(lastNode instanceof IBranchNode && ((IBranchNode) lastNode).getAlternative() == null) 
			((IBranchNode) lastNode).setBranch(assign_statement);
		else if(lastNode.getNext() == null)
			lastNode.setNext(assign_statement);
		// Handling the branch false case.
		
		if(lastBranchNode != null) {
			lastBranchNode.setNext(assign_statement);
			setLastBranchNode(null);
		}

		setLastNode(assign_statement);
		return true;
	}

	@Override
	public boolean visit(ISelection selection) {
		if(selection.hasAlternativeBlock()) visit(selection.getAlternativeBlock());
		IBranchNode if_branch = this.CFG.newBranch(selection.getGuard());
		/* Checks if the previous node is a statement or a branch, and acts accordingly. */
		if(lastNode instanceof IBranchNode) ((IBranchNode) lastNode).setBranch(if_branch);
		else lastNode.setNext(if_branch);
		/* Pushes the new node into the INode Collection. */
		this.selection_stack.push(if_branch);
		setLastNode(if_branch);
//		setLastBranchNode(if_branch);
		return true;
	}
	
	@Override
	public void endVisit(ISelection selection) {
		setLastBranchNode(this.selection_stack.pop());
		lastBranchNode.setNext(lastNode);
	}


	@Override
	public boolean visit(ILoop loop) {
		IBranchNode new_loop_branch = this.CFG.newBranch(loop.getGuard());
		/* Checks if the previous node is a statement or a branch, and acts accordingly. */
		if(lastNode instanceof IBranchNode) ((IBranchNode) lastNode).setBranch(new_loop_branch);
		else lastNode.setNext(new_loop_branch);
		this.loop_stack.push(new_loop_branch);
//		setLastBranchNode(new_loop_branch);
		setLastNode(new_loop_branch);
		return true;
	}

	@Override
	public void endVisit(ILoop loop) {
		IBranchNode lastLoopNode = this.loop_stack.pop();
		lastNode.setNext(lastLoopNode);
		setLastLoopNode(lastLoopNode);
	}
	
	@Override
	public void visit(IContinue continueStatement) {
		IStatementNode continue_statement = CFG.newStatement(continueStatement);
		if(lastLoopNode.getNext() == null) lastLoopNode.setNext(continue_statement);
		else if(lastNode instanceof IBranchNode && ((IBranchNode) lastNode).getAlternative() == null) ((IBranchNode) lastNode).setBranch(continue_statement);
		else if(lastNode.getNext() == null) lastNode.setNext(continue_statement);
		continue_statement.setNext(this.loop_stack.peek());
		setLastNode(continue_statement);
	}

	@Override
	public boolean visit(IReturn returnStatement) {
		IStatementNode ret = this.CFG.newStatement(returnStatement);
		INode lastLiteralNode = lastNode.getNext();
		if(lastLiteralNode != null) lastNode.getNext().setNext(ret);
		else lastNode.setNext(ret);
		ret.setNext(this.CFG.getExitNode());
		setLastNode(ret);
		return true;
	}
	
	@Override
	public void visit(IBreak breakStatement) {
	}
	
	private void setLastBranchNode(IBranchNode lastBranchNode) {
		this.lastBranchNode = lastBranchNode;
	}
	
	private void setLastNode(INode lastNode) {
		this.lastNode = lastNode;
	}
	
	public void setLastLoopNode(IBranchNode lastLoopNode) {
		this.lastLoopNode = lastLoopNode;
	}
}
