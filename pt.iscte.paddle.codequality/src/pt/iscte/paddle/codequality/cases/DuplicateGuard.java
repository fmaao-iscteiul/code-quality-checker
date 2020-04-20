package pt.iscte.paddle.codequality.cases;

import java.util.List;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;

import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.codequality.misc.Classification;
import pt.iscte.paddle.codequality.misc.Compability;
import pt.iscte.paddle.codequality.misc.Explanations;
import pt.iscte.paddle.javardise.service.IClassWidget;
import pt.iscte.paddle.javardise.service.ICodeDecoration;
import pt.iscte.paddle.javardise.service.IJavardiseService;
import pt.iscte.paddle.javardise.service.IWidget;
import pt.iscte.paddle.javardise.util.HyperlinkedText;
import pt.iscte.paddle.model.IExpression;
import pt.iscte.paddle.model.IProgramElement;

public class DuplicateGuard extends BadCodeCase{
	
	List<IProgramElement> guards;

	public DuplicateGuard(List<IProgramElement> guards) {
		super(Category.DUPLICATE_SELECTION_GUARD, Classification.AVERAGE);
		this.guards = guards;
	}
	
	@Override
	public void generateComponent(IClassWidget widget, Composite comp, int style) {
		generateMark(widget, comp, style, guards);
		generateExplanation(widget, comp, style);
	}
	
	@Override
	protected void generateExplanation(IClassWidget widget, Composite comp, int style) {
		IWidget w = generateElementWidget(guards.get(0));
		ICodeDecoration<Text> d2 = w.addNote("Unnecessary double-check?", ICodeDecoration.Location.RIGHT);
		d2.show();
		getDecorations().add(d2);
		if(w != null) {
			Link link = new HyperlinkedText(null)
					.words("The condition ").link(guards.get(0).toString(), () -> {})
					.words(" was found duplicated inside the code block.")
					.words("\n\n - Neither of the variables used in the conditions had their values changed in between.")
					.words("\n - Double checking a condition which parts don't change in between checks, will have the same result.")
					.words("\n - Since the result is the same, it doesn't need to be duplicated. Second check should be removed.")
					.create(comp, SWT.WRAP | SWT.V_SCROLL);

			link.requestLayout();
		}
	}

}
