package pt.iscte.paddle.codequality.cases;

import java.util.List;
import java.util.Objects;

import pt.iscte.paddle.codequality.cases.EmptyBranch.Builder;
import pt.iscte.paddle.model.IExpression;
import pt.iscte.paddle.model.cfg.INode;

public class UnreachableCode extends BadCodeCase{
	
	public static class Builder extends BadCodeCase.Builder<Builder> {
		private final List<INode> deadNodes;


		public Builder(List<INode> deadNodes) {
			this.deadNodes = Objects.requireNonNull(deadNodes);
		}


		@Override
		public UnreachableCode build() {
			return new UnreachableCode(this);
		}


		@Override
		protected Builder self() {
			return this;
		}
	}

	private UnreachableCode(Builder builder) {
		super(builder);
	}
}
