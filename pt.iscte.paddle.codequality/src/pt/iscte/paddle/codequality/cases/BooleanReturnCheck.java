package pt.iscte.paddle.codequality.cases;

import java.util.Objects;

import pt.iscte.paddle.codequality.cases.BadCodeCase.Category;
import pt.iscte.paddle.javali.translator.ElementLocation;
import pt.iscte.paddle.model.IExpression;
import pt.iscte.paddle.model.IProgramElement;

public class BooleanReturnCheck extends BadCodeCase{
	
	private final IProgramElement faultyVerification;

	private BooleanReturnCheck(Category category, ElementLocation location, String explanation, IProgramElement verification) {
		super(category, location, explanation);
		this.faultyVerification = Objects.requireNonNull(verification);
	}
}
