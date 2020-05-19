package pt.iscte.paddle.linter.visitors;
import static pt.iscte.paddle.model.IType.BOOLEAN;

import pt.iscte.paddle.linter.issues.Contradiction;
import pt.iscte.paddle.linter.issues.EmptyLoop;
import pt.iscte.paddle.linter.linter.Linter;
import pt.iscte.paddle.linter.misc.Compability;
import pt.iscte.paddle.linter.misc.Explanations;
import pt.iscte.paddle.linter.misc.IssueType;
import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.model.IBlockElement;
import pt.iscte.paddle.model.IExpression;
import pt.iscte.paddle.model.ILoop;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.model.ISelection;

public class Loop implements IVisitor{

	public static Loop build() {
		return new Loop();
	}

	@Override
	public boolean visit(ILoop loop) {
		if(!loop.getGuard().isNull() && loop.getGuard().isSame(BOOLEAN.literal(false)))
			Linter.getInstance().register(new Contradiction(Explanations.CONTRADICTION, loop.getGuard()));

		if(loop.isEmpty())
			Linter.getInstance().register(new EmptyLoop(IssueType.EMPTY_LOOP, Explanations.EMPTY_LOOP, loop));

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
