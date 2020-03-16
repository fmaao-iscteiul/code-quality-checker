package pt.iscte.paddle.codequality.visitors;

import pt.iscte.paddle.model.cfg.IBranchNode;
import pt.iscte.paddle.model.cfg.INode;

import java.util.ArrayList;
import java.util.List;

import pt.iscte.paddle.codequality.Icfg.IControlFlowGraphBuilder;
import pt.iscte.paddle.codequality.cases.Duplicate;
import pt.iscte.paddle.codequality.linter.Linter;
import pt.iscte.paddle.model.IStatement;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.codequality.misc.BadCodeAnalyser;
import pt.iscte.paddle.codequality.misc.Explanations;

public class DuplicateStatement implements BadCodeAnalyser, IVisitor{

	private IControlFlowGraphBuilder cfgBuilder;

	private ArrayList<DuplicateOccurrence> duplicateOccurences = new ArrayList<DuplicateOccurrence>();
	private ArrayList<IStatement> assignments = new ArrayList<IStatement>();

	private class DuplicateOccurrence {
		private IStatement assignment;
		private Duplicate badcase;

		public DuplicateOccurrence(IStatement assignment) {
			this.assignment = assignment;
			this.badcase = new Duplicate("Insert description here!", assignment);
		}

		public IStatement getAssignment() {
			return assignment;
		}
		public void addOccurrence(IProgramElement assingment) {
			this.badcase.addAssignment(assignment);
		}

	}

	private DuplicateStatement(IControlFlowGraphBuilder cfgBuilder) {
		this.cfgBuilder = cfgBuilder;
	}

	public static DuplicateStatement build(IControlFlowGraphBuilder cfgBuilder) {
		return new DuplicateStatement(cfgBuilder);
	}

	@Override
	public void analyse() {
		for(INode node : this.cfgBuilder.getCFG().getNodes()) {
			List<INode> stack = new ArrayList<INode>();
			List<INode> duplicates = new ArrayList<INode>();
			System.out.println(node.getIncomming());
			for (INode incoming : node.getIncomming()) {
				if((incoming instanceof IBranchNode) || node.isExit() || node.getIncomming().size() == 1) continue;
				
				System.out.println(incoming);
				boolean contains = false;
				for(INode n: stack) {
					if(n.isEquivalentTo(incoming)) {
						if(duplicates.size() == 0) duplicates.add(n);
						duplicates.add(incoming);
						contains = true;
					}
				}
				if(!contains) stack.add(incoming);
			}
			if(duplicates.size() > 1) Linter.getInstance().register(new Duplicate(Explanations.DUPLICATE_BRANCH_CODE, duplicates));
		};
	}

	@Override
	public boolean visit(IVariableAssignment assignment) {
		boolean assExists = false;

		for(IStatement ass: assignments) {
			if(ass.isSame(assignment) && assignment.getParent().isSame(ass.getParent())) {
				assExists = true;
				boolean caseExists = true;
				for (DuplicateOccurrence dOcc : duplicateOccurences) 
					if(dOcc.assignment.isSame(assignment)) dOcc.badcase.addAssignment(assignment);
					else caseExists = false;
				if(!caseExists || duplicateOccurences.size() == 0) {
					DuplicateOccurrence occurence = new DuplicateOccurrence(assignment);
					duplicateOccurences.add(occurence);
					Linter.getInstance().register(occurence.badcase);
				}
			}
		}
		if(!assExists || assignments.size() == 0) assignments.add(assignment);

		return IVisitor.super.visit(assignment);
	}

}
