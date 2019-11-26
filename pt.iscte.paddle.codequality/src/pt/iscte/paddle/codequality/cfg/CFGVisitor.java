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
import pt.iscte.paddle.model.cfg.IBranchNode;
import pt.iscte.paddle.model.cfg.IControlFlowGraph;
import pt.iscte.paddle.model.cfg.INode;
import pt.iscte.paddle.model.cfg.IStatementNode;

public class CFGVisitor implements IVisitor{
	
	private IControlFlowGraph CFG;
	private boolean isFirst = true;
	
	Deque<INode> node_stack;

	public CFGVisitor(IControlFlowGraph CFG) {
		this.CFG = CFG;
		this.node_stack = new ArrayDeque<INode>();
	}
	
	@Override
	public boolean visit(IVariableAssignment assignment) {
		INode lastNode = this.CFG.getNodes().getLast();
		IStatementNode statement = this.CFG.newStatement(assignment);
		if(assignment.getParent().getDepth() == 1 && this.isFirst) {
			this.CFG.getEntryNode().setNext(statement);
			this.isFirst = false;
		}
		else lastNode.setNext(statement);
		return IVisitor.super.visit(assignment);
	}
	
	@Override
	public boolean visit(ISelection selection) {
		IBranchNode if_branch = this.CFG.newBranch(selection.getGuard());
		this.node_stack.push(if_branch);
		return IVisitor.super.visit(selection);
	}
	
	@Override
	public void endVisit(ISelection selection) {
		INode lastQueueNode = this.CFG.getNodes().getLast();
		INode lastStackNode = this.node_stack.pop();
		lastQueueNode.setNext(lastStackNode);
		if(selection.hasAlternativeBlock()) visit(selection.getAlternativeBlock());
		IVisitor.super.endVisit(selection);
	}
	
	@Override
	public boolean visit(ILoop loop) {
		IBranchNode loop_branch = this.CFG.newBranch(loop.getGuard());
		this.node_stack.push(loop_branch);
		return IVisitor.super.visit(loop);
	}
	
	@Override
	public void endVisit(ILoop loop) {
		INode lastQueueNode = this.CFG.getNodes().getLast();
		INode lastStackNode = this.node_stack.pop();
		lastQueueNode.setNext(lastStackNode);
		IVisitor.super.endVisit(loop);
	}

	@Override
	public boolean visit(IReturn returnStatement) {
		INode lastNode = this.CFG.getNodes().getLast();
		IStatementNode ret = this.CFG.newStatement(returnStatement);
		lastNode.setNext(ret);
		ret.setNext(this.CFG.getExitNode());
		return IVisitor.super.visit(returnStatement);
	}
}
