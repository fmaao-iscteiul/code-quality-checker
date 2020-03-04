package pt.iscte.paddle.codequality.cases;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.javardise.Decoration;
import pt.iscte.paddle.javardise.MarkerService;
import pt.iscte.paddle.javardise.MarkerService.Mark;
import pt.iscte.paddle.model.IStatement;

public class MagicNumber extends BadCodeCase{

	List<IStatement> assignments;

	public MagicNumber(String explanation, IStatement magicNumber) {
		super(Category.FAULTY_ASSIGNMENT, explanation);
		this.assignments = new ArrayList<IStatement>();
		this.assignments.add(magicNumber);
	}
	
	public List<IStatement> getAssignments() {
		return assignments;
	}
	
	public void addAssignment(IStatement statement) {
		this.assignments.add(statement);
	}

	@Override
	public void generateComponent(Display display, Composite comp, int style) {
		this.assignments.forEach(assignment -> {
			Mark mark = MarkerService.mark(new Color (display, 255, 0, 0), assignment);
			Decoration d = MarkerService.addDecoration(assignment, "Magic Number", Decoration.Location.RIGHT);
		});
		
	}

}
