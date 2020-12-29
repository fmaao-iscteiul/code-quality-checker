package pt.iscte.paddle.linter.visitors;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import pt.iscte.paddle.linter.cases.base.CodeAnalyser;
import pt.iscte.paddle.linter.cases.base.QualityIssue;
import pt.iscte.paddle.linter.issues.DuplicateGuard;
import pt.iscte.paddle.linter.linter.Linter;
import pt.iscte.paddle.linter.misc.BadCodeAnalyser;
import pt.iscte.paddle.linter.misc.Compability;
import pt.iscte.paddle.model.IBinaryExpression;
import pt.iscte.paddle.model.IBinaryOperator;
import pt.iscte.paddle.model.IControlStructure;
import pt.iscte.paddle.model.IExpression;
import pt.iscte.paddle.model.IOperator.OperationType;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.model.IType;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.cfg.IBranchNode;
import pt.iscte.paddle.model.cfg.IControlFlowGraph;
import pt.iscte.paddle.model.cfg.IControlFlowGraph.Path;
import pt.iscte.paddle.model.cfg.INode;

public class DuplicateBranchGuard extends CodeAnalyser implements BadCodeAnalyser {

	public DuplicateBranchGuard(IProcedure procedure) {
		super(procedure);
	}

	private class Guard {
		private ArrayList<INode> occurences;
		private INode occ;

		public Guard(INode node) {
			this.occurences = new ArrayList<INode>();
			this.occurences.add(node);
			this.occ = node;
		}
		@Override
		public String toString() {
			return occ.toString();
		}
	}

	class GuardPair {
		private INode start;
		private INode end;

		public GuardPair(INode start, INode end) {
			this.start = start; 
			this.end = end;
		}

		public INode getEnd() {
			return end;
		}
		public INode getStart() {
			return start;
		}
	}

	private ArrayList<INode> branchConditions = new ArrayList<INode>();
	private ArrayList<Guard> duplicatedGuards = new ArrayList<Guard>();
	// Paired duplicates
	private ArrayList<GuardPair> pairs = new ArrayList<GuardPair>();

	@Override
	public void analyse(IControlFlowGraph cfg) {
		this.gatherGuardDuplicates(cfg);
		this.pairGatheredDuplicates(cfg);

		if(!pairs.isEmpty()) {
			pairs.forEach(d -> {
				List<IProgramElement> elements = new ArrayList<IProgramElement>();
				elements.add(d.getStart().getElement());
				elements.add(d.getEnd().getElement());
				issues.add(new DuplicateGuard(getProcedure(), elements));
			});
		}

	}

	public static boolean hasBeenChanged(IControlFlowGraph cfg, INode start, INode end) {
		List<Path> paths = cfg.pathsBetweenNodes(start, end);
		Set<IExpression> guardParts = Compability.extractVariables(((IExpression) start.getElement()).getParts());
		for (Path path : paths) {
			List<INode> pathNodes = path.getNodes();
			if(pathNodes.size() > 2) {
				pathNodes.remove(0);
				pathNodes.remove(pathNodes.size() - 1);
				for (INode node : pathNodes) {
					if(node.getElement() != null && node.getElement() instanceof IVariableAssignment) {
						if(start.getElement() != null && start.getElement() instanceof IBinaryExpression) {
							for (IExpression guardVar : guardParts) {
								if(guardVar.isSame(((IVariableAssignment) node.getElement()).getTarget().expression())) 
									return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	private void pairGatheredDuplicates(IControlFlowGraph cfg) {
		
		for (Guard duplicateBranchGuard : duplicatedGuards) {
			for(int i = 1; i < duplicateBranchGuard.occurences.size(); i++) {
				INode start = duplicateBranchGuard.occurences.get(i - 1);
				INode end = duplicateBranchGuard.occurences.get(i);

				if(!hasBeenChanged(cfg, start, end)) pairs.add(new GuardPair(start, end));
			}
		}

	}
	
	private boolean duplicateGuard(IExpression guard, IExpression listGuard) {
		if(guard.isSame(listGuard)) return true;
		if(guard instanceof IBinaryExpression 
				&& (((IBinaryExpression) guard).getLeftOperand() instanceof IBinaryExpression)
				&& (((IBinaryExpression) guard).getRightOperand() instanceof IBinaryExpression)) {
			for (IExpression part : guard.getParts()) {
				if(!part.isSame(IType.BOOLEAN.literal(true)) 
						&& !part.isSame(IType.BOOLEAN.literal(false)) 
						&& equalPartExpressions(part, listGuard)) 
							return true;
			}
		} else return equalPartExpressions(guard, listGuard);
		
		return false;
	}
	
	private boolean equalPartExpressions(IExpression guard, IExpression listGuard) {
		if(guard.isSame(listGuard)) return true;
		if(listGuard.getOperationType().equals(OperationType.RELATIONAL)
				|| listGuard instanceof IBinaryExpression 
				&& ((IBinaryExpression) listGuard).getOperator().equals(IBinaryOperator.OR)) return false;
		
		List<IExpression> guardParts = listGuard.getParts(); 
		for (IExpression part : guardParts) {
			
			if(part.isSame(guard)) return true;
			else if(part instanceof IBinaryExpression) {
				duplicateGuard(guard, ((IBinaryExpression) part).getLeftOperand());
				duplicateGuard(guard, ((IBinaryExpression) part).getRightOperand());
			}
		}
		return false;
	}

	private void gatherGuardDuplicates(IControlFlowGraph cfg) {
		cfg.getNodes().forEach(node -> {
			boolean existsInBranchConditions = false;
			if(node != null && node instanceof IBranchNode ) {
				for(INode c: branchConditions) {
					IControlStructure selection1 = (IControlStructure) node.getElement().getProperty(IControlStructure.class);
					IControlStructure selection2 = (IControlStructure) c.getElement().getProperty(IControlStructure.class);	
					if(!selection1.getParent().isSame(selection2.getBlock())) continue;
					
					IExpression guard = (IExpression) node.getElement();
					IExpression listGuard = (IExpression) c.getElement();
					if(duplicateGuard(guard, listGuard)) { // TODO see if guard is conjunction and check it's parts for duplicates.
						existsInBranchConditions = true;
						if(duplicatedGuards.size() == 0) {
							Guard dup = new Guard(c);
							dup.occurences.add(node);
							duplicatedGuards.add(dup);
						}
						else {
							boolean exists = false;
							for(Guard g: duplicatedGuards) {
								if(g.occ.getElement().isSame(node.getElement())) {
									g.occurences.add(node);
									exists = true;
								}
							}
							if(!exists) {
								Guard dup = new Guard(c);
								dup.occurences.add(node);
								duplicatedGuards.add(dup);
							}
						}

					}
				}
				if(!existsInBranchConditions) branchConditions.add(node);
			}

		});
	}

}
