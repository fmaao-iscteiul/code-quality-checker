package pt.iscte.paddle.codequality.cases;

import java.util.List;
import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.model.cfg.INode;

public class UnreachableCode extends BadCodeCase {
	
	private final List<INode> deadNodes;

	public UnreachableCode(Category category, String explanation, List<INode> deadNodes) {
		super(category, explanation, null);
		
		this.deadNodes = deadNodes;
	}
	
	public List<INode> getDeadNodes() {
		return deadNodes;
	}
}
