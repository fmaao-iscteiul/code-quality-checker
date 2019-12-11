package pt.iscte.paddle.codequality.cfg;
import pt.iscte.paddle.model.ILoop;
import pt.iscte.paddle.model.IReturn;
import pt.iscte.paddle.model.ISelection;
import pt.iscte.paddle.model.IVariableAssignment;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.model.IBlock;
import pt.iscte.paddle.model.IBreak;
import pt.iscte.paddle.model.IConditionalExpression;
import pt.iscte.paddle.model.IContinue;
import pt.iscte.paddle.model.cfg.IBranchNode;
import pt.iscte.paddle.model.cfg.IControlFlowGraph;
import pt.iscte.paddle.model.cfg.INode;
import pt.iscte.paddle.model.cfg.IStatementNode;

public class CFGVisitor implements IVisitor {

	private IControlFlowGraph CFG;
	//	private boolean isFirst = true;
	private INode lastNode = null;
	private IBranchNode lastSelectionNode = null;
	private IBranchNode lastLoopNode = null;

	private Deque<IBranchNode> selectionNodeStack;
	private Deque<IBranchNode> loopNodeStack;
	
	private List<INode> selectionOrphans;
	// tentar com pilha dos ultimos nos.

	public CFGVisitor(IControlFlowGraph CFG) {
		this.CFG = CFG;

		this.selectionNodeStack = new ArrayDeque<>();
		this.loopNodeStack = new ArrayDeque<>();
		this.selectionOrphans = new ArrayList<INode>();
	}

	@Override
	public boolean visit(IVariableAssignment assignment) {
		IStatementNode assignment_statement = this.CFG.newStatement(assignment);
		System.out.println("------------------- assignment: "+ assignment +" --------------------");
		System.out.println("last selection: " + lastSelectionNode);
		System.out.println("last loop: " + lastLoopNode);
		System.out.println("last node: " + lastNode);

		this.handleStatementVisit(assignment_statement);

		setLastNode(assignment_statement);
		return true;
	}

	@Override
	public boolean visit(ISelection selection) {
		System.out.println("------------------- started selection: "+ selection.getGuard() +" --------------------");
		System.out.println("last selection: " + lastSelectionNode);
		System.out.println("last loop: " + lastLoopNode);
		System.out.println("last node: " + lastNode);
		IBranchNode if_branch = this.CFG.newBranch(selection.getGuard());
		
		/* Checks if the previous node is a statement or a branch, and acts accordingly. */
		this.handleSelectionBranchVisit(if_branch);
		
		this.setlastSelectionNode(if_branch);
		this.setLastNode(if_branch);
	
		/* Pushes the new node into the INode Collection. */
		this.selectionNodeStack.push(if_branch);
				
		return true;
	}

	// Se houver loop acima, definir o next do last node para esse loop, sem por a o lastLoopNode a null
	@Override
	public void endVisit(ISelection selection) {
		System.out.println("------------------- finished selection: "+ selection.getGuard() +" --------------------");
		System.out.println("last selection: " + lastSelectionNode);
		System.out.println("last loop: " + lastLoopNode);
		System.out.println("last node: " + lastNode);
		if(this.selectionNodeStack.size() > 0) {
			IBranchNode selectionBranch = this.selectionNodeStack.pop();
			
			this.setlastSelectionNode(selectionBranch);
//			if(lastNode != null && lastNode.getNext() == null) selectionBranch.setNext(lastNode);
			
			System.out.println("last connection: " + lastNode);
		}
		if(selection.hasAlternativeBlock()) visit(selection.getAlternativeBlock());
	}


	@Override
	public boolean visit(ILoop loop) {
		System.out.println("------------------- loop: "+ loop.getGuard() +" --------------------");
		System.out.println("started loop: " + loop.getGuard());
		System.out.println("last selection: " + lastSelectionNode);
		System.out.println("last node: " + lastNode);
		IBranchNode loop_branch = this.CFG.newBranch(loop.getGuard());
		/* Checks if the previous node is a statement or a branch, and acts accordingly. */
		
		this.loopNodeStack.push(loop_branch);	
		this.handleLoopBranchVisit(loop_branch);
		setLastLoopNode(loop_branch);
		this.setLastNode(loop_branch);
		return true;
	}

	@Override
	public void endVisit(ILoop loop) {
		System.out.println("------------------- finished loop: "+ loop.getGuard() +" --------------------");
		System.out.println("finished loop: " + loop.getGuard());
		System.out.println("last selection: " + lastSelectionNode);
		System.out.println("last node: " + lastNode);
		if(this.loopNodeStack.size() > 0) {
			IBranchNode finishedLoopBranch = this.loopNodeStack.pop();
			/* So that the last Node can point to the beginning of the next loop. */
			setLastLoopNode(finishedLoopBranch);
			
			if(lastNode != null && lastNode.getNext() == null) lastNode.setNext(lastLoopNode);
		}
	}

