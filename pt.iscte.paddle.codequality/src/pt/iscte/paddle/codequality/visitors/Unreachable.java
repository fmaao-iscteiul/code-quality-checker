package pt.iscte.paddle.codequality.visitors;

import java.util.List;

import pt.iscte.paddle.codequality.Icfg.IControlFlowGraphBuilder;
import pt.iscte.paddle.codequality.cases.BadCodeCase;
import pt.iscte.paddle.codequality.cases.UnreachableCode;
import pt.iscte.paddle.codequality.cases.BadCodeCase.Category;
import pt.iscte.paddle.codequality.linter.Linter;
import pt.iscte.paddle.codequality.misc.BadCodeAnalyser;
import pt.iscte.paddle.model.cfg.INode;

public class Unreachable implements BadCodeAnalyser {
	
	IControlFlowGraphBuilder cfg;
	
	public static Unreachable build(IControlFlowGraphBuilder cfg) {
		return new Unreachable(cfg);
	}
	
	private Unreachable(IControlFlowGraphBuilder cfg) {
		this.cfg = cfg;
	}

	@Override
	public void analyse() {
		List<INode> deadNodes = this.cfg.getCFG().deadNodes();
		if(!this.cfg.getCFG().deadNodes().isEmpty()) {
			String explanation = "bla bla bla";
			Linter.getInstance().register(new UnreachableCode.Builder(deadNodes).addCategory(Category.DEAD_CODE).setExplanation(explanation).build());
		}
	}

}
