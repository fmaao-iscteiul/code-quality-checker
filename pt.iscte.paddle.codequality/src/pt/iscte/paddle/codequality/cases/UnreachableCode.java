package pt.iscte.paddle.codequality.cases;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.javardise.service.ICodeDecoration;
import pt.iscte.paddle.javardise.service.IJavardiseService;
import pt.iscte.paddle.javardise.service.IWidget;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.cfg.INode;

public class UnreachableCode extends BadCodeCase {

	private final List<INode> deadNodes;

	public UnreachableCode(String explanation, List<INode> deadNodes) {
		super(Category.UNREACHABLE_CODE, explanation, null);
		this.deadNodes = deadNodes;
	}

	public List<INode> getDeadNodes() {
		return deadNodes;
	}

	@Override
	public void generateComponent(Display display, Composite comp, int style) {
		IWidget w = IJavardiseService.getWidget(deadNodes.get(0).getElement());
		IWidget[] elements = new IWidget[deadNodes.size()];
		
		for (int i = 0; i < elements.length; i++) {
			elements[i] = IJavardiseService.getWidget(deadNodes.get(i).getElement());
			
		}
		
		Color cyan = Display.getDefault().getSystemColor(SWT.COLOR_CYAN);
		ICodeDecoration dec = w.addRegionMark(cyan, elements);
		super.addDecoration(dec);
		dec.show();
		
	}

}
