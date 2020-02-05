package pt.iscte.paddle.codequality.cases;

import java.util.Objects;

import pt.iscte.paddle.javali.translator.ElementLocation;
import pt.iscte.paddle.model.IConditionalExpression;
import pt.iscte.paddle.model.IExpression;
import pt.iscte.paddle.model.ISelection;
import pt.iscte.paddle.model.IBlock.IVisitor;

public class EmptyCondition extends BadCodeCase implements IVisitor{

	private final IExpression guard;

	public static class Builder extends BadCodeCase.Builder<Builder> {
		private final IExpression guard;


		public Builder(IExpression selectionGuard) {
			this.guard = Objects.requireNonNull(selectionGuard);
		}


		@Override
		BadCodeCase build() {
			return new EmptyCondition(this);
		}


		@Override
		protected Builder self() {
			return this;
		}
	}

	private EmptyCondition(Builder builder) {
		super(builder);
		this.guard = builder.guard;
	}

	@Override
	public boolean visit(ISelection selection) {

		if(selection.getBlock().isEmpty()) {

			ElementLocation location = (ElementLocation) selection.getProperty(ElementLocation.Part.WHOLE);

			final String explanation = "You have an empty If on the line : " +location.getLine() 
			+ "\nInstead of using an if to verify a condition and only writing code in the else statement because we want to target the negative outcome of such condition, "
			+ "the right way to go is by placing the operator ‘!’ behind the condition, like ( ! YOUR_CONDITION ). "
			+ "By doing this, you are targeting the desired condition right away, and there is no need to leave empty code blocks,  "
			+ "like an if statement.";

			new EmptyCondition.Builder(selection.getGuard())
			.addType(CaseType.EMPTY_SELECTION)
			.setExplanation(explanation)
			.setLocation(location);
		}
		return true;
	}
}