	@Override
	public boolean visit(IReturn returnStatement) {
		System.out.println("------------------- return statement: "+ returnStatement +" --------------------");
		System.out.println("last selection: " + lastSelectionNode);
		System.out.println("last loop: " + lastLoopNode);
		System.out.println("last node: " + lastNode);
		System.out.println("------------------- VISITOR JUST ENDED --------------------");
		IStatementNode ret = this.CFG.newStatement(returnStatement);
		if(lastNode instanceof IBranchNode && !((IBranchNode) lastNode).hasBranch()) ((IBranchNode) lastNode).setBranch(ret);
		else if(lastNode != null && lastNode.getNext() == null) lastNode.setNext(ret);
		else lastNode.getNext().setNext(ret);
		ret.setNext(this.CFG.getExitNode());
		setLastNode(ret);
		return true;
	}

	@Override
	public void visit(IContinue continueStatement) {
		IStatementNode continue_statement = CFG.newStatement(continueStatement);
		if(lastNode instanceof IBranchNode) ((IBranchNode) lastNode).setBranch(continue_statement);
		else lastNode.setNext(continue_statement);
		if(lastLoopNode != null) continue_statement.setNext(lastLoopNode);
		setLastNode(continue_statement);
	}

	@Override
	public void visit(IBreak breakStatement) {
	}

	@Override
	public boolean visit(IBlock block) {
		System.out.println("---------------------BLOCO DO ELSE---------------------");
		System.out.println("lastSelectionNode: " +lastSelectionNode);
		System.out.println("lastNode: " + lastNode);
		
		if(lastSelectionNode != null) {
			lastSelectionNode.setNext(lastNode);
			setlastSelectionNode(null);
		}
		return IVisitor.super.visit(block);
	}
	@Override
	public void endVisit(IBlock block) {
		IVisitor.super.endVisit(block);
	}

	/**
	 * @function HandleStatementVisit
	 * @description Checks for the type of the previous node, and acts accordingly. This meaning that it will place itself has the next Statement node for the previous one.
	 * @param INode The statement that is has been visited and will be inserted in the Control Flow Graph.
	 */
	private void handleStatementVisit(INode statement) {
		
		if(lastSelectionNode != null && lastSelectionNode.hasBranch()) {
			lastSelectionNode.setNext(statement);
			setlastSelectionNode(null);
		}
		if(lastLoopNode != null && lastLoopNode.hasBranch() && lastNode.getNext() != null) {
			lastLoopNode.setNext(statement);
			setLastLoopNode(null);
		}		
		
		if(lastNode == null) this.CFG.getEntryNode().setNext(statement);
		else if(lastNode instanceof IBranchNode && ((IBranchNode) lastNode).getAlternative() == null) ((IBranchNode) lastNode).setBranch(statement);
		else if(lastNode.getNext() == null) lastNode.setNext(statement);
		/* In case that this is the statement right after a loop visitor has ended. */ 
		
	}
	
	private void handleSelectionBranchVisit(IBranchNode selection) {
		
//		if(lastSelectionNode != null && lastSelectionNode.getNext() == null) lastSelectionNode.setNext(selection);
//		if(lastLoopNode != null && lastLoopNode.hasBranch() && lastNode.getNext() != null) {
//			lastLoopNode.setNext(selection);
////			setLastLoopNode(null);
//		}	
		if(lastSelectionNode != null && lastNode.getNext() != null) lastSelectionNode.setNext(selection);
		else if(lastLoopNode != null && lastLoopNode.hasBranch() && lastNode.getNext() != null) {
			lastLoopNode.setNext(selection);
//			setLastLoopNode(null);
		}
		if(lastNode instanceof IBranchNode && !((IBranchNode) lastNode).hasBranch()) ((IBranchNode) lastNode).setBranch(selection);
		else if(lastNode != null && lastNode.getNext() == null) lastNode.setNext(selection);
		else if(lastNode == null) this.CFG.getEntryNode().setNext(selection);
		
//		if(lastLoopNode != null && lastLoopNode.getNext() == null) lastNode.setNext(lastLoopNode);
//		if(lastSelectionNode != null) lastSelectionNode.setNext(selection);
	}

	/**
	 * @function handleBranchVisit
	 * @description Checks for the type of the previous node, and acts accordingly. This meaning that it will place itself has the next Branch node for the previous one.
	 * @param IBranchNode branch The statement that is has been visited and will be inserted in the Control Flow Graph.
	 */
	private void handleLoopBranchVisit(IBranchNode loop) {
//		if(lastSelectionNode != null && lastSelectionNode.getNext() == null) {
//			lastSelectionNode.setNext(branch);
//		}
		if(lastSelectionNode != null && lastNode.getNext() != null) lastSelectionNode.setNext(loop);
		else if(lastLoopNode != null && lastLoopNode.hasBranch() && lastNode.getNext() != null) {
			lastLoopNode.setNext(loop);
//			setLastLoopNode(null);
		}	
		if(lastNode instanceof IBranchNode && !((IBranchNode) lastNode).hasBranch()) ((IBranchNode) lastNode).setBranch(loop);
		else if(lastNode != null && lastNode.getNext() == null) lastNode.setNext(loop);
		else if( lastNode == null ) this.CFG.getEntryNode().setNext(loop);
	}

	private void setlastSelectionNode(IBranchNode lastSelectionNode) {
		this.lastSelectionNode = lastSelectionNode;
	}
	private void setLastLoopNode(IBranchNode lastLoopNode) {
		this.lastLoopNode = lastLoopNode;
	}
	private void setLastNode(INode lastNode) {
		this.lastNode = lastNode;
	}
}
