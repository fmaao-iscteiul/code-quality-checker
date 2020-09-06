package pt.iscte.paddle.quality.client;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import pt.iscte.paddle.model.javaparser.SourceLocation;
import pt.iscte.paddle.quality.cases.base.MultipleOccurrencesIssue;
import pt.iscte.paddle.quality.cases.base.QualityIssue;
import pt.iscte.paddle.quality.cases.base.SingleOcurrenceIssue;
import pt.iscte.paddle.quality.misc.IssueType;

public class LintingResult {

	private class IssueMetric {
		int totalOccurrences = 1;
		int numberOfProjects = 0;
	}

	private int numberOfIssues = 0;
	Map<IssueType, IssueMetric> occurrencesCounter;
	Map<String, List<IssueType>> issuesPerProject;

	public LintingResult(Map<String, List<QualityIssue>> modules) {

		try {
			File f = new File("/Users/franciscoalfredo/Desktop/uni/tese/logs/linter_" + new Date().getTime() + ".txt");
			FileWriter myWriter = new FileWriter(f);

			this.occurrencesCounter = new HashMap<IssueType, IssueMetric>();
			this.issuesPerProject = new HashMap<String, List<IssueType>>();

			for (Entry<String, List<QualityIssue>> model : modules.entrySet()) {

				Map<IssueType, Integer> issuesCounter = new HashMap<IssueType, Integer>(); 

				issuesPerProject.put(model.getKey(), new ArrayList<IssueType>());
				numberOfIssues += model.getValue().size();
				myWriter.append("\n------------ Module: " + model.getKey() + " ------------\n");
				model.getValue().forEach(i -> {
					try {
						if(i instanceof MultipleOccurrencesIssue) 
							((MultipleOccurrencesIssue) i).getOccurences().forEach(o ->{
								if(o!= null) {
									try {
										SourceLocation location = o.getProperty(SourceLocation.class);
										if(location != null) {
											String[] locParts = location.toString().split("/");
											myWriter.append("\n"+i.getIssueType() + " : " + o + " " + o.getProperty(SourceLocation.class) + " Location --> " + locParts[locParts.length - 1]);
										}
										else myWriter.append("\n"+i.getIssueType() + " : " + o);
									} catch (IOException e) {
										e.printStackTrace();
									}
								}

							});
						if(i != null && i instanceof SingleOcurrenceIssue) {
							SourceLocation location = ((SingleOcurrenceIssue) i).getOccurrence().getProperty(SourceLocation.class);
							if(location != null) {
								String[] locParts = location.toString().split("/");
								myWriter.append("\n"+i.getIssueType() + " : " + ((SingleOcurrenceIssue) i).getOccurrence() + " Location --> " + locParts[locParts.length - 1]);
							} 
							else myWriter.append("\n"+i.getIssueType() + " : " + ((SingleOcurrenceIssue) i).getOccurrence());
						}

						if(occurrencesCounter.containsKey(i.getIssueType())) 
							occurrencesCounter.get(i.getIssueType()).totalOccurrences += 1;
						else occurrencesCounter.put(i.getIssueType(), new IssueMetric());


						if(!issuesPerProject.get(model.getKey()).contains(i.getIssueType())) {
							occurrencesCounter.get(i.getIssueType()).numberOfProjects += 1;
							issuesPerProject.get(model.getKey()).add(i.getIssueType());
						}

						if(!issuesCounter.containsKey(i.getIssueType()))
							issuesCounter.put(i.getIssueType(), 1);
						else issuesCounter.put(i.getIssueType(), issuesCounter.get(i.getIssueType()) + 1);

					} catch (IOException e) {
						e.printStackTrace();
					}
				});
				myWriter.append(modelResults(issuesCounter));
			}

			myWriter.append("\n\n"+finalResults());
			myWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public LintingResult(List<QualityIssue> issues) { 
		this.numberOfIssues = issues.size();

		try {
			File f = new File("/Users/franciscoalfredo/Desktop/uni/tese/logs/linter_" + new Date().getTime() + ".txt");
			FileWriter myWriter = new FileWriter(f);

			this.occurrencesCounter = new HashMap<IssueType, IssueMetric>();
			for (QualityIssue issue : issues) {
				try {
					if(issue instanceof MultipleOccurrencesIssue) 
						((MultipleOccurrencesIssue) issue).getOccurences().forEach(o ->{
							if(o!= null)
								try {
									myWriter.append("\n"+issue.getIssueType() + " : " + o + " " + o.getProperty(SourceLocation.class));
								} catch (IOException e) {
									e.printStackTrace();
								}
						});
					if(issue != null && issue instanceof SingleOcurrenceIssue)
						myWriter.append("\n"+issue.getIssueType() + " : " + ((SingleOcurrenceIssue) issue).getOccurrence() + " " + ((SingleOcurrenceIssue) issue).getOccurrence().getProperty(SourceLocation.class));

					if(occurrencesCounter.containsKey(issue.getIssueType())) 
						occurrencesCounter.get(issue.getIssueType()).totalOccurrences += 1;
					else occurrencesCounter.put(issue.getIssueType(), new IssueMetric());

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			myWriter.append("\n\n"+finalResults());
			myWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String modelResults(Map<IssueType, Integer> issuesCounter) {
		int total = 0;
		String results = "";
		for (Entry<IssueType, Integer> issue : issuesCounter.entrySet()) {
			results = results.concat("\n      " + issue.getKey() + " - " + issue.getValue());
			total += issue.getValue();
		}
		return "\n\n---------------- Results for the model above: "+total+" issues were found ----------------\n\n".concat(results).concat("\n\n");
	}

	public String finalResults() {
		String resultsText =  "------------------- FINAL RESULTS -------------------\n\nTHE TOTAL NUMBER OF ISSUES FOUND WITHIN THE PROVIDED MODULES IS " 
				+ this.numberOfIssues +
				" ACROSS " +  issuesPerProject.size() + (issuesPerProject.size() > 1 ?  " MODULES" : " MODULE") + " \n   THE CATEGORIES WERE: \n";
		for (Entry<IssueType, IssueMetric> issue : occurrencesCounter.entrySet()) {
			resultsText = resultsText.concat("      " + issue.getKey() + " - " + issue.getValue().totalOccurrences 
					+ " TIMES ACROSS " + issue.getValue().numberOfProjects + (issue.getValue().numberOfProjects > 1 ? " MODULES" : " MODULE") + ".\n");			
		}

		resultsText = resultsText.concat("\n-----------------------------------------------");

		return resultsText;
	}


}
