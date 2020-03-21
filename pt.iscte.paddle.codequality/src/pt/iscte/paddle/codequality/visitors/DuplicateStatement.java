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
import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.codequality.misc.BadCodeAnalyser;
import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.codequality.misc.Explanations;

public class DuplicateStatement implements BadCodeAnalyser, IVisitor{

	private IControlFlowGraph cfg;

	private ArrayList<DuplicateOccurrence> duplicateOccurences = new ArrayList<DuplicateOccurrence>();
	private ArrayList<IStatement> assignments = new ArrayList<IStatement>();

	private class DuplicateOccurrence {
		private IStatement assignment;
		private Duplicate badcase;

		public DuplicateOccurrence(IStatement assignment) {
			this.assignment = assignment;
			this.badcase = new Duplicate(Explanations.DUPLICATE_STATEMENT, assignment);
		}

		public IStatement getAssignment() {
			return assignment;
		}
		public void addOccurrence(IProgramElement assingment) {
			this.badcase.addAssignment(assignment);
		}

	}

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
					if(n != null && incoming != null && !n.equals(incoming) && n.getElement().isSame(incoming.getElement())) 
						duplicates.add(n);
			if(duplicates.size() > 1) Linter.getInstance().register(new Duplicate(Category.DUPLICATE_CODE, Explanations.DUPLICATE_BRANCH_CODE, duplicates));
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

		return true;
	}

}
