package pt.iscte.paddle.codequality.visitors;

import java.util.List;

import pt.iscte.paddle.codequality.Icfg.IControlFlowGraphBuilder;
import pt.iscte.paddle.codequality.cases.BadCodeCase;
import pt.iscte.paddle.codequality.cases.UnreachableCode;
import pt.iscte.paddle.codequality.linter.Linter;
import pt.iscte.paddle.codequality.misc.BadCodeAnalyser;
import pt.iscte.paddle.model.cfg.INode;

public class Unreachable implements BadCodeAnalyser {
	
	IControlFlowGraphBuilder cfgBuilder;
	
	public static Unreachable build(IControlFlowGraphBuilder cfg) {
		return new Unreachable(cfg);
	}
	
	private Unreachable(IControlFlowGraphBuilder cfg) {
		this.cfgBuilder = cfg;
	}

	@Override
	public void analyse() {
//		List<INode> deadNodes = this.cfgBuilder.getCFG().deadNodes();
//		if(!this.cfgBuilder.getCFG().deadNodes().isEmpty()) {
//			String explanation = "bla bla bla";
//			Linter.getInstance().register(new UnreachableCode(Category.DEAD_CODE, explanation, deadNodes));
//		}
	}

}
