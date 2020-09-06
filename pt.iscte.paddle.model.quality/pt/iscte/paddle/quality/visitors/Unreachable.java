package pt.iscte.paddle.quality.visitors;

import java.util.ArrayList;
import java.util.List;

import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.model.IReturn;
import pt.iscte.paddle.model.cfg.IControlFlowGraph;
import pt.iscte.paddle.model.cfg.INode;
import pt.iscte.paddle.quality.cases.base.CodeAnalyser;
import pt.iscte.paddle.quality.cases.base.QualityIssue;
import pt.iscte.paddle.quality.client.Linter;
import pt.iscte.paddle.quality.issues.UnreachableCode;
import pt.iscte.paddle.quality.misc.BadCodeAnalyser;

public class Unreachable extends CodeAnalyser implements BadCodeAnalyser {
	
	@Override
	public void analyse(IControlFlowGraph cfg) {
		List<INode> deadNodes = cfg.deadNodes();
		cfg.getNodes().forEach(node -> {
			if(!node.isEntry() && !node.isExit() && node.getIncomming().isEmpty()) {
				int index = cfg.getNodes().indexOf(node);
				INode n = cfg.getNodes().get(index - 1);
				if(n.getElement() != null && n.getElement() instanceof IReturn && !((IReturn) n.getElement()).isError())
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
