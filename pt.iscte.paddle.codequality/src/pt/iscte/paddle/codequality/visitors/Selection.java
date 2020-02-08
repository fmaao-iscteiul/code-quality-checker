package pt.iscte.paddle.codequality.visitors;
import pt.iscte.paddle.codequality.cases.BooleanCheck;
import pt.iscte.paddle.codequality.cases.EmptyBranch;
import pt.iscte.paddle.codequality.cases.BadCodeCase.Category;
import pt.iscte.paddle.codequality.linter.Linter;
import pt.iscte.paddle.javali.translator.ElementLocation;
import pt.iscte.paddle.model.ISelection;
import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.model.IExpression;

public class Selection implements IVisitor {

	public static Selection build() {
		return new Selection();
	}

	private Selection() {}

	@Override
	public boolean visit(ISelection selection) {
	
		ElementLocation location = (ElementLocation) selection.getProperty(ElementLocation.Part.WHOLE);
		IExpression guard = selection.getGuard();
		
		if(selection.getGuard().toString().matches("\\W+\\w+\\s*==\\s*[false|true]*")) {
			Linter.getInstance()
			.register(
					new BooleanCheck
					.Builder(guard)
					.addCategory(Category.FAULTY_BOOLEAN_CHECK)
					.setLocation((ElementLocation) guard.getProperty(ElementLocation.Part.WHOLE))
					.build());
		}
			
		if(selection.getBlock().isEmpty()) {

			final String explanation = "You have an empty If on the line : " +location.getLine() 
			+ "\nInstead of using an if to verify a condition and only writing code in the else statement because we want to target the negative outcome of such condition, "
			+ "the right way to go is by placing the operator ‘!’ behind the condition, like ( ! YOUR_CONDITION ). "
			+ "By doing this, you are targeting the desired condition right away, and there is no need to leave empty code blocks,  "
			+ "like an if statement.";

			EmptyBranch emptyCondition = new EmptyBranch.Builder(guard)
					.addCategory(Category.EMPTY_SELECTION)
					.setExplanation(explanation)
					.setLocation(location).build();

			Linter.getInstance().register(emptyCondition);
		}
		return true;
	}


	@Override
	public boolean visitAlternative(ISelection selection) {
		ElementLocation location = (ElementLocation) selection.getProperty(ElementLocation.Part.WHOLE);

		final String explanation = "Bla Bla Bla";

		EmptyBranch emptyCondition = new EmptyBranch.Builder(selection.getGuard())
				.addCategory(Category.EMPTY_ALTERNATIVE	)
				.setExplanation(explanation)
				.setLocation(location).build();

		Linter.getInstance().register(emptyCondition);
		return true;
	}



}
