package pt.iscte.paddle.codequality.cases;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Link;

import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.javardise.service.IClassWidget;
import pt.iscte.paddle.javardise.util.HyperlinkedText;
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
	
	public Duplicate(Category category, String explanation, IProgramElement duplicate) {
		super(category, explanation);
		this.duplicates = new ArrayList<IProgramElement>();
		this.duplicates.add(duplicate);
	}

	public Duplicate(Category category, String explanation, Collection<INode> duplicatesList) {
		super(category, explanation);
		this.duplicates = new ArrayList<IProgramElement>();
		duplicatesList.forEach(node -> this.duplicates.add(node.getElement()));
	}

	@Override
	public void generateComponent(IClassWidget widget, org.eclipse.swt.widgets.Composite comp, int style) {
		this.generateMark(widget, comp, style, duplicates);
		this.generateExplanation(widget, comp, style);
	}
	
	@Override
	protected void generateExplanation(IClassWidget widget, Composite comp, int style) {
		Link link = new HyperlinkedText(null).words(getExplanation()).create(comp, SWT.WRAP | SWT.V_SCROLL);
		link.requestLayout();
	}

	public List<IProgramElement> getDuplicates() {
		return duplicates;
	}

	public void addAssignment(IStatement assignment) {
		this.duplicates.add(assignment);
	}

}
