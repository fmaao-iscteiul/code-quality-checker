package pt.iscte.paddle.codequality.cases;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;

import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.codequality.misc.Classification;
import pt.iscte.paddle.javardise.service.IClassWidget;
import pt.iscte.paddle.javardise.service.ICodeDecoration;
import pt.iscte.paddle.javardise.service.IJavardiseService;
import pt.iscte.paddle.javardise.service.IWidget;
import pt.iscte.paddle.javardise.util.HyperlinkedText;
import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.model.ISelection;

public class BooleanReturnCheck extends BadCodeCase{

	public BooleanReturnCheck(IProgramElement selection) {
		super(Category.FAULTY_RETURN_BOOLEAN_CHECK, Classification.LIGHT, selection);
	}

	@Override
	protected void generateMark(IClassWidget widget, Composite comp, int style) {
		super.generateMark(widget, comp, style);
	}

	@Override
	protected void generateExplanation(IClassWidget widget, Composite comp, int style) {
		IWidget w = IJavardiseService.getWidget(element);
		ICodeDecoration<Text> t = w.addNote("Shouldn't you \n return the variable?", ICodeDecoration.Location.RIGHT);
		t.show();
		getDecorations().add(t);
		if(w != null) {
			Link link = new HyperlinkedText(null)
					.words("The ").link("if( "+((ISelection) element).getGuard().toString() + " )", ()->{}).words(" condition is unnecessary!\n ")
					.words("\n - There is no need to check the value of a boolean variable before returning true or false.")
					.words("\n - The condition itself already represents the value meant to be returned.")
					.create(comp, SWT.WRAP | SWT.SCROLL_LINE | SWT.V_SCROLL);

			link.requestLayout();
		}
	}
}
