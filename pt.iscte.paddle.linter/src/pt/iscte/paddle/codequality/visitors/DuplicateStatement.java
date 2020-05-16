package pt.iscte.paddle.codequality.visitors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import pt.iscte.paddle.codequality.issues.Duplicate;
import pt.iscte.paddle.codequality.linter.Linter;
import pt.iscte.paddle.codequality.misc.BadCodeAnalyser;
import pt.iscte.paddle.codequality.misc.Explanations;
import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.cfg.IControlFlowGraph;
import pt.iscte.paddle.model.cfg.INode;

public class DuplicateStatement implements BadCodeAnalyser{

	private IControlFlowGraph cfg;

	private DuplicateStatement(IControlFlowGraph cfg) {
		this.cfg = cfg;
	}

	public static DuplicateStatement build(IControlFlowGraph cfgBuilder) {
		return new DuplicateStatement(cfgBuilder);
	}

	@Override
	public void analyse() {
		for(INode node : this.cfg.getNodes()) {
			Set<INode> duplicates = new HashSet<INode>();
			for(INode incoming: node.getIncomming()) 
				for(INode n: node.getIncomming()) 
					if(n != null && incoming != null && !node.isExit() && !n.equals(incoming) && n.getElement().isSame(incoming.getElement())) 
						duplicates.add(n);
			if(duplicates.size() > 1) {
				ArrayList<IProgramElement> occurrences = new ArrayList<IProgramElement>();
				duplicates.forEach(d -> occurrences.add(d.getElement()));
				Linter.getInstance().register(new Duplicate(occurrences));
			}
		};
	}

}
