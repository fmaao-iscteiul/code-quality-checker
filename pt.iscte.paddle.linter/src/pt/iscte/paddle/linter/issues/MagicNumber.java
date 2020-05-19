package pt.iscte.paddle.linter.issues;


import java.util.List;
import pt.iscte.paddle.linter.cases.base.MultipleOccurrencesIssue;
import pt.iscte.paddle.linter.misc.Classification;
import pt.iscte.paddle.linter.misc.IssueType;
import pt.iscte.paddle.model.IProgramElement;

public class MagicNumber extends MultipleOccurrencesIssue {


	public MagicNumber(String explanation, IProgramElement magicNumber) {
		super(IssueType.MAGIC_NUMBER, Classification.AVERAGE);
		this.occurrences.add(magicNumber);
	}

	public List<IProgramElement> getOccurrences() {
		return occurrences;
	}

	public void addAssignment(IProgramElement statement) {
		this.occurrences.add(statement);
	}

}
