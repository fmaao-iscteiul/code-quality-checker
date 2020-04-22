package pt.iscte.paddle.codequality.cases;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;

import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.codequality.misc.Classification;
import pt.iscte.paddle.javardise.service.IClassWidget;
import pt.iscte.paddle.javardise.service.IJavardiseService;
import pt.iscte.paddle.javardise.service.IWidget;
import pt.iscte.paddle.javardise.util.HyperlinkedText;
import pt.iscte.paddle.model.IProgramElement;

public class UselessVariableAssignment extends BadCodeCase {

	public UselessVariableAssignment(IProgramElement element) {
		super(Category.USELESS_CODE, Classification.SERIOUS, element);
	}
	
	
	@Override
	protected void generateExplanation(IClassWidget widget, Composite comp, int style) {
		IWidget w = IJavardiseService.getWidget(super.element);
		if(w != null) {
			Link link = new HyperlinkedText(null)
					.words("Issue: \n\n")
					.words("The highlighted assignment's ").link(element.toString(), () -> {
					})
					.words(" value was not used. ")
					.words("\n\n - Variables don't need to be initialized before being assigned with a value. ")
					.words("\n - The value was not used before the second assignment, which means that is as no impact. ")
					.words("\n - Unused values are useless to the program.")
					.words("\n\n Suggestion: \n\n- Remove this useless assignment.")
					.create(comp, SWT.WRAP | SWT.V_SCROLL);

			link.requestLayout();
		}
	}

}
