package pt.iscte.paddle.codequality.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

public class EmptySelection extends BaseComponent {

	public EmptySelection(Composite composite) {
		super(composite, SWT.BORDER);
		
		composite.setLayout(new FillLayout());
	}
}
