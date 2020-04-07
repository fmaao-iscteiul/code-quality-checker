package pt.iscte.paddle.codequality.cases;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;

import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.javardise.service.IClassWidget;
import pt.iscte.paddle.javardise.service.ICodeDecoration;
import pt.iscte.paddle.javardise.service.IJavardiseService;
import pt.iscte.paddle.javardise.service.IWidget;
import pt.iscte.paddle.javardise.util.HyperlinkedText;
import pt.iscte.paddle.model.IProgramElement;

public class BooleanReturnCheck extends BadCodeCase{

	public BooleanReturnCheck(IProgramElement selection) {
		super(Category.FAULTY_RETURN_BOOLEAN_CHECK, "", selection);
	}

	@Override
	protected void generateMark(IClassWidget widget, Composite comp, int style) {
		super.generateMark(widget, comp, style);
	}

	@Override
	protected void generateExplanation(IClassWidget widget, Composite comp, int style) {
		IWidget w = IJavardiseService.getWidget(element);
		if(w != null) {
			Link link = new HyperlinkedText(null)
					.words("There is an unnecessary condition: \n\n ")
					.link(element.toString(), () -> {
						ICodeDecoration<Text> t = w.addNote("Aren't you complicating?", ICodeDecoration.Location.RIGHT);
						t.show();
						getDecorations().add(t);
					})
					.words("\n There is no need to check the value of a boolean variable"
							+ " before returning true or false.")
					.create(comp, SWT.WRAP | SWT.V_SCROLL);

			link.requestLayout();
		}
	}
}
