package pt.iscte.paddle.codequality.cases;

import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Link;

import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.codequality.misc.Explanations;
import pt.iscte.paddle.javardise.service.IClassWidget;
import pt.iscte.paddle.model.IProgramElement;

public class DuplicateGuard extends BadCodeCase{
	
	List<IProgramElement> guards;

	public DuplicateGuard(List<IProgramElement> guards) {
		super(Category.DUPLICATE_SELECTION_GUARD, Explanations.DUPLICATE_SELECTION_GUARD);
		this.guards = guards;
	}
	
	@Override
	public void generateComponent(IClassWidget widget, Composite comp, int style) {
		super.generateMark(widget, comp, style, guards);
		super.generateExplanation(widget, comp, style);
	}

}
