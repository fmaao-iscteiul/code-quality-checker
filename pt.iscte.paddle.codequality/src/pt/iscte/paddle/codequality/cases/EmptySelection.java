package pt.iscte.paddle.codequality.cases;

import java.util.Objects;
import pt.iscte.paddle.model.IExpression;

public class EmptySelection extends BadCodeCase{

	private final IExpression guard;

	public static class Builder extends BadCodeCase.Builder<Builder> {
		private final IExpression guard;


		public Builder(IExpression selectionGuard) {
			this.guard = Objects.requireNonNull(selectionGuard);
		}


		@Override
		public EmptySelection build() {
			return new EmptySelection(this);
		}


		@Override
		protected Builder self() {
			return this;
		}
	}

	private EmptySelection(Builder builder) {
		super(builder);
		this.guard = builder.guard;
		super.explanation = builder.explanation;
		super.blockLocation = builder.blockLocation;
	}
	
	public IExpression getGuard() {
		return guard;
	}
}
