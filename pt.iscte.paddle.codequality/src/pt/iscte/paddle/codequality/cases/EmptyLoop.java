package pt.iscte.paddle.codequality.cases;

import pt.iscte.paddle.javali.translator.ElementLocation;
import pt.iscte.paddle.model.ILoop;
import pt.iscte.paddle.model.IBlock.IVisitor;

public class EmptyLoop implements IVisitor{
	
	@Override
	public boolean visit(ILoop loop) {
		System.out.println(loop.getParent());
		if(loop.getBlock().isEmpty()) {
			ElementLocation location = (ElementLocation) loop.getProperty(ElementLocation.Part.WHOLE);
			System.out.println("You have an empty Loop on the line : " +location.getLine() 
			+ "\nAn empty loop represents a number of iterations that will do nothing.These will use resources unnecessarily. Consider adding code to the loop, or removing it.\n");
		}
		return IVisitor.super.visit(loop);
	}

}
