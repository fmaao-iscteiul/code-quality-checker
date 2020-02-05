package pt.iscte.paddle.codequality.cases;

import java.util.Objects;
import pt.iscte.paddle.model.IExpression;

public class EmptyCondition extends BadCodeCase{

	private final IExpression guard;

	public static class Builder extends BadCodeCase.Builder<Builder> {
		private final IExpression guard;


		public Builder(IExpression selectionGuard) {
			this.guard = Objects.requireNonNull(selectionGuard);
		}


		@Override
		public EmptyCondition build() {
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
		super.explanation = builder.explanation;
		super.blockLocation = builder.blockLocation;
	}
}
