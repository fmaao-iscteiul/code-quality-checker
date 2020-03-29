package pt.iscte.paddle.codequality.visitors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.iscte.paddle.codequality.linter.Linter;
import pt.iscte.paddle.codequality.misc.BadCodeAnalyser;
import pt.iscte.paddle.model.IBinaryExpression;
import pt.iscte.paddle.model.IExpression;
import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.IVariableExpression;
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

	class DuplicateBranchGuard{
		private ArrayList<INode> occurences;
		private ArrayList<INode> duplicates;
		private INode occ;

		public DuplicateBranchGuard(INode node) {
			this.occurences = new ArrayList<INode>();
			this.duplicates = new ArrayList<INode>();
			this.occ = node;
			this.occurences.add(node);
			this.duplicates.add(node);
		}
		@Override
		public String toString() {
			return occ.toString();
		}

	}

	private ArrayList<INode> branchConditions = new ArrayList<INode>();
	ArrayList<DuplicateBranchGuard> duplicatedGuards = new ArrayList<DuplicateBranchGuard>();

	@Override
	public void analyse() {

		this.cfg.getNodes().forEach(node -> {
			boolean existsInBranchConditions = false;
			if(node != null && node instanceof IBranchNode ) {
				for(INode c: branchConditions) {
					if(node.getElement().isSame(c.getElement())) {
						existsInBranchConditions = true;
						if(duplicatedGuards.size() == 0) {
							DuplicateBranchGuard dup = new DuplicateBranchGuard(c);
							dup.occurences.add(node);
							dup.duplicates.add(node);
							duplicatedGuards.add(dup);
						}
						else {
							boolean exists = false;
							for(DuplicateBranchGuard g: duplicatedGuards) {
								if(g.occ.getElement().isSame(node.getElement())) {
									g.occurences.add(node);
									g.duplicates.add(node);
									exists = true;
								}
							}
							if(!exists) {
								DuplicateBranchGuard dup = new DuplicateBranchGuard(c);
								dup.occurences.add(node);
								dup.duplicates.add(node);
								duplicatedGuards.add(dup);
							}
						}

					}
				}
				if(!existsInBranchConditions) branchConditions.add(node);
			}

		});
		for(DuplicateBranchGuard c: duplicatedGuards) {
			for (int i = 0; i < c.occurences.size() - 1; i++) {
				INode n1 = c.occurences.get(i);
				INode n2 = c.occurences.get(i + 1);
				
				if(n1.isEquivalentTo(n2)) continue;
				
				List<Path> paths = this.cfg.pathsBetweenNodes(n1, n2);
				paths.forEach(path -> {

					List<INode> pathNodes = path.getNodes();
					if(pathNodes.size() > 2) {
						pathNodes.remove(0);
						pathNodes.remove(pathNodes.size() - 1);
						pathNodes.forEach(node -> {
							if(node.getElement() != null && node.getElement() instanceof IVariableAssignment) {
								if(c.occ.getElement() != null && c.occ.getElement() instanceof IBinaryExpression) {
									IVariableExpression vExp_l = (IVariableExpression) ((IBinaryExpression) c.occ.getElement()).getLeftOperand();
									IVariableExpression vExp_r = (IVariableExpression) ((IBinaryExpression) c.occ.getElement()).getRightOperand();
									if(vExp_l.getVariable().isSame(((IVariableAssignment) node.getElement()).getTarget()) 
										|| vExp_r.getVariable().isSame(((IVariableAssignment) node.getElement()).getTarget())){
										c.duplicates.remove(n2);
									}
								}
							}
						});
					}
				});
			}
		}

		if(!duplicatedGuards.isEmpty()) {
			List<IProgramElement> elements = new ArrayList<IProgramElement>();
			duplicatedGuards.forEach(d -> d.duplicates.forEach(o -> elements.add(o.getElement())));
			Linter.getInstance().register(new pt.iscte.paddle.codequality.cases.DuplicateGuard(elements));
		}

	}

}
