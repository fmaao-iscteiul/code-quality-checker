package pt.iscte.paddle.linter.cases.base;

import java.util.ArrayList;
import java.util.List;
import pt.iscte.paddle.linter.misc.Classification;
import pt.iscte.paddle.linter.misc.IssueType;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IProgramElement;

public class MultipleOccurrencesIssue extends QualityIssue {

	protected List<IProgramElement> occurrences = new ArrayList<IProgramElement>();

	protected MultipleOccurrencesIssue(IssueType category, Classification classification, IProcedure procedure, IProgramElement... occ) {
		super(category, classification, procedure);
		for (IProgramElement occurence : occ) {
			this.occurrences.add(occurence);
		}
	}
	
	protected MultipleOccurrencesIssue(IssueType category, Classification classification, IProcedure procedure, List<IProgramElement> occ) {
		super(category, classification, procedure);
		for (IProgramElement occurence : occ) {
			this.occurrences.add(occurence);
		}
	}
	
	public void addOccurence(IProgramElement occurrence) {
		occurrences.add(occurrence);
	}

	public List<IProgramElement> getOccurrences() {
		return occurrences;
	}

}
