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
import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.model.IStatement;

public class MagicNumber extends BadCodeCase{

	private List<IProgramElement> occurrences = new ArrayList<IProgramElement>();
	private IProgramElement magicNumber;

	public MagicNumber(String explanation, IProgramElement magicNumber) {
		super(Category.MAGIC_NUMBER, explanation);
		this.magicNumber = magicNumber;
		this.occurrences.add(magicNumber);
	}
	
	public IProgramElement getMagicNumber() {
		return magicNumber;
	}
	
	public List<IProgramElement> getOccurrences() {
		return occurrences;
	}
	
	public void addAssignment(IProgramElement statement) {
		this.occurrences.add(statement);
	}
	
	@Override
	public void generateComponent(Display display, org.eclipse.swt.widgets.Composite comp, int style) {
		occurrences.forEach(magicNumber -> {
			System.out.println(magicNumber);
			super.generateMark(display, comp, style, magicNumber);
		});
	}

}
