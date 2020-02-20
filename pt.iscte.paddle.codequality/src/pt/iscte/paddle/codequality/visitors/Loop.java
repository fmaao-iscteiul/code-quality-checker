package pt.iscte.paddle.codequality.visitors;
import pt.iscte.paddle.codequality.cases.EmptyBranch;
import pt.iscte.paddle.codequality.cases.BadCodeCase.Category;
import pt.iscte.paddle.codequality.linter.Linter;
import pt.iscte.paddle.javali.translator.ElementLocation;
import pt.iscte.paddle.model.ILoop;
import pt.iscte.paddle.model.IBlock.IVisitor;

public class Loop implements IVisitor{
	
	public static Loop build() {
		return new Loop();
	}

	@Override
	public boolean visit(ILoop loop) {
		if(loop.isEmpty()) {
			ElementLocation location = (ElementLocation) loop.getProperty(ElementLocation.Part.WHOLE);

			final String explanation = "You have an empty If on the line : " +location.getLine() 
			+ "\nInstead of using an if to verify a condition and only writing code in the else statement because we want to target the negative outcome of such condition, "
			+ "the right way to go is by placing the operator ‘!’ behind the condition, like ( ! YOUR_CONDITION ). "
			+ "By doing this, you are targeting the desired condition right away, and there is no need to leave empty code blocks,  "
			+ "like an if statement.";

			EmptyBranch emptyCondition = new EmptyBranch(Category.EMPTY_LOOP, location, explanation, loop);

			Linter.getInstance().register(emptyCondition);
		}
		return true;
	}
}
