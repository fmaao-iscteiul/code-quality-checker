package pt.iscte.paddle.codequality.visitors;
import pt.iscte.paddle.model.IReturn;


import pt.iscte.paddle.model.IBlock.IVisitor;

public class Return implements IVisitor{
	
	public static Return build() {
		return new Return();
	}
	
	
	@Override
	public boolean visit(IReturn returnStatement) {
		switch (returnStatement.getReturnValueType().toString()) {
		case "boolean":
//			System.out.println(returnStatement.getParent().getParent().toString().matches("if\\W+\\w+[)]"));
			break;
		default:
//			System.out.println("Default.");

		}
		return true;
	}
}
