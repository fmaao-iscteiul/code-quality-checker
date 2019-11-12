package pt.iscte.paddle.codequality.cases;
import pt.iscte.paddle.javali.translator.ElementLocation;
import pt.iscte.paddle.model.IConditionalExpression;
import pt.iscte.paddle.model.ISelection;
import pt.iscte.paddle.model.IBlock.IVisitor;

public class EmptyExcepetion implements IVisitor{


	@Override
	public boolean visit(ISelection selection) {
		if(selection.getAlternativeBlock().isEmpty()) {
			ElementLocation location = (ElementLocation) selection.getProperty(ElementLocation.Part.WHOLE);
			System.out.println("You have an empty else on the line : " +location.getLine() 
			+ "\nConsider filling this block with a alternative that will happen when the condition above is not verified. If you find no use for this block, you can and should remove.");
		}
		return IVisitor.super.visit(selection);
	}
}
