package pt.iscte.paddle.codequality.visitors;

import java.util.ArrayList;

import pt.iscte.paddle.codequality.Icfg.IControlFlowGraphBuilder;
import pt.iscte.paddle.codequality.linter.Linter;
import pt.iscte.paddle.codequality.misc.BadCodeAnalyser;
import pt.iscte.paddle.model.IBlockElement;
import pt.iscte.paddle.model.IControlStructure;
import pt.iscte.paddle.model.IExpression;
import pt.iscte.paddle.model.ILoop;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.model.ISelection;
import pt.iscte.paddle.model.cfg.IBranchNode;

public class DuplicateGuard implements BadCodeAnalyser {

	private IControlFlowGraphBuilder cfgBuilder;

	private DuplicateGuard(IControlFlowGraphBuilder cfgBuilder) {
		this.cfgBuilder = cfgBuilder;
	}

	public static DuplicateGuard build(IControlFlowGraphBuilder cfgBuilder) {
		return new DuplicateGuard(cfgBuilder);
	}

	@Override
	public void analyse() {
		ArrayList<IProgramElement> duplicatedGuards = new ArrayList<IProgramElement>();
		this.cfgBuilder.getCFG().getNodes().forEach(node -> {
			if(node != null && node instanceof IBranchNode 
					&& ((IBranchNode) node).hasBranch() 
					&& ((IBranchNode) node).getAlternative() instanceof IBranchNode
					&& node.getElement().isSame(((IBranchNode) node).getAlternative().getElement())) {
				duplicatedGuards.add(node.getElement());
				duplicatedGuards.add(((IBranchNode) node).getAlternative().getElement());
			}
		});
		if(duplicatedGuards.size() > 0) 
			Linter.getInstance().register(new pt.iscte.paddle.codequality.cases.DuplicateGuard(duplicatedGuards));
	}

}
