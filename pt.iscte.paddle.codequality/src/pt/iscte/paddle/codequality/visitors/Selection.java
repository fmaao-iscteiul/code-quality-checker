package pt.iscte.paddle.codequality.visitors;
import pt.iscte.paddle.codequality.cases.BadCodeCase.CaseType;
import pt.iscte.paddle.codequality.cases.EmptySelection;
import pt.iscte.paddle.codequality.cases.EmptySelection.Builder;
import pt.iscte.paddle.codequality.linter.Linter;
import pt.iscte.paddle.javali.translator.ElementLocation;
import pt.iscte.paddle.model.ISelection;
import pt.iscte.paddle.model.IBlock.IVisitor;

public class Selection implements IVisitor {
	
	public static Selection buildSelectionVisitor() {
		return new Selection();
	}
	
	private Selection() {}
	
	@Override
	public boolean visit(ISelection selection) {
		if(selection.getBlock().isEmpty()) {

			ElementLocation location = (ElementLocation) selection.getProperty(ElementLocation.Part.WHOLE);

			final String explanation = "You have an empty If on the line : " +location.getLine() 
			+ "\nInstead of using an if to verify a condition and only writing code in the else statement because we want to target the negative outcome of such condition, "
			+ "the right way to go is by placing the operator ‘!’ behind the condition, like ( ! YOUR_CONDITION ). "
			+ "By doing this, you are targeting the desired condition right away, and there is no need to leave empty code blocks,  "
			+ "like an if statement.";

			EmptySelection emptyCondition = new EmptySelection.Builder(selection.getGuard())
			.addType(CaseType.EMPTY_SELECTION)
			.setExplanation(explanation)
			.setLocation(location).build();
			
			Linter.getInstance().registerCatchedCase(emptyCondition);
		}
		return true;
	}
	
	
	@Override
	public boolean visitAlternative(ISelection selection) {
		ElementLocation location = (ElementLocation) selection.getProperty(ElementLocation.Part.WHOLE);

		final String explanation = "Bla Bla Bla";

		EmptySelection emptyCondition = new EmptySelection.Builder(selection.getGuard())
		.addType(CaseType.EMPTY_ALTERNATIVE	)
		.setExplanation(explanation)
		.setLocation(location).build();
		
		Linter.getInstance().registerCatchedCase(emptyCondition);
		return true;
	}
	
	

}
