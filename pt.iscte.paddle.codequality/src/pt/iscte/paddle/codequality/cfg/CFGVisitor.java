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
	
	private INode lastNode = null;
	private SelectionNode lastSelectionNode = null;
	private IBranchNode lastLoopNode = null;
	private IStatementNode lastBreakStatement = null;
	private BRANCH_TYPE_STATE currentBranchType = null;

	private Deque<SelectionNode> selectionNodeStack;
	private Deque<IBranchNode> loopNodeStack;
	private Deque<BreakNode> breakNodeStack;

	private static class SelectionNode {
		final IBranchNode node;
		
		final List<INode> orphans;

		SelectionNode(IBranchNode if_branch) {
			node = if_branch;
			orphans = new ArrayList<INode>();
		}
	}
	
	private static class BreakNode {
		final IStatementNode node;
		final List<INode> orphans;

		BreakNode(IStatementNode breakStatement) {
			node = breakStatement;
			orphans = new ArrayList<INode>();
		}
	}
	
	

	public CFGVisitor(IControlFlowGraph CFG) {
		this.CFG = CFG;
		this.selectionNodeStack = new ArrayDeque<>();
		this.loopNodeStack = new ArrayDeque<>();
		this.breakNodeStack = new ArrayDeque<>();
	}

	@Override
	public boolean visit(IVariableAssignment assignment) {
		System.out.println("------------------- assignment: "+ assignment +" --------------------");
		System.out.println("last selection: " + getLastSelectionNode());
		System.out.println("last loop: " + lastLoopNode);
		System.out.println("last node: " + lastNode);
		IStatementNode assignment_statement = this.CFG.newStatement(assignment);

		this.handleStatementVisit(assignment_statement);
		setLastNode(assignment_statement);
		return true;
	}

	@Override
	public boolean visit(ISelection selection) {
		currentBranchType = BRANCH_TYPE_STATE.SELECTION;
		System.out.println("------------------- started selection: "+ selection.getGuard() +" --------------------");
		System.out.println("last selection: " + getLastSelectionNode());
		System.out.println("last loop: " + lastLoopNode);
		System.out.println("last node: " + lastNode);
		IBranchNode if_branch = this.CFG.newBranch(selection.getGuard());

		/* Checks if the previous node is a statement or a branch, and acts accordingly. */
		this.handleSelectionBranchVisit(if_branch);		
		/* Pushes the new node into the INode Collection. */
		SelectionNode new_selection = new SelectionNode(if_branch);
		this.selectionNodeStack.push(new_selection);
		this.setLastNode(if_branch);
		return true;
	}

	// Se houver loop acima, definir o next do last node para esse loop, sem por a o lastLoopNode a null
	@Override
	public void endVisit(ISelection selection) {
		currentBranchType = null;
		System.out.println("------------------- finished selection: "+ selection.getGuard() +" --------------------");
		System.out.println("last selection: " + getLastSelectionNode());
		System.out.println("orphans state: " + selectionNodeStack.size());
		System.out.println("last loop: " + lastLoopNode);
		System.out.println("last node: " + lastNode);
		if(getLastSelectionNode() != null && getLastSelectionNode().getNext() == null) 
			this.selectionNodeStack.peek().orphans.add(getLastSelectionNode());
		this.setlastSelectionNode(this.selectionNodeStack.pop());
	}

	@Override
	public void endVisitBranch(ISelection selection) {
		if(selectionNodeStack.size() > 0) currentBranchType = BRANCH_TYPE_STATE.SELECTION;
		else currentBranchType = null;
		System.out.println("------------------- END VISIT BRANCH selection: "+ selection.getGuard() +" --------------------");
		System.out.println("last selection: " + getLastSelectionNode());
		System.out.println("last loop: " + lastLoopNode);
		System.out.println("last node: " + lastNode);
		if(lastNode.getNext() == null && lastSelectionNode == null && selectionNodeStack.size() > 0 && !selectionNodeStack.peek().orphans.contains(lastNode)) {
			System.out.println("pushed into orphan list: "+lastNode);
			selectionNodeStack.peek().orphans.add(lastNode);
		}
		/* Because of else's after selection, that need to be set as next.*/
		this.setlastSelectionNode(selectionNodeStack.peek());
	}

	@Override
	public boolean visitAlternative(ISelection selection) {
		currentBranchType = BRANCH_TYPE_STATE.ALTERNATIVE;
		System.out.println("------------------- ELSE VISIT: "+ selection.getGuard() +" --------------------");
		System.out.println("last selection: " + getLastSelectionNode());
		System.out.println("last loop: " + lastLoopNode);
		System.out.println("last node: " + lastNode);
		if(lastNode.getNext() == null && selectionNodeStack.size() > 0 && !selectionNodeStack.peek().orphans.contains(lastNode)) {
			selectionNodeStack.peek().orphans.add(lastNode);
			System.out.println("pushed into orphan list: "+lastNode);
		}
		return IVisitor.super.visitAlternative(selection);
	}

	@Override
	public void endVisitAlternative(ISelection selection) {
		if(selectionNodeStack.size() > 0) currentBranchType = BRANCH_TYPE_STATE.SELECTION;
		else currentBranchType = null;

		System.out.println("------------------- END VISIT ALTERNATIVE selection: "+ selection.getGuard() +" --------------------");
		System.out.println("last selection: " + getLastSelectionNode());
		System.out.println("last loop: " + lastLoopNode);
		System.out.println("last node: " + lastNode);
		if(lastNode.getNext() == null && lastSelectionNode == null && 
				!selection.getAlternativeBlock().isEmpty() &&
					selectionNodeStack.size() > 0 && 
						!selectionNodeStack.peek().orphans.contains(lastNode)) {
			System.out.println("pushed into orphan list: "+lastNode + "current size: " + selectionNodeStack.peek().orphans.size());
			selectionNodeStack.peek().orphans.add(lastNode);
		}	
	}

	@Override
	public boolean visit(ILoop loop) {
		System.out.println("------------------- loop: "+ loop.getGuard() +" --------------------");
		System.out.println("started loop: " + loop.getGuard());
		System.out.println("last selection: " + getLastSelectionNode());
		System.out.println("last node: " + lastNode);
		IBranchNode loop_branch = this.CFG.newBranch(loop.getGuard());
		/* Checks if the previous node is a statement or a branch, and acts accordingly. */

		this.loopNodeStack.push(loop_branch);	
		this.handleLoopBranchVisit(loop_branch);
//		setLastLoopNode(loop_branch);
		this.setLastNode(loop_branch);
		return true;
	}

	@Override
	public void endVisit(ILoop loop) {
		System.out.println("------------------- finished loop: "+ loop.getGuard() +" --------------------");
		System.out.println("finished loop: " + loop.getGuard());
		System.out.println("last selection: " + getLastSelectionNode());
		System.out.println("last node: " + lastNode);
		
		if(this.loopNodeStack.size() > 0) {
			IBranchNode finishedLoopBranch = this.loopNodeStack.pop();
			
			if(lastBreakStatement != null) {
				lastBreakStatement.setNext(finishedLoopBranch);
				setLastBreakStatement(null);
			}
			
			if(lastSelectionNode != null /* && selectionNodeStack.size() > 0 */) {
				adoptOrphans(lastSelectionNode, finishedLoopBranch);
			}
			
			if(lastLoopNode != null && lastLoopNode.getNext() == null){
				lastLoopNode.setNext(finishedLoopBranch);
			}
			if(lastNode != null && lastNode.getNext() == null) lastNode.setNext(finishedLoopBranch);
			/* So that the last Node can point to the beginning of the next loop. */
			setLastLoopNode(finishedLoopBranch);
		}
		if(breakNodeStack.size() > 0) {
			BreakNode lastBrake = this.breakNodeStack.pop();
			setLastBreakStatement(lastBrake.node);
		}
	}

	@Override
	public boolean visit(IReturn returnStatement) {
		System.out.println("------------------- return statement: "+ returnStatement +" --------------------");
		System.out.println("last selection: " + getLastSelectionNode());
		System.out.println("last loop: " + lastLoopNode);
		System.out.println("last node: " + lastNode);
		IStatementNode ret = this.CFG.newStatement(returnStatement);
		if(lastNode instanceof IBranchNode && !((IBranchNode) lastNode).hasBranch()) ((IBranchNode) lastNode).setBranch(ret);
		else if(lastNode != null && lastNode.getNext() == null) lastNode.setNext(ret);
		else lastNode.getNext().setNext(ret);
		ret.setNext(this.CFG.getExitNode());
		setLastNode(ret);
		return true;
	}
	
	@Override
	public void endVisit(IBlock block) {
		System.out.println("------------------- VISITOR JUST ENDED --------------------");
		IVisitor.super.endVisit(block);
	}

	@Override
	public void visit(IContinue continueStatement) {
		IStatementNode continue_statement = CFG.newStatement(continueStatement);
		if(lastNode instanceof IBranchNode) ((IBranchNode) lastNode).setBranch(continue_statement);
		else lastNode.setNext(continue_statement);
		if(loopNodeStack.peek() != null) continue_statement.setNext(loopNodeStack.peek());
		setLastNode(continue_statement);
	}

	@Override
	public void visit(IBreak breakStatement) {
		IStatementNode break_statement = CFG.newStatement(breakStatement);
		if(lastNode instanceof IBranchNode) ((IBranchNode) lastNode).setBranch(break_statement);
		else lastNode.setNext(break_statement);
		this.breakNodeStack.add(new BreakNode(break_statement));
	}

	/**
	 * @function HandleStatementVisit
	 * @description Checks for the type of the previous node, and acts accordingly. This meaning that it will place itself has the next Statement node for the previous one.
	 * @param INode The statement that is has been visited and will be inserted in the Control Flow Graph.
	 */
	private void handleStatementVisit(INode statement) {

		if(currentBranchType != BRANCH_TYPE_STATE.ALTERNATIVE && lastSelectionNode != null && lastSelectionNode.orphans.size() > 0)
			adoptOrphans(lastSelectionNode, statement);
		
		if(lastBreakStatement != null) {
			lastBreakStatement.setNext(statement);
			setLastBreakStatement(null);
		}
		
		if(getLastSelectionNode() != null && getLastSelectionNode().hasBranch() && getLastSelectionNode().getNext() == null) {
			getLastSelectionNode().setNext(statement);
			setlastSelectionNode(null);
		}
		if(lastLoopNode != null && lastLoopNode.hasBranch() && lastNode.getNext() != null) {
			lastLoopNode.setNext(statement);
			setLastLoopNode(null);
		}
		if(lastNode == null) 
			this.CFG.getEntryNode().setNext(statement);
		else if(lastNode instanceof IBranchNode && !((IBranchNode) lastNode).hasBranch()) 
			((IBranchNode) lastNode).setBranch(statement);
		/* The middle condition is duo to the possibility of having an assignment inside an else, that can't be set as the lastNode's next.*/
		else if(lastNode != null && !(lastNode instanceof IBranchNode) && (selectionNodeStack.size() == 0 || !selectionNodeStack.peek().orphans.contains(lastNode)) && lastNode.getNext() == null)
			lastNode.setNext(statement);
		

		/* In case that this is the statement right after a loop visitor has ended. */ 

	}

	private void handleSelectionBranchVisit(IBranchNode selection) {

		if(lastLoopNode != null && lastLoopNode.hasBranch() && lastNode.getNext() != null) {
			lastLoopNode.setNext(selection);
			setLastLoopNode(null);
		}
		
		if(lastBreakStatement != null) {
			lastBreakStatement.setNext(selection);
			setLastBreakStatement(null);
		}

		if(getLastSelectionNode() != null && getLastSelectionNode().hasBranch() && getLastSelectionNode().getNext() == null) {
			getLastSelectionNode().setNext(selection);
			setlastSelectionNode(null);
		}

		if(lastSelectionNode != null && lastSelectionNode.orphans.size() > 0)
			adoptOrphans(lastSelectionNode, selection);

		if(lastNode instanceof IBranchNode && !((IBranchNode) lastNode).hasBranch()) ((IBranchNode) lastNode).setBranch(selection);
		else if(lastNode != null && lastNode.getNext() == null) lastNode.setNext(selection);
		else if(lastNode == null) this.CFG.getEntryNode().setNext(selection);
	}

	/**
	 * @function handleBranchVisit
	 * @description Checks for the type of the previous node, and acts accordingly. This meaning that it will place itself has the next Branch node for the previous one.
	 * @param IBranchNode branch The statement that is has been visited and will be inserted in the Control Flow Graph.
	 */
	private void handleLoopBranchVisit(IBranchNode loop) {

		if(getLastSelectionNode() != null && getLastSelectionNode().hasBranch() && getLastSelectionNode().getNext() == null) {
			getLastSelectionNode().setNext(loop);
			setlastSelectionNode(null);
		}
		if(lastLoopNode != null && lastLoopNode.hasBranch() && lastNode.getNext() != null) {
			lastLoopNode.setNext(loop);
			setLastLoopNode(null);
		}

		if(lastSelectionNode != null && lastSelectionNode.orphans.size() > 0)
			adoptOrphans(lastSelectionNode, loop);

		if(lastNode instanceof IBranchNode && !((IBranchNode) lastNode).hasBranch()) ((IBranchNode) lastNode).setBranch(loop);
		else if(lastNode != null && lastNode.getNext() == null) lastNode.setNext(loop);
		else if( lastNode == null ) this.CFG.getEntryNode().setNext(loop);
	}
	
	public IBranchNode getLastSelectionNode() {
		if(this.lastSelectionNode != null && lastSelectionNode.node != null) return lastSelectionNode.node;
		else return null;
	}
	private void setlastSelectionNode(SelectionNode newSelectionNode) {
		if(newSelectionNode != null && lastSelectionNode != null && lastSelectionNode.orphans.size() > 0)
			newSelectionNode.orphans.addAll(lastSelectionNode.orphans);
		this.lastSelectionNode = newSelectionNode;
	}
	private void setLastLoopNode(IBranchNode lastLoopNode) {
		this.lastLoopNode = lastLoopNode;
	}
	public void setLastBreakStatement(IStatementNode lastBreakStatement) {
		this.lastBreakStatement = lastBreakStatement;
	}
	private void setLastNode(INode lastNode) {
		this.lastNode = lastNode;
	}
	private void adoptOrphans(SelectionNode selection, INode parent) {
		selection.orphans.forEach(node -> {
			System.out.println("ORFÃO: " + node);
			node.setNext(parent);
		});
		selection.orphans.clear();
	}
}
