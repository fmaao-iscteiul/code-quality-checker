package pt.iscte.paddle.linter.misc;

import pt.iscte.paddle.model.cfg.IControlFlowGraph;

public interface BadCodeAnalyser {

	public void analyse(IControlFlowGraph cfg);
}
