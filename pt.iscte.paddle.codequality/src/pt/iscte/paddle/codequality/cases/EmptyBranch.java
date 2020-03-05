package pt.iscte.paddle.codequality.cases;

import java.awt.Composite;

import org.eclipse.swt.widgets.Display;

import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.model.IControlStructure;

public class EmptyBranch extends BadCodeCase{

	private final IControlStructure branch;

	public EmptyBranch(Category category, String explanation, IControlStructure branch) {
		super(category, explanation, branch);
		this.branch = branch;
	}
	
	public IControlStructure getBranch() {
		return branch;
	}
}
