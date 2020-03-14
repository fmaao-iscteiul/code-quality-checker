package pt.iscte.paddle.codequality.visitors;
import pt.iscte.paddle.codequality.cases.DuplicateGuard;
import pt.iscte.paddle.codequality.cases.EmptyBranch;
import pt.iscte.paddle.codequality.cases.Nesting;
import pt.iscte.paddle.codequality.linter.Linter;
import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.codequality.misc.Explanations;
import pt.iscte.paddle.model.ILoop;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.model.ISelection;
import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.model.IBlockElement;
import pt.iscte.paddle.model.IControlStructure;
import pt.iscte.paddle.model.IExpression;

public class Loop implements IVisitor{

	public static Loop build() {
		return new Loop();
	}

	@Override
	public boolean visit(ILoop loop) {

		if(loop.isEmpty())
			Linter.getInstance().register(new EmptyBranch(Category.EMPTY_LOOP, Explanations.EMPTY_LOOP, loop));

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

	@Override
	public boolean visit(ISelection selection) {
		//		IExpression loopGuard = this.getParentLoopGuard(selection);
		//		if(selection.getBlock().isInLoop() && loopGuard != null && loopGuard.toString().equals(selection.getGuard().toString())) {
		//			Linter.getInstance().register(new DuplicateLoopGuard(selection.getGuard()));
		//		}
		//		return IVisitor.super.visit(selection);
		return true;
	}
}
