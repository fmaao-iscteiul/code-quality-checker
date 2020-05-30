package pt.iscte.paddle.quality.visitors;
import static pt.iscte.paddle.model.IType.BOOLEAN;

import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.quality.cases.base.CodeAnalyser;
import pt.iscte.paddle.quality.cases.base.QualityIssue;
import pt.iscte.paddle.quality.client.Linter;
import pt.iscte.paddle.quality.issues.Contradiction;
import pt.iscte.paddle.quality.issues.EmptyLoop;
import pt.iscte.paddle.quality.misc.Explanations;
import pt.iscte.paddle.quality.misc.IssueType;
import pt.iscte.paddle.model.IBlockElement;
import pt.iscte.paddle.model.IExpression;
import pt.iscte.paddle.model.ILoop;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.model.ISelection;

public class Loop extends CodeAnalyser implements IVisitor{

	@Override
	public boolean visit(ILoop loop) {
		if(!loop.getGuard().isNull() && loop.getGuard().isSame(BOOLEAN.literal(false))) {
			issues.add(new Contradiction(Explanations.CONTRADICTION, loop.getGuard()));
		}


		if(loop.isEmpty()) {
			issues.add(new EmptyLoop(IssueType.EMPTY_LOOP, Explanations.EMPTY_LOOP, loop));	
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
