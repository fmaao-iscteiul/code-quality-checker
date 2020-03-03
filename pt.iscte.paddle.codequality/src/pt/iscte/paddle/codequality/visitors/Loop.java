package pt.iscte.paddle.codequality.visitors;
import pt.iscte.paddle.codequality.cases.EmptyBranch;
import pt.iscte.paddle.codequality.cases.Nesting;
import pt.iscte.paddle.codequality.linter.Linter;
import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.model.ILoop;
import pt.iscte.paddle.model.IBlock.IVisitor;

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
		if(loop.getParent().isInLoop())
			Linter.getInstance().register(new Nesting(Category.NESTED_CODE, "Nested loops can lead to performance and readibility issues!"));

		return true;
	}
}
