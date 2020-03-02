package pt.iscte.paddle.codequality.visitors;
import pt.iscte.paddle.codequality.linter.Linter;
import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.javali.translator.ElementLocation;
import pt.iscte.paddle.codequality.cases.BooleanReturnCheck;
import pt.iscte.paddle.model.IReturn;
import pt.iscte.paddle.model.ISelection;
import pt.iscte.paddle.model.IType;

import pt.iscte.paddle.model.IBlock.IVisitor;

public class Return implements IVisitor{

	public static Return build() {
		return new Return();
	}


	@Override
	public boolean visit(IReturn returnStatement) {
		if(returnStatement.getReturnValueType().equals(IType.BOOLEAN) 
				&& returnStatement.getParent().getParent() instanceof ISelection) {
			ISelection faultyVerification = (ISelection) returnStatement.getParent().getParent();
			Linter.getInstance().register(new BooleanReturnCheck(Category.FAULTY_BOOLEAN_CHECK, "", faultyVerification));
		}

		return true;
	}
}
