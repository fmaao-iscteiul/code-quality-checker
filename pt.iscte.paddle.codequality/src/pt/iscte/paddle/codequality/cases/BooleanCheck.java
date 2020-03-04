package pt.iscte.paddle.codequality.cases;

import java.awt.Composite;
import java.util.Objects;

import org.eclipse.swt.widgets.Display;

import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.model.IExpression;

public class BooleanCheck extends BadCodeCase {

	private IExpression expression;
	
	public BooleanCheck(String explanation, IExpression selectionGuard) {
		super(Category.FAULTY_BOOLEAN_CHECK, explanation, selectionGuard);
		this.expression = Objects.requireNonNull(selectionGuard);
	}
	
	public IExpression getExpression() {
		return expression;
	}

	@Override
	public void generateComponent(Display display, org.eclipse.swt.widgets.Composite comp, int style) {
		// TODO Auto-generated method stub
		
	}
}
