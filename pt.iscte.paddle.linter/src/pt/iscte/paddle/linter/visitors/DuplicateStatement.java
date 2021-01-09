package pt.iscte.paddle.linter.visitors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import pt.iscte.paddle.linter.cases.base.CodeAnalyser;
import pt.iscte.paddle.linter.cases.base.QualityIssue;
import pt.iscte.paddle.linter.issues.Duplicate;
import pt.iscte.paddle.linter.linter.Linter;
import pt.iscte.paddle.linter.misc.BadCodeAnalyser;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.model.cfg.IControlFlowGraph;
import pt.iscte.paddle.model.cfg.INode;

public class DuplicateStatement extends CodeAnalyser implements BadCodeAnalyser {

	public DuplicateStatement(IProcedure procedure) {
		super(procedure);
	}

	@Override
	public void analyse(IControlFlowGraph cfg) {
		for(INode graphNode : cfg.getNodes()) {
			Set<INode> duplicates = new HashSet<INode>();

			for(INode incomingNode: graphNode.getIncomming()) {
				
				dance:
				for(INode anotherIncoming: graphNode.getIncomming()) {
					if(anotherIncoming != null && incomingNode != null && graphNode != null && anotherIncoming.getElement() != null && incomingNode.getElement() != null 
							&& !graphNode.isExit() && !anotherIncoming.equals(incomingNode)

							&& anotherIncoming.getElement().isSame(incomingNode.getElement()))
						duplicates.add(anotherIncoming);
					else break dance;
				}
			}

			if(duplicates.size() > 1) {
				ArrayList<IProgramElement> occurrences = new ArrayList<IProgramElement>();
				duplicates.forEach(d -> occurrences.add(d.getElement()));
				issues.add(new Duplicate(getProcedure(), occurrences));
			}
		};
	}

}
