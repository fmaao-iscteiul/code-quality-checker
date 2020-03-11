package pt.iscte.paddle.codequality.cases;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.model.IProgramElement;

public class DuplicateGuard extends BadCodeCase{
	
	List<IProgramElement> guards;

	public DuplicateGuard(List<IProgramElement> guards) {
		super(Category.DUPLICATE_SELECTION_GUARD, "Duplicate Loop Guard.");
		this.guards = guards;
	}
	
	@Override
	public void generateComponent(Display display, Composite comp, int style) {
		guards.forEach(guard -> super.generateMark(display, comp, style, guard));
	}

}
