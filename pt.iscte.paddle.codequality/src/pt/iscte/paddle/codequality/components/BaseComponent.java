package pt.iscte.paddle.codequality.components;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Composite;

import pt.iscte.paddle.codequality.cases.base.QualityIssue;
import pt.iscte.paddle.model.IProgramElement;

public abstract class BaseComponent {
	
	private List<IProgramElement> elements;
	private Composite comp;
	private QualityIssue badCase;
	
	public BaseComponent(Composite composite, QualityIssue badCase, int style) {
		this.comp = new Composite(composite, style);
		this.badCase = badCase;
		this.elements = new ArrayList<IProgramElement>();
	}
	
	public QualityIssue getBadCase() {
		return badCase;
	}
	
	public List<IProgramElement> getElements() {
		return elements;
	}
	
	public Composite getComp() {
		return comp;
	}
	
}
