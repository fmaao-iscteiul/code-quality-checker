package pt.iscte.paddle.codequality.visitors;
import pt.iscte.paddle.codequality.cases.BadCodeCase.Category;
import pt.iscte.paddle.codequality.linter.Linter;
import pt.iscte.paddle.javali.translator.ElementLocation;
import pt.iscte.paddle.codequality.cases.BooleanReturnCheck;
import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.model.IReturn;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.iscte.paddle.model.IBlock.IVisitor;

public class Return implements IVisitor{
	
	public static Return build() {
		return new Return();
	}
	
	
	@Override
	public boolean visit(IReturn returnStatement) {
		ElementLocation location = (ElementLocation) returnStatement.getProperty(ElementLocation.Part.WHOLE);
		switch (returnStatement.getReturnValueType().toString()) {
		case "boolean":
			IProgramElement faultyVerification = returnStatement.getParent().getParent();
			Pattern p = Pattern.compile("if\\W+\\w+[)]", Pattern.CASE_INSENSITIVE);
			Matcher m = p.matcher(faultyVerification.toString());
			if(m.find()) {
				Linter.getInstance()
				.register(new BooleanReturnCheck.Builder(faultyVerification)
				.addCategory(Category.FAULTY_BOOLEAN_CHECK)
				.setBlock(returnStatement.getParent())
				.setLocation(location)
				.build());
			}
			break;
		default:
//			System.out.println("Default.");

		}
		return true;
	}
}
