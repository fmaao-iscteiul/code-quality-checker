package pt.iscte.paddle.codequality.visitors;
import pt.iscte.paddle.codequality.cases.BooleanCheck;
import pt.iscte.paddle.codequality.cases.EmptyBranch;
import pt.iscte.paddle.codequality.cases.Nesting;
import pt.iscte.paddle.codequality.cases.BadCodeCase.Category;
import pt.iscte.paddle.codequality.linter.Linter;
import pt.iscte.paddle.codequality.misc.BranchVerifications;
import pt.iscte.paddle.javali.translator.ElementLocation;
import pt.iscte.paddle.model.IBinaryExpression;
import pt.iscte.paddle.model.IBinaryOperator;
import pt.iscte.paddle.model.IExpression;
import pt.iscte.paddle.model.ILoop;
import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.model.IControlStructure;
import pt.iscte.paddle.model.IOperator.OperationType;

public class Loop implements IVisitor{

	public static Loop build() {
		return new Loop();
	}

	@Override
	public boolean visit(ILoop loop) {
		ElementLocation location = (ElementLocation) loop.getProperty(ElementLocation.Part.WHOLE);

		if(loop.isEmpty()) {

			final String explanation = "For other than iteration porpuses, an empty loop is an unnecessary chunk of code! It serves only tyhe porpuse of filling the code base"
					+ "with non-relevant chunks.";

			EmptyBranch emptyCondition = new EmptyBranch(Category.EMPTY_LOOP, location, explanation, loop);

			Linter.getInstance().register(emptyCondition);
		}
		if(loop.getParent().isInLoop())
			Linter.getInstance().register(new Nesting(Category.NESTED_CODE, location, "Nested loops can lead to performance and readibility issues!"));

		return true;
	}
}
