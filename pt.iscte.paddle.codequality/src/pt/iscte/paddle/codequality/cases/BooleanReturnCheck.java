package pt.iscte.paddle.codequality.cases;

import pt.iscte.paddle.model.IControlStructure;

import java.awt.Composite;

import org.eclipse.swt.widgets.Display;

import pt.iscte.paddle.codequality.misc.Category;

public class BooleanReturnCheck extends BadCodeCase{
	

	public BooleanReturnCheck(Category category, String explanation, IControlStructure selection) {
		super(category, explanation, selection);
	}

}
