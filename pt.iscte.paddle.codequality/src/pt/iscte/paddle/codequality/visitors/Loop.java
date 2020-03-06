package pt.iscte.paddle.codequality.visitors;
import pt.iscte.paddle.codequality.cases.DuplicateLoopGuard;
import pt.iscte.paddle.codequality.cases.EmptyBranch;
import pt.iscte.paddle.codequality.cases.Nesting;
import pt.iscte.paddle.codequality.linter.Linter;
import pt.iscte.paddle.codequality.misc.Category;
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

		if(loop.isEmpty()) {

			final String explanation = "For other than iteration porpuses, an empty loop is an unnecessary chunk of code! It serves only tyhe porpuse of filling the code base"
					+ "with non-relevant chunks.";

			EmptyBranch emptyCondition = new EmptyBranch(Category.EMPTY_LOOP, explanation, loop);

			Linter.getInstance().register(emptyCondition);
		}
		//		if(loop.getParent().isInLoop())
		//			Linter.getInstance().register(new Nesting(Category.NESTED_CODE, "Nested loops can lead to performance and readibility issues!"));

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
		IExpression loopGuard = this.getParentLoopGuard(selection);
		if(selection.getBlock().isInLoop() && loopGuard != null && loopGuard.toString().equals(selection.getGuard().toString())) {
			Linter.getInstance().register(new DuplicateLoopGuard(selection.getGuard()));
		}
		return IVisitor.super.visit(selection);
	}
}
