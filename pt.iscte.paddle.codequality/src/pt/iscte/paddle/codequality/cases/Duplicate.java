package pt.iscte.paddle.codequality.cases;

import java.util.List;

import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.model.cfg.INode;

public class Duplicate extends BadCodeCase {
	
	List<INode> duplicates;

	public Duplicate(String explanation, List<INode> duplicates) {
		super(Category.DUPLICATE_CODE, explanation);
		this.duplicates = duplicates;
	}
	
	public List<INode> getDuplicates() {
		return duplicates;
	}
}
