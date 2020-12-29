package pt.iscte.paddle.linter.issues;

import java.util.List;
import pt.iscte.paddle.linter.cases.base.MultipleOccurrencesIssue;
import pt.iscte.paddle.linter.misc.Classification;
import pt.iscte.paddle.linter.misc.IssueType;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IProgramElement;

public class DuplicateMethodCall extends MultipleOccurrencesIssue {
	

	public DuplicateMethodCall(IProcedure procedure, List<IProgramElement> duplicatesList) {	
		super(IssueType.DUPLICATE_SELECTION_GUARD, Classification.SERIOUS, procedure, duplicatesList);
	}

}
