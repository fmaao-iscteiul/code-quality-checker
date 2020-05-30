package pt.iscte.paddle.linter.visitors;

import java.util.ArrayList;
import java.util.List;

import pt.iscte.paddle.linter.cases.base.CodeAnalyser;
import pt.iscte.paddle.linter.cases.base.QualityIssue;
import pt.iscte.paddle.linter.issues.UnreachableCode;
import pt.iscte.paddle.linter.linter.Linter;
import pt.iscte.paddle.linter.misc.BadCodeAnalyser;
import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.model.IReturn;
import pt.iscte.paddle.model.cfg.IControlFlowGraph;
import pt.iscte.paddle.model.cfg.INode;

public class Unreachable extends CodeAnalyser implements BadCodeAnalyser {
	
	@Override
	public void analyse(IControlFlowGraph cfg) {
		List<INode> deadNodes = cfg.deadNodes();
		cfg.getNodes().forEach(node -> {
			if(!node.isEntry() && !node.isExit() && node.getIncomming().isEmpty()) {
				int index = cfg.getNodes().indexOf(node);
				INode n = cfg.getNodes().get(index - 1);
				if(n.getElement() != null && n.getElement() instanceof IReturn)
					deadNodes.add(0, n);
			}
				
		});
		if(!cfg.deadNodes().isEmpty()) {
			ArrayList<IProgramElement> occurrences = new ArrayList<IProgramElement>();
			deadNodes.forEach(d -> occurrences.add(d.getElement()));
			issues.add(new UnreachableCode(occurrences));
		}
	}

}
