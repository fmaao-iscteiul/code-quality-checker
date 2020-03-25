package pt.iscte.paddle.codequality.visitors;

import java.util.ArrayList;
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
		private INode occ;

		public DuplicateBranchGuard(INode node) {
			this.occurences = new ArrayList<INode>();
			this.occ = node;
			this.occurences.add(node);
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
			if(node != null && node instanceof IBranchNode ) {
				System.out.println(node.getElement());
				for(INode c: branchConditions) {
					if(node.getElement().isSame(c.getElement())) {
						if(duplicatedGuards.size() == 0) {
							DuplicateBranchGuard dup = new DuplicateBranchGuard(c);
							dup.occurences.add(node);
							duplicatedGuards.add(dup);
						}
						else {
							for(DuplicateBranchGuard g: duplicatedGuards) {
								if(g.occ.getElement().isSame(node.getElement())) g.occurences.add(node);
								else {
									DuplicateBranchGuard dup = new DuplicateBranchGuard(c);
									dup.occurences.add(node);
									duplicatedGuards.add(dup);
								}
							}

						}

					}
				}
				branchConditions.add(node);
				//				IVariableExpression vExp = (IVariableExpression) ((IBinaryExpression) node.getElement()).getLeftOperand();
				//				FixedValue fExp = new FixedValue(vExp.getVariable());
			}

			//			if(node != null && node instanceof IBranchNode 
			//					&& ((IBranchNode) node).hasBranch()
			//					&& ((IBranchNode) node).getAlternative() instanceof IBranchNode
			//					&& node.getElement().isSame(((IBranchNode) node).getAlternative().getElement())) {
			//				duplicatedGuards.add(node.getElement());
			//				duplicatedGuards.add(((IBranchNode) node).getAlternative().getElement());
			//			}
		});
		ArrayList<DuplicateBranchGuard> itemsToRemove = new ArrayList<DuplicateBranchGuard>();

		for(DuplicateBranchGuard c: duplicatedGuards) {

			for (int i = 0; i < c.occurences.size(); i++) {
				System.out.println("--------------------");
				System.out.println(c.occurences);
				if(i + 1 == c.occurences.size()) continue;
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
								System.out.println(node.getElement());
								if(c.occ.getElement() != null && c.occ.getElement() instanceof IBinaryExpression) {
									IVariableExpression vExp = (IVariableExpression) ((IBinaryExpression) c.occ.getElement()).getLeftOperand();
									if(vExp.getVariable().isSame(((IVariableAssignment) node.getElement()).getTarget())){
										
										c.occurences.remove(n2);
									}
								}
							}
						});
					}
				});
			}
		}

		List<IProgramElement> elements = new ArrayList<IProgramElement>();
		duplicatedGuards.forEach(d -> d.occurences.forEach(o -> elements.add(o.getElement())));
		Linter.getInstance().register(new pt.iscte.paddle.codequality.cases.DuplicateGuard(elements));
	}

}
