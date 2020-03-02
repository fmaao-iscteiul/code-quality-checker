package pt.iscte.paddle.codequality.visitors;

import pt.iscte.paddle.model.cfg.IBranchNode;
import pt.iscte.paddle.model.cfg.INode;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import pt.iscte.paddle.codequality.Icfg.IControlFlowGraphBuilder;
import pt.iscte.paddle.codequality.cases.Duplicate;
import pt.iscte.paddle.codequality.linter.Linter;
import pt.iscte.paddle.codequality.misc.BadCodeAnalyser;

public class Duplication implements BadCodeAnalyser{

	IControlFlowGraphBuilder cfgBuilder;

	private Duplication(IControlFlowGraphBuilder cfgBuilder) {
		this.cfgBuilder = cfgBuilder;
	}

	public static Duplication build(IControlFlowGraphBuilder cfgBuilder) {
		return new Duplication(cfgBuilder);
	}

	@Override
	public void analyse() {
		for(INode node : this.cfgBuilder.getCFG().getNodes()) {
			List<INode> stack = new ArrayList<INode>();
			List<INode> duplicates = new ArrayList<INode>();
			for (INode incoming : node.getIncomming()) {
//				System.out.println(incoming.getElement().getId());
				if((incoming instanceof IBranchNode) || node.getIncomming().size() <= 1) continue;
				else if(stack.contains(incoming)) {
					duplicates.add(incoming);
					System.out.println("I found a duplicate: " + incoming);
					break;
				} 
				else stack.add(incoming);
			}
			if(duplicates.size() > 0) Linter.getInstance().register(new Duplicate("", duplicates));
		};
	}
}
