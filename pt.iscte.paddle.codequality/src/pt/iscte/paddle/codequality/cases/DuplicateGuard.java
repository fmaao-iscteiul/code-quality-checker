package pt.iscte.paddle.codequality.cases;

import java.util.List;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;

import pt.iscte.paddle.codequality.misc.Category;
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
		super(Category.DUPLICATE_SELECTION_GUARD, Explanations.DUPLICATE_SELECTION_GUARD);
		this.guards = guards;
	}
	
	@Override
	public void generateComponent(IClassWidget widget, Composite comp, int style) {
		generateMark(widget, comp, style, guards);
		generateExplanation(widget, comp, style);
	}
	
	@Override
	protected void generateExplanation(IClassWidget widget, Composite comp, int style) {
		IWidget w = IJavardiseService.getWidget(guards.get(0));
		Set<IExpression> vars = Compability.extractVariables(((IExpression) guards.get(0)).getParts());
		System.out.println(vars);
		if(w != null) {
			Link link = new HyperlinkedText(null)
					.words("The condition ").link(guards.get(0).toString(), () -> {
						ICodeDecoration<Text> d2 = w.addNote("Does this need \n to be duplicated?", ICodeDecoration.Location.RIGHT);
						d2.show();
						getDecorations().add(d2);
					})
					.words(" was found duplicated inside the code block.")
					.words("\n\n - Neither of the variables used in the conditions had their values changed in between.")
					.words("\n - Double checking a condition which parts don't change in between checks, is not necessary.")
					.create(comp, SWT.WRAP | SWT.V_SCROLL);

			link.requestLayout();
		}
	}

}
