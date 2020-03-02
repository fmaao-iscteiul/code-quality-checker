package pt.iscte.paddle.codequality.components;

import org.eclipse.swt.widgets.Composite;

public abstract class BaseComponent {
	
	private Composite comp;
	
	public BaseComponent(Composite composite, int style) {
		this.comp = new Composite(composite, style);
	}
}
