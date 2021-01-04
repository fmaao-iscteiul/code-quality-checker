package pt.iscte.paddle.linter.visitors;
import static pt.iscte.paddle.model.IType.BOOLEAN;

import pt.iscte.paddle.linter.cases.base.CodeAnalyser;
import pt.iscte.paddle.linter.issues.Contradiction;
import pt.iscte.paddle.linter.issues.EmptyBlock;
import pt.iscte.paddle.linter.misc.Explanations;
import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.model.IBlockElement;
import pt.iscte.paddle.model.IExpression;
import pt.iscte.paddle.model.ILoop;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.model.ISelection;

public class Loop extends CodeAnalyser implements IVisitor{

	public Loop(IProcedure procedure) {
		super(procedure);
	}

	@Override
	public boolean visit(ILoop loop) {
		if(!loop.getGuard().isNull() && loop.getGuard().isSame(BOOLEAN.literal(false))) {
			issues.add(new Contradiction(Explanations.CONTRADICTION, getProcedure(), loop.getGuard()));
		}


		if(loop.isEmpty()) {
			issues.add(new EmptyBlock(Explanations.EMPTY_LOOP, getProcedure(), loop.getBlock()));	
		}


		return true;
	}

	private IExpression getParentLoopGuard(ISelection element) {
		IProgramElement p = element.getParent();
		while(!(p instanceof IProcedure))
			if(p instanceof ILoop)
				return ((ILoop) p).getGuard();
			else
				p = ((IBlockElement) p).getParent();

		return null;
	}

}
