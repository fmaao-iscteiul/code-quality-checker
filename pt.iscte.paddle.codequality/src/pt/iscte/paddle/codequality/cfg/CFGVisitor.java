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
	private Deque<IBranchNode> node_stack;

	public CFGVisitor(IControlFlowGraph CFG) {
		this.CFG = CFG;
		this.node_stack = new ArrayDeque<>();
	}
	
	@Override
	public boolean visit(IVariableAssignment assignment) {
		IStatementNode statement = this.CFG.newStatement(assignment);
		if(lastNode == null)
			this.CFG.getEntryNode().setNext(statement);
		else if(lastNode instanceof IBranchNode) 
			((IBranchNode) lastNode).setBranch(statement);
		else 
			lastNode.setNext(statement);

		if(lastBranchNode != null) {
			lastBranchNode.setNext(statement);
			lastBranchNode = null;
		}

		lastNode = statement;
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
		lastNode = if_branch;
		lastBranchNode = if_branch;
		return true;
	}

	@Override
	public void endVisit(ISelection selection) {
		lastBranchNode = this.node_stack.pop();
		//		if(selection.hasAlternativeBlock()) visit(selection.getAlternativeBlock());
	}

	@Override
	public boolean visit(ILoop loop) {
		IBranchNode new_loop_branch = this.CFG.newBranch(loop.getGuard());
		/* Checks if the previous node is a statement or a branch, and acts accordingly. */
		if(lastNode instanceof IBranchNode) ((IBranchNode) lastNode).setBranch(new_loop_branch);
		else lastNode.setNext(new_loop_branch);
		this.node_stack.push(new_loop_branch);
		lastBranchNode = new_loop_branch;
		lastNode = new_loop_branch;
		return true;
	}

	@Override
	public void endVisit(ILoop loop) {
		INode lastStackNode = this.node_stack.pop();
		lastNode.setNext(lastStackNode);
	}

	@Override
	public boolean visit(IReturn returnStatement) {
		IStatementNode ret = this.CFG.newStatement(returnStatement);
		INode lastLiteralNode = lastNode.getNext();
		if(lastLiteralNode != null) lastNode.getNext().setNext(ret);
		else lastNode.setNext(ret);
		ret.setNext(this.CFG.getExitNode());
		lastNode = ret;
		return true;
	}

	@Override
	public void endVisit(IBlock block) {
		System.out.println("ola");
		IVisitor.super.endVisit(block);
	}
}
