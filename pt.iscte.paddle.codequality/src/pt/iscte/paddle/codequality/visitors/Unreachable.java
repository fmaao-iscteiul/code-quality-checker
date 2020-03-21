package pt.iscte.paddle.codequality.visitors;

import java.util.List;

import pt.iscte.paddle.codequality.Icfg.IControlFlowGraphBuilder;
import pt.iscte.paddle.codequality.cases.BadCodeCase;
import pt.iscte.paddle.codequality.cases.UnreachableCode;
import pt.iscte.paddle.codequality.linter.Linter;
import pt.iscte.paddle.codequality.misc.BadCodeAnalyser;
import pt.iscte.paddle.codequality.misc.Explanations;
import pt.iscte.paddle.model.cfg.IControlFlowGraph;
import pt.iscte.paddle.model.cfg.INode;

public class Unreachable implements BadCodeAnalyser {
	
	IControlFlowGraph cfg;
	
	public static Unreachable build(IControlFlowGraph cfg) {
		return new Unreachable(cfg);
	}
	
	private Unreachable(IControlFlowGraph cfg) {
		this.cfg = cfg;
	}

	@Override
	public void analyse() {
		List<INode> deadNodes = this.cfg.deadNodes();
		if(!this.cfg.deadNodes().isEmpty()) {
			Linter.getInstance().register(new UnreachableCode(Explanations.UNREACHABLE_CODE, deadNodes));
		}
	}

}
