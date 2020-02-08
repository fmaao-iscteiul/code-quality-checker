package pt.iscte.paddle.codequality.cases;

import java.util.Objects;

import pt.iscte.paddle.codequality.cases.EmptyBranch.Builder;
import pt.iscte.paddle.model.IExpression;

public class BooleanCheck extends BadCodeCase {
	
	public static class Builder extends BadCodeCase.Builder<Builder> {
		private final IExpression condition;


		public Builder(IExpression selectionGuard) {
			this.condition = Objects.requireNonNull(selectionGuard);
		}


		@Override
		public BooleanCheck build() {
			return new BooleanCheck(this);
		}


		@Override
		protected Builder self() {
			return this;
		}
	}

	private BooleanCheck(Builder builder) {
		super(builder);
	}

}
