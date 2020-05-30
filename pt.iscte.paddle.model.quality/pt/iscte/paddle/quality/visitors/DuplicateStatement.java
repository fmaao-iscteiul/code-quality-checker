package pt.iscte.paddle.quality.visitors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.model.cfg.IControlFlowGraph;
import pt.iscte.paddle.model.cfg.INode;
import pt.iscte.paddle.quality.cases.base.CodeAnalyser;
import pt.iscte.paddle.quality.cases.base.QualityIssue;
import pt.iscte.paddle.quality.client.Linter;
import pt.iscte.paddle.quality.issues.Duplicate;
import pt.iscte.paddle.quality.misc.BadCodeAnalyser;

public class DuplicateStatement extends CodeAnalyser implements BadCodeAnalyser {

	@Override
	public void analyse(IControlFlowGraph cfg) {
		for(INode node : cfg.getNodes()) {
			Set<INode> duplicates = new HashSet<INode>();
			for(INode incoming: node.getIncomming()) 
				for(INode n: node.getIncomming()) 
					if(n != null && incoming != null && !node.isExit() && !n.equals(incoming) && n.getElement().isSame(incoming.getElement())) 
						duplicates.add(n);
			if(duplicates.size() > 1) {
				ArrayList<IProgramElement> occurrences = new ArrayList<IProgramElement>();
				duplicates.forEach(d -> occurrences.add(d.getElement()));
				issues.add(new Duplicate(occurrences));
			}
		};
	}

}
