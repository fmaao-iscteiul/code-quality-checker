package pt.iscte.paddle.quality.misc;

import pt.iscte.paddle.model.cfg.IControlFlowGraph;

public interface BadCodeAnalyser {

	public void analyse(IControlFlowGraph cfg);
}
