package pt.iscte.paddle.linter.issues;

import java.util.List;

import pt.iscte.paddle.linter.cases.base.MultipleOccurrencesIssue;
import pt.iscte.paddle.linter.misc.Classification;
import pt.iscte.paddle.linter.misc.IssueType;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.model.IStatement;

public class Duplicate extends MultipleOccurrencesIssue {

	public Duplicate(IProcedure procedure, List<IProgramElement> occurrences) {
		super(IssueType.REDUNDANT_BRANCHES, Classification.SERIOUS, procedure, occurrences);
	}

	public IProgramElement getInstance() {
		return (IStatement) occurrences.get(0);
	}
	

	@Override
	public String getIssueTitle() {
		return "Duplicated code: " + occurrences.get(0);
	}
}
