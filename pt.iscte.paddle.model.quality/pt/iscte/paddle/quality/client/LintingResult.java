package pt.iscte.paddle.quality.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pt.iscte.paddle.model.javaparser.SourceLocation;
import pt.iscte.paddle.quality.cases.base.MultipleOccurrencesIssue;
import pt.iscte.paddle.quality.cases.base.QualityIssue;
import pt.iscte.paddle.quality.cases.base.SingleOcurrenceIssue;
import pt.iscte.paddle.quality.misc.IssueType;

public class LintingResult {

	private int numberOfIssues;
	Map<IssueType, Integer> results;

	public LintingResult(List<QualityIssue> issues) { 
		this.numberOfIssues = issues.size();

		this.results = new HashMap<IssueType, Integer>();
		issues.forEach(issue -> {

//			if(issue instanceof MultipleOccurrencesIssue) 
//				((MultipleOccurrencesIssue) issue).getOccurences().forEach(o ->{
//					if(o!= null) System.out.println(issue.getIssueType() + " : " + o.getProperty(SourceLocation.class));
//				});
//			if(issue instanceof SingleOcurrenceIssue && issue != null) 
//				System.out.println(issue.getIssueType() + " : " + ((SingleOcurrenceIssue) issue).getOccurrence() + " " + ((SingleOcurrenceIssue) issue).getOccurrence().getProperty(SourceLocation.class));


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
