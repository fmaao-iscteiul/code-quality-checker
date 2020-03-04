package pt.iscte.paddle.codequality.components;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Composite;

import pt.iscte.paddle.codequality.cases.BadCodeCase;
import pt.iscte.paddle.model.IProgramElement;

public abstract class BaseComponent {
	
	private List<IProgramElement> elements;
	private Composite comp;
	private BadCodeCase badCase;
	
	public BaseComponent(Composite composite, BadCodeCase badCase, int style) {
		this.comp = new Composite(composite, style);
		this.badCase = badCase;
		this.elements = new ArrayList<IProgramElement>();
	}
	
	public BadCodeCase getBadCase() {
		return badCase;
	}
	
	public List<IProgramElement> getElements() {
		return elements;
	}
	
	public Composite getComp() {
		return comp;
	}
}
