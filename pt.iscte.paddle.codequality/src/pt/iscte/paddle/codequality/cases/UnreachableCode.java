package pt.iscte.paddle.codequality.cases;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.javardise.service.IClassWidget;
import pt.iscte.paddle.javardise.service.ICodeDecoration;
import pt.iscte.paddle.javardise.service.IJavardiseService;
import pt.iscte.paddle.javardise.service.IWidget;
import pt.iscte.paddle.model.IControlStructure;
import pt.iscte.paddle.model.IExpression;
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
	public void generateComponent(IClassWidget widget, Composite comp, int style) {
		IWidget w = IJavardiseService.getWidget(deadNodes.get(0).getElement());
		IWidget[] elements = new IWidget[deadNodes.size()];

		for (int i = 0; i < elements.length; i++) {
			IControlStructure s = deadNodes.get(i).getElement().getProperty(IControlStructure.class);
			if(s != null) {
				elements[i] = IJavardiseService.getWidget(s);
			}
			else elements[i] = IJavardiseService.getWidget(deadNodes.get(i).getElement());
		}
	
		Color cyan = Display.getDefault().getSystemColor(SWT.COLOR_CYAN);
		ICodeDecoration dec = w.addRegionMark(cyan, elements);
		ICodeDecoration note = w.addNote("Dwadwa", ICodeDecoration.Location.RIGHT);
		note.show();
		super.addDecoration(note);
		super.addDecoration(dec);
		

	}

}
