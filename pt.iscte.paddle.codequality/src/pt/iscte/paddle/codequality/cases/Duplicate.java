package pt.iscte.paddle.codequality.cases;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.widgets.Display;

import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.model.IStatement;
import pt.iscte.paddle.model.cfg.INode;

public class Duplicate extends BadCodeCase {

	List<IProgramElement> duplicates;

	public Duplicate(String explanation, IProgramElement duplicate) {
		super(Category.DUPLICATE_CODE, explanation);
		this.duplicates = new ArrayList<IProgramElement>();
		this.duplicates.add(duplicate);
	}

	public Duplicate(String explanation, Set<INode> duplicatesList) { 
		super(Category.DUPLICATE_CODE, explanation);
		this.duplicates = new ArrayList<IProgramElement>();
		duplicatesList.forEach(node -> this.duplicates.add(node.getElement()));

	}

	@Override
	public void generateComponent(Display display, org.eclipse.swt.widgets.Composite comp, int style) {
		super.generateMark(display, comp, style, duplicates);
	}

	public List<IProgramElement> getDuplicates() {
		return duplicates;
	}

	public void addAssignment(IStatement assignment) {
		this.duplicates.add(assignment);
	}

}
