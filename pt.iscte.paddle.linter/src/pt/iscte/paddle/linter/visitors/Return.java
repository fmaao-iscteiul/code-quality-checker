package pt.iscte.paddle.linter.visitors;

import pt.iscte.paddle.linter.cases.base.CodeAnalyser;
import pt.iscte.paddle.linter.issues.BooleanReturnCheck;
import pt.iscte.paddle.linter.issues.UselessReturn;
import pt.iscte.paddle.linter.misc.BadCodeAnalyser;
import pt.iscte.paddle.model.IControlStructure;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IReturn;
import pt.iscte.paddle.model.ISelection;
import pt.iscte.paddle.model.IType;
import pt.iscte.paddle.model.cfg.IBranchNode;
import pt.iscte.paddle.model.cfg.IControlFlowGraph;
import pt.iscte.paddle.model.cfg.INode;

public class Return extends CodeAnalyser implements BadCodeAnalyser {

	public Return(IProcedure procedure) {
		super(procedure);
	}

	@Override
	public void analyse(IControlFlowGraph cfg) {
		if(cfg.getProcedure().getReturnType().isVoid() && cfg.getExitNode().getIncomming().size() == 1) {
			INode lastStatement = cfg.getExitNode().getIncomming().iterator().next();
			if(lastStatement.getElement() instanceof IReturn) {
				issues.add(new UselessReturn(cfg.getProcedure(), (IReturn) lastStatement.getElement() ));
			}
		}
		for (INode node : cfg.getNodes()) {

			//			if(node.getElement() != null && node.getElement() instanceof IReturn) {
			//				IReturn ret = (IReturn) node.getElement();
			//
			//				if(ret.getOwnerProcedure().getReturnType().equals(IType.VOID)
			//						&& ret.getOwnerProcedure().getBlock().getChildren().contains(ret))
			//					issues.add(new UselessReturn(ret.getOwnerProcedure(), ret));
			//			}

			if(node instanceof IBranchNode && ((IBranchNode) node).hasBranch() ) {
				IControlStructure s = node.getElement().getProperty(IControlStructure.class);
				if(s != null && s.getGuard().getType().equals(IType.BOOLEAN) && 
						s instanceof ISelection && ((ISelection) s).hasAlternativeBlock() 
						&& ((IBranchNode) node).getNext().getElement() instanceof IReturn 
						&& ((IBranchNode) node).getAlternative().getElement() instanceof IReturn) {
					IReturn altRet = (IReturn) ((IBranchNode) node).getAlternative().getElement();
					IReturn nextRet = (IReturn) ((IBranchNode) node).getNext().getElement();
					if((altRet.getExpression() != null && altRet.getExpression().isSame(IType.BOOLEAN.literal(true))
							&& nextRet.getExpression() != null && nextRet.getExpression().isSame(IType.BOOLEAN.literal(false)))
							//							|| (altRet.getExpression() != null && altRet.getExpression().isSame(IType.BOOLEAN.literal(false))
							//							&& nextRet.getExpression() != null && nextRet.getExpression().isSame(IType.BOOLEAN.literal(true)))
							) {
						issues.add(new BooleanReturnCheck(getProcedure(), s));
					}
				}
			}
		}

	}


}
