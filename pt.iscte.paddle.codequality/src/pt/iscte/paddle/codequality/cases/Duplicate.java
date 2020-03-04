package pt.iscte.paddle.codequality.cases;

import java.awt.Composite;
import java.util.List;

import org.eclipse.swt.widgets.Display;

import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.model.cfg.INode;

public class Duplicate extends BadCodeCase {
	
	List<INode> duplicates;

	public Duplicate(String explanation, List<INode> duplicates) {
		super(Category.DUPLICATE_CODE, explanation);
		this.duplicates = duplicates;
	}
	
	public List<INode> getDuplicates() {
		return duplicates;
	}

	@Override
	public void generateComponent(Display display, org.eclipse.swt.widgets.Composite comp, int style) {
		// TODO Auto-generated method stub
		
	}
}
