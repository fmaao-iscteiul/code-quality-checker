package pt.iscte.paddle.codequality.cases;

import java.util.Objects;

import pt.iscte.paddle.codequality.cases.BooleanCheck.Builder;
import pt.iscte.paddle.model.IExpression;
import pt.iscte.paddle.model.IProgramElement;

public class BooleanReturnCheck extends BadCodeCase{

	private BooleanReturnCheck(Builder builder) {
		super(builder);
	}

	public static class Builder extends BadCodeCase.Builder<Builder> {
		private final IProgramElement faultyVerification;


		public Builder(IProgramElement faultyVerification) {
			this.faultyVerification = Objects.requireNonNull(faultyVerification);
		}


		@Override
		public BooleanReturnCheck build() {
			return new BooleanReturnCheck(this);
		}


		@Override
		protected Builder self() {
			return this;
		}
	}

}
