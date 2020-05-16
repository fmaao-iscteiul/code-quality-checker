package pt.iscte.paddle.codequality.linter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pt.iscte.paddle.codequality.cases.base.QualityIssue;
import pt.iscte.paddle.codequality.misc.IssueType;

public class LintingResult {

	private int numberOfIssues;
	Map<IssueType, Integer> results;

	public LintingResult(List<QualityIssue> issues) {
		this.numberOfIssues = issues.size();

		this.results = new HashMap<IssueType, Integer>();
		issues.forEach(issue -> {
			if(results.containsKey(issue.getIssueType())) 
				results.replace(issue.getIssueType(), results.get(issue.getIssueType()) + 1);
			else results.put(issue.getIssueType(), 1);
		});
	}

	@Override
	public String toString() {

		String resultsText =  "------------------- RESULTS -------------------\n\nTOTAL NUMBER OF ISSUES FOUND WITHIN THE PROVIDED MODULES IS: " 
				+ this.numberOfIssues +
				"\n   THE CATEGORIES WERE: \n";
		Iterator it = results.entrySet().iterator();
		while ( it.hasNext()) {
			Map.Entry issue = (Map.Entry) it.next();
			resultsText = resultsText.concat("      " + issue.getKey() + " - " + issue.getValue()+ " TIMES.\n");
		}
		resultsText = resultsText.concat("\n-----------------------------------------------");

		return resultsText;
	}

}
