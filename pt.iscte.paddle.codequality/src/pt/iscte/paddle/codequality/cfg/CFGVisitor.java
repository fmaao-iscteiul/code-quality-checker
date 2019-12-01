package pt.iscte.paddle.codequality.cfg;
import pt.iscte.paddle.model.ILoop;
import pt.iscte.paddle.model.IReturn;
import pt.iscte.paddle.model.ISelection;
import pt.iscte.paddle.model.IVariableAssignment;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.model.IBlock;
import pt.iscte.paddle.model.IBlockElement;
import pt.iscte.paddle.model.IBreak;
import pt.iscte.paddle.model.IContinue;
import pt.iscte.paddle.model.IExpression;
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
	private Deque<IBranchNode> node_stack;

	public CFGVisitor(IControlFlowGraph CFG) {
		this.CFG = CFG;
		this.node_stack = new ArrayDeque<>();
	}
	
	@Override
	public boolean visit(IVariableAssignment assignment) {
		IStatementNode assign_statement = this.CFG.newStatement(assignment);
		if(lastNode == null)
			this.CFG.getEntryNode().setNext(assign_statement);
		else if(lastNode instanceof IBranchNode) 
			((IBranchNode) lastNode).setBranch(assign_statement);
		else if(lastNode.getNext() == null)
			lastNode.setNext(assign_statement);

		if(lastBranchNode != null) {
			lastBranchNode.setNext(assign_statement);
			setLastBranchNode(null);
		}

		setLastNode(assign_statement);
		return true;
	}

	@Override
	public boolean visit(ISelection selection) {
		IBranchNode if_branch = this.CFG.newBranch(selection.getGuard());
		/* Checks if the previous node is a statement or a branch, and acts accordingly. */
		if(lastNode instanceof IBranchNode) ((IBranchNode) lastNode).setBranch(if_branch);
		else lastNode.setNext(if_branch);
		/* Pushes the new node into the INode Collection. */
		this.node_stack.push(if_branch);
		setLastNode(if_branch);
		setLastBranchNode(if_branch);
		return true;
	}

	@Override
	public void endVisit(ISelection selection) {
		setLastBranchNode(this.node_stack.pop());
		//		if(selection.hasAlternativeBlock()) visit(selection.getAlternativeBlock());
	}

	@Override
	public boolean visit(ILoop loop) {
		IBranchNode new_loop_branch = this.CFG.newBranch(loop.getGuard());
		/* Checks if the previous node is a statement or a branch, and acts accordingly. */
		if(lastNode instanceof IBranchNode) ((IBranchNode) lastNode).setBranch(new_loop_branch);
		else lastNode.setNext(new_loop_branch);
		this.node_stack.push(new_loop_branch);
		setLastBranchNode(new_loop_branch);
		setLastNode(new_loop_branch);
		setLastLoopNode(new_loop_branch);
		return true;
	}

	@Override
	public void endVisit(ILoop loop) {
		lastNode.setNext(this.node_stack.pop());
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
	public void visit(IContinue continueStatement) {
		IStatementNode continue_statement = CFG.newStatement(continueStatement);
		if(lastNode instanceof IBranchNode) ((IBranchNode) lastNode).setBranch(continue_statement);
		else lastNode.setNext(continue_statement);
		continue_statement.setNext(lastLoopNode);
		setLastNode(continue_statement);
		setLastLoopNode(null);
	}
	
	@Override
	public void visit(IBreak breakStatement) {
	}

	@Override
	public void endVisit(IBlock block) {
		IVisitor.super.endVisit(block);
	}
	
	private void setLastBranchNode(IBranchNode lastBranchNode) {
		this.lastBranchNode = lastBranchNode;
	}
	private void setLastLoopNode(IBranchNode lastLoopNode) {
		this.lastLoopNode = lastLoopNode;
	}
	private void setLastNode(INode lastNode) {
		this.lastNode = lastNode;
	}
}
