package pt.iscte.paddle.codequality.cases;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.model.IControlStructure;
import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.model.ISelection;

public class EmptySelection extends EmptyBranch {
	
	public EmptySelection(String explanation, ISelection branch) {
		super(Category.EMPTY_SELECTION, explanation, branch);
	}

}
