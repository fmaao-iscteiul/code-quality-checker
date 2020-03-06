package pt.iscte.paddle.codequality.cases;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.model.cfg.IBranchNode;
import pt.iscte.paddle.model.cfg.INode;

public class UnreachableCode extends BadCodeCase {

	private final List<INode> deadNodes;

	public UnreachableCode(String explanation, List<INode> deadNodes) {
		super(Category.DEAD_CODE, explanation, null);
		this.deadNodes = deadNodes;
	}

	public List<INode> getDeadNodes() {
		return deadNodes;
	}
	
	public void addDeadNode(INode deadNode) {
		this.deadNodes.add(deadNode);
	}

	@Override
	public void generateComponent(Display display, Composite comp, int style) {
		deadNodes.forEach(deadNode -> {
			super.generateMark(display, comp, style, deadNode.getElement());
		});
		this.generateExplanation(comp, style);
	}

}
