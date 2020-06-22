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
			
			for (Entry<String, List<QualityIssue>> project : modules.entrySet()) {
				numberOfIssues += project.getValue().size();
				myWriter.append("\n------------ Module: " + project.getKey() + " ------------\n");
				project.getValue().forEach(i -> {
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
						
							
						if(issuesPerProject.get(project.getKey()) == null) {
							ArrayList<IssueType> types = new ArrayList<IssueType>();
							types.add(i.getIssueType());
							issuesPerProject.put(project.getKey(), types);
							occurrencesCounter.get(i.getIssueType()).numberOfProjects += 1;
						}
						else if(!issuesPerProject.get(project.getKey()).contains(i.getIssueType())) {
							occurrencesCounter.get(i.getIssueType()).numberOfProjects += 1;
							issuesPerProject.get(project.getKey()).add(i.getIssueType());
						}

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
			}

			myWriter.append("\n\n"+toString());
			myWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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

			myWriter.append("\n\n"+toString());
			myWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {

		String resultsText =  "------------------- FINAL RESULTS -------------------\n\nTOTAL NUMBER OF ISSUES FOUND WITHIN THE PROVIDED MODULES IS: " 
				+ this.numberOfIssues +
				" ACROSS " + issuesPerProject.size() +  " MODULES\n   THE CATEGORIES WERE: \n";
		for (Entry<IssueType, IssueMetric> issue : occurrencesCounter.entrySet()) {
			resultsText = resultsText.concat("      " + issue.getKey() + " - " + issue.getValue().totalOccurrences 
					+ " TIMES ACROSS " + issue.getValue().numberOfProjects + " MODULES.\n");			
		}

		resultsText = resultsText.concat("\n-----------------------------------------------");

		return resultsText;
	}

}
