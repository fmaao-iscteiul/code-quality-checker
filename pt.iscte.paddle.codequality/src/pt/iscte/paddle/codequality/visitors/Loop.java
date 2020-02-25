package pt.iscte.paddle.codequality.visitors;
import pt.iscte.paddle.codequality.cases.BooleanCheck;
import pt.iscte.paddle.codequality.cases.EmptyBranch;
import pt.iscte.paddle.codequality.cases.BadCodeCase.Category;
import pt.iscte.paddle.codequality.linter.Linter;
import pt.iscte.paddle.codequality.misc.BranchVerifications;
import pt.iscte.paddle.javali.translator.ElementLocation;
import pt.iscte.paddle.model.IBinaryExpression;
import pt.iscte.paddle.model.IBinaryOperator;
import pt.iscte.paddle.model.IExpression;
import pt.iscte.paddle.model.ILoop;
import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.model.IOperator.OperationType;

public class Loop implements IVisitor{
	
	public static Loop build() {
		return new Loop();
	}

	@Override
	public boolean visit(ILoop loop) {
		if(loop.isEmpty()) {
			ElementLocation location = (ElementLocation) loop.getProperty(ElementLocation.Part.WHOLE);

			final String explanation = "For other than iteration porpuses, an empty loop is an unnecessary chunk of code! It serves only tyhe porpuse of filling the code base"
					+ "with non-relevant chunks.";

			EmptyBranch emptyCondition = new EmptyBranch(Category.EMPTY_LOOP, location, explanation, loop);

			Linter.getInstance().register(emptyCondition);
		}
		
		IExpression guard = loop.getGuard();
		if(guard.getOperationType().equals(OperationType.RELATIONAL)) {
			IBinaryExpression bExp = IBinaryExpression.create(IBinaryOperator.EQUAL, guard.getParts().get(0), guard.getParts().get(1));
			if(bExp.getLeftOperand().getType().isBoolean() && bExp.getRightOperand().getType().isBoolean()) {
				Linter.getInstance().register(new BooleanCheck(Category.FAULTY_BOOLEAN_CHECK, (ElementLocation) guard.getProperty(ElementLocation.Part.WHOLE), "", loop.getGuard()));
			}
		}
		else BranchVerifications.checkBranchParts(loop, guard);
		return true;
	}
}
