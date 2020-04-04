package pt.iscte.paddle.codequality.visitors;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import pt.iscte.paddle.codequality.linter.Linter;
import pt.iscte.paddle.codequality.misc.BadCodeAnalyser;
import pt.iscte.paddle.codequality.misc.Compability;
import pt.iscte.paddle.model.IBinaryExpression;
import pt.iscte.paddle.model.IExpression;
import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.cfg.IBranchNode;
import pt.iscte.paddle.model.cfg.IControlFlowGraph;
import pt.iscte.paddle.model.cfg.IControlFlowGraph.Path;
import pt.iscte.paddle.model.cfg.INode;

public class DuplicateGuard implements BadCodeAnalyser {

	private IControlFlowGraph cfg;

	private DuplicateGuard(IControlFlowGraph cfg) {
		this.cfg = cfg;
	}

	public static DuplicateGuard build(IControlFlowGraph cfgBuilder) {
		return new DuplicateGuard(cfgBuilder);
	}

	class DuplicateBranchGuard {
		private ArrayList<INode> occurences;
		private INode occ;

		public DuplicateBranchGuard(INode node) {
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
	private ArrayList<DuplicateBranchGuard> duplicatedGuards = new ArrayList<DuplicateBranchGuard>();
	// Paired duplicates
	private ArrayList<GuardPair> pairs = new ArrayList<GuardPair>();

	@Override
	public void analyse() {

		this.gatherGuardDuplicates();
		this.pairGatheredDuplicates();

		if(!pairs.isEmpty()) {
			pairs.forEach(d -> {
				List<IProgramElement> elements = new ArrayList<IProgramElement>();
				elements.add(d.getStart().getElement());
				elements.add(d.getEnd().getElement());
				Linter.getInstance().register(new pt.iscte.paddle.codequality.cases.DuplicateGuard(elements));
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
							System.out.println(guardParts + " - " + ((IVariableAssignment) node.getElement()).getTarget().expression());
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

	private void pairGatheredDuplicates() {

		for (DuplicateBranchGuard duplicateBranchGuard : duplicatedGuards) {
			for(int i = 1; i < duplicateBranchGuard.occurences.size(); i++) {
				INode start = duplicateBranchGuard.occurences.get(i - 1);
				INode end= duplicateBranchGuard.occurences.get(i);

				if(!hasBeenChanged(cfg, start, end)) pairs.add(new GuardPair(start, end));
			}
		}

	}

	private void gatherGuardDuplicates() {
		this.cfg.getNodes().forEach(node -> {
			boolean existsInBranchConditions = false;
			if(node != null && node instanceof IBranchNode ) {
				for(INode c: branchConditions) {
					if(node.getElement().isSame(c.getElement())) {
						existsInBranchConditions = true;
						if(duplicatedGuards.size() == 0) {
							DuplicateBranchGuard dup = new DuplicateBranchGuard(c);
							dup.occurences.add(node);
							duplicatedGuards.add(dup);
						}
						else {
							boolean exists = false;
							for(DuplicateBranchGuard g: duplicatedGuards) {
								if(g.occ.getElement().isSame(node.getElement())) {
									g.occurences.add(node);
									exists = true;
								}
							}
							if(!exists) {
								DuplicateBranchGuard dup = new DuplicateBranchGuard(c);
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
