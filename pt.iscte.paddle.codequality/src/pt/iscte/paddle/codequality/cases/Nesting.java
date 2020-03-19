
package pt.iscte.paddle.codequality.cases;

import java.awt.Composite;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Link;

import pt.iscte.paddle.codequality.misc.Category;

public class Nesting extends BadCodeCase{

	public Nesting(Category category, String explanation) {
		super(category, explanation, null);
	}

	@Override
	public void generateComponent(Display display, org.eclipse.swt.widgets.Composite comp, Link textWidget, int style) {
		// TODO Auto-generated method stub
		
	}

}
