package pt.iscte.paddle.codequality.ICfg;

import pt.iscte.paddle.codequality.cfg.Handler;
import pt.iscte.paddle.codequality.cfg.Visitor;
import pt.iscte.paddle.model.cfg.IBranchNode;
import pt.iscte.paddle.model.cfg.IControlFlowGraph;
import pt.iscte.paddle.model.cfg.INode;

public interface IVisitHandler {
	
	void handleStatementVisit(INode statement);
	void handleBranchVisit(IBranchNode branch);
	
	void setLastLoopNext(INode node);
	void setLastSelectionNext(INode node);
	void setLastBreakNext(INode node);
	void handleOrphansAdoption(INode node);
	void setReturnStatementNext(INode ret);
	void updateOrphansList(INode orphan);
	
	static Handler create(IControlFlowGraph cfg, Visitor visitor) {
		return new Handler(cfg, visitor);
	}
}
