package pt.iscte.paddle.codequality.cases;
import pt.iscte.paddle.model.ILoop;
import pt.iscte.paddle.model.IBlock.IVisitor;

public class Loop implements IVisitor{
	
	@Override
	public boolean visit(ILoop loop) {
		System.out.println("this is the loop:\n" + loop.getBlock());
		System.out.println(loop.getGuard());
		return IVisitor.super.visit(loop);
	}

}
