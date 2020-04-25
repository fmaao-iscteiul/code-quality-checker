package pt.iscte.paddle.codequality.cases;

import java.awt.Composite;
import java.util.Objects;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;

import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.codequality.misc.Classification;
import pt.iscte.paddle.javardise.service.IClassWidget;
import pt.iscte.paddle.javardise.service.ICodeDecoration;
import pt.iscte.paddle.javardise.service.IJavardiseService;
import pt.iscte.paddle.javardise.service.IWidget;
import pt.iscte.paddle.javardise.util.HyperlinkedText;
import pt.iscte.paddle.model.IExpression;

public class BooleanCheck extends BadCodeCase {

	private IExpression expression;

	public BooleanCheck(String explanation, IExpression selectionGuard) {
		super(Category.FAULTY_BOOLEAN_CHECK, Classification.LIGHT, selectionGuard);
		this.expression = Objects.requireNonNull(selectionGuard);
	}

	public IExpression getExpression() {
		return expression;
	}

	@Override
	protected void generateMark(IClassWidget widget, org.eclipse.swt.widgets.Composite comp, int style) {
		IWidget w = generateElementWidget(element);
		if(w != null) {
			ICodeDecoration<Canvas> d = w.addMark(getColor());
			d.show();
			getDecorations().add(d);
			ICodeDecoration<Text> d2 = w.addNote("Maybe use \n the ! operator?", ICodeDecoration.Location.RIGHT);
			d2.show();
			getDecorations().add(d2);
		}
	}

	@Override
	protected void generateExplanation(IClassWidget widget, org.eclipse.swt.widgets.Composite comp, int style) {
		IWidget w = IJavardiseService.getWidget(element);
		if(w != null) {
			Link link = new HyperlinkedText(null)
					.words("Issue:\n\n")
					.words("The condition ")
					.link(element.toString(), () -> {}) 
					.words(" could make use of the '!' operator. \n")	
					.words("\n - It represents the comparision between a boolean variable and one of it's binary possible values (true or false).")
					.words("\n - It is more of a styling mather, but this can negatively affect the code readibility, due to the condition's length.")
					.words("\n\nSuggestion:\nTry using the negation (!) operator in order to improve readibility.")
					.create(comp, SWT.WRAP | SWT.V_SCROLL);

			link.requestLayout();
		}
	}

}
