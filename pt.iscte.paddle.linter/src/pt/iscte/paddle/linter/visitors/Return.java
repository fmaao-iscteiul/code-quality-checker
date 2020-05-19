package pt.iscte.paddle.linter.visitors;

import pt.iscte.paddle.linter.issues.BooleanReturnCheck;
import pt.iscte.paddle.linter.linter.Linter;
import pt.iscte.paddle.linter.misc.BadCodeAnalyser;
import pt.iscte.paddle.model.IControlStructure;
import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.model.IReturn;
import pt.iscte.paddle.model.IType;
import pt.iscte.paddle.model.cfg.IBranchNode;
import pt.iscte.paddle.model.cfg.IControlFlowGraph;
import pt.iscte.paddle.model.cfg.INode;

public class Return implements BadCodeAnalyser{

	IControlFlowGraph cfg;

	public static Return build(IControlFlowGraph cfg) {
		return new Return(cfg);
	}

	private Return(IControlFlowGraph cfg) {
		this.cfg = cfg;
	}

	@Override
	public void analyse() {
		for (INode node : cfg.getNodes()) {
			if(node instanceof IBranchNode 
					&& ((IBranchNode) node).hasBranch() 
					&& ((IBranchNode) node).getAlternative().getElement() instanceof IReturn) {
				IReturn ret = (IReturn) ((IBranchNode) node).getAlternative().getElement();
				IControlStructure s = node.getElement().getProperty(IControlStructure.class);
				if(s.getGuard().getType().equals(IType.BOOLEAN) 
						&& (ret.getExpression().isSame(IType.BOOLEAN.literal(true))
						|| ret.getReturnValueType().isSame(IType.BOOLEAN.literal(false)))) 
					Linter.getInstance().register(new BooleanReturnCheck(s));
			}
		}

	}


}
