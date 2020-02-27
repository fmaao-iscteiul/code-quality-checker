package pt.iscte.paddle.codequality.cases;

import java.util.List;
import java.util.Objects;

import pt.iscte.paddle.codequality.cases.BadCodeCase.Category;
import pt.iscte.paddle.javali.translator.ElementLocation;
import pt.iscte.paddle.model.IExpression;
import pt.iscte.paddle.model.cfg.INode;

public class UnreachableCode extends BadCodeCase {
	
	private final List<INode> deadNodes;

	public UnreachableCode(Category category, ElementLocation location, String explanation, List<INode> deadNodes) {
		super(category, location, explanation, null);
		
		this.deadNodes = deadNodes;
	}
	
	public List<INode> getDeadNodes() {
		return deadNodes;
	}
}
