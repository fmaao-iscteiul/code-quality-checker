package pt.iscte.paddle.codequality.cfg;
import pt.iscte.paddle.model.ILoop;
import pt.iscte.paddle.model.IReturn;
import pt.iscte.paddle.model.ISelection;
import pt.iscte.paddle.model.IVariableAssignment;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import pt.iscte.paddle.model.IBlock.IVisitor;
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
		//		INode lastNode = this.CFG.getNodes().getLast();
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
		//		INode lastNode = this.CFG.getNodes().getLast();
		IBranchNode if_branch = this.CFG.newBranch(selection.getGuard());
		/* Checks if the previous node is a statement or a branch, and acts accordingly. */
		if(lastNode instanceof IBranchNode) ((IBranchNode) lastNode).setBranch(if_branch);
		else lastNode.setNext(if_branch);
		this.node_stack.push(if_branch);
		lastNode = if_branch;
		return true;
	}

	@Override
	public void endVisit(ISelection selection) {
		//		INode lastQueueNode = this.CFG.getNodes().getLast();
		lastBranchNode = this.node_stack.pop();
//		lastNode.setNext(lastStackNode);
//		if(selection.hasAlternativeBlock()) visit(selection.getAlternativeBlock());
	}

	@Override
	public boolean visit(ILoop loop) {
		//		INode lastNode = this.CFG.getNodes().getLast();
		IBranchNode new_loop_branch = this.CFG.newBranch(loop.getGuard());
		/* Checks if the previous node is a statement or a branch, and acts accordingly. */
		if(lastNode instanceof IBranchNode) ((IBranchNode) lastNode).setBranch(new_loop_branch);
		else lastNode.setNext(new_loop_branch);
		this.node_stack.push(new_loop_branch);
		lastNode = new_loop_branch;
		return true;
	}

	@Override
	public void endVisit(ILoop loop) {
		//		INode lastQueueNode = this.CFG.getNodes().getLast();
		INode lastStackNode = this.node_stack.pop();
		lastNode.setNext(lastStackNode);
	}

	@Override
	public boolean visit(IReturn returnStatement) {
		//		INode lastNode = this.CFG.getNodes().getLast().getNext();
		IStatementNode ret = this.CFG.newStatement(returnStatement);
		lastNode.setNext(ret);
		ret.setNext(this.CFG.getExitNode());
		lastNode = ret;
		return true;
	}
}
