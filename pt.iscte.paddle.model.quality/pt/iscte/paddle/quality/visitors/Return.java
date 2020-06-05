package pt.iscte.paddle.quality.visitors;

import pt.iscte.paddle.model.IControlStructure;
import pt.iscte.paddle.model.IReturn;
import pt.iscte.paddle.model.IType;
import pt.iscte.paddle.model.cfg.IBranchNode;
import pt.iscte.paddle.model.cfg.IControlFlowGraph;
import pt.iscte.paddle.model.cfg.INode;
import pt.iscte.paddle.quality.cases.base.CodeAnalyser;
import pt.iscte.paddle.quality.cases.base.QualityIssue;
import pt.iscte.paddle.quality.client.Linter;
import pt.iscte.paddle.quality.issues.BooleanReturnCheck;
import pt.iscte.paddle.quality.issues.UnnecessaryReturn;
import pt.iscte.paddle.quality.misc.BadCodeAnalyser;

public class Return extends CodeAnalyser implements BadCodeAnalyser {

	@Override
	public void analyse(IControlFlowGraph cfg) {
		for (INode node : cfg.getNodes()) {
			if(node instanceof IBranchNode 
					&& ((IBranchNode) node).hasBranch() 
					&& ((IBranchNode) node).getAlternative().getElement() instanceof IReturn) {
				IReturn ret = (IReturn) ((IBranchNode) node).getAlternative().getElement();
				IControlStructure s = node.getElement().getProperty(IControlStructure.class);
				if(s.getGuard().getType().equals(IType.BOOLEAN) && (s.getGuard().getNumberOfParts() == 0 || s.getGuard().getNumberOfParts() == 1) 
						&& (ret != null && ret.getExpression() != null && ret.getExpression().isSame(IType.BOOLEAN.literal(true))
						|| ret.getReturnValueType().isSame(IType.BOOLEAN.literal(false)))) {
					issues.add(new BooleanReturnCheck(s));
				}
			}
			else if(node.getNext() != null 
					&& node.getNext().isExit() 
					&& node.getElement() != null 
					&& node.getElement() instanceof IReturn 
					&& cfg.getProcedure().getReturnType().isVoid()
					&& !((IReturn) node.getElement()).isError()
					&& !(((IReturn) node.getElement()).getParent().getParent() instanceof IControlStructure)) {
				issues.add(new UnnecessaryReturn(node.getElement()));
			}

		}

	}


}
