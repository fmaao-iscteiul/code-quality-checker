package pt.iscte.paddle.codequality.visitors;

import pt.iscte.paddle.model.cfg.IControlFlowGraph;
import pt.iscte.paddle.model.cfg.INode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import pt.iscte.paddle.codequality.cases.Duplicate;
import pt.iscte.paddle.codequality.linter.Linter;
import pt.iscte.paddle.model.IStatement;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.codequality.misc.BadCodeAnalyser;
import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.codequality.misc.Explanations;

public class DuplicateStatement implements BadCodeAnalyser, IVisitor{

	private IControlFlowGraph cfg;

	private ArrayList<DuplicateOccurrence> duplicateOccurences = new ArrayList<DuplicateOccurrence>();
	private ArrayList<IVariableAssignment> assignments = new ArrayList<IVariableAssignment>();

	private DuplicateStatement(IControlFlowGraph cfg) {
		this.cfg = cfg;
	}

	public static DuplicateStatement build(IControlFlowGraph cfgBuilder) {
		return new DuplicateStatement(cfgBuilder);
	}

	@Override
	public void analyse() {
		for(INode node : this.cfg.getNodes()) {

			Set<INode> duplicates = new HashSet<INode>();
			for(INode incoming: node.getIncomming()) 
				for(INode n: node.getIncomming()) 
					if(n != null && incoming != null && !node.isExit() && !n.equals(incoming) && n.getElement().isSame(incoming.getElement())) 
						duplicates.add(n);
			if(duplicates.size() > 1) {
				Linter.getInstance().register(new Duplicate(Category.DUPLICATE_CODE, Explanations.DUPLICATE_BRANCH_CODE, duplicates));
			}
		};
	}

	private class DuplicateOccurrence {
		private IVariableAssignment assignment;
		private Duplicate badcase;

		public DuplicateOccurrence(IVariableAssignment assignment) {
			this.assignment = assignment;
			this.badcase = new Duplicate(Explanations.DUPLICATE_STATEMENT, assignment);
		}

		@Override
		public String toString() {
			return assignment.toString();
		}
	}

	@Override
	public boolean visit(IVariableAssignment assignment) {
		boolean duplicate = false;
		IVariableAssignment a = null;
		for(IVariableAssignment ass: assignments) {
			if(ass.isSame(assignment) && ass.getTarget().equals(assignment.getTarget()) && assignment.getParent().isSame(ass.getParent())) {
				duplicate = true;
				a = ass;
				break;
			}
								
		}
		if(duplicate) {
			boolean alreadyExists = false;
			for (DuplicateOccurrence dOcc : duplicateOccurences) {
				if(dOcc.assignment.getTarget().equals(assignment.getTarget()) && dOcc.assignment.isSame(assignment)) {
					dOcc.badcase.addAssignment(assignment);
					alreadyExists = true;
				}
			}
			if(!alreadyExists) {
				DuplicateOccurrence occurence = new DuplicateOccurrence(assignment);
				occurence.badcase.addAssignment(a);
				duplicateOccurences.add(occurence);
				Linter.getInstance().register(occurence.badcase);
			}
		}
		assignments.add(assignment);
		return true;
	}

}
