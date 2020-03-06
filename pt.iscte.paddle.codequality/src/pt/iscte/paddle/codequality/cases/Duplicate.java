package pt.iscte.paddle.codequality.cases;

import java.awt.Composite;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Display;

import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.model.IStatement;

public class Duplicate extends BadCodeCase {
	
	List<IStatement> duplicates;

	public Duplicate(String explanation, IStatement duplicate) {
		super(Category.DUPLICATE_CODE, explanation);
		this.duplicates = new ArrayList<IStatement>();
		this.duplicates.add(duplicate);
	}
	
	@Override
	public void generateComponent(Display display, org.eclipse.swt.widgets.Composite comp, int style) {
		duplicates.forEach(duplicate -> {
			System.out.println(duplicate);
			super.generateMark(display, comp, style, duplicate);
		});
	}
	
	public List<IStatement> getDuplicates() {
		return duplicates;
	}
	
	public void addAssignment(IStatement assignment) {
		this.duplicates.add(assignment);
	}

}
