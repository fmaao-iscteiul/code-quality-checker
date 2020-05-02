package pt.iscte.paddle.codequality.linter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pt.iscte.paddle.codequality.cases.base.QualityIssue;
import pt.iscte.paddle.codequality.misc.BadCodeAnalyser;
import pt.iscte.paddle.codequality.misc.IssueType;
import pt.iscte.paddle.codequality.misc.Classification;
import pt.iscte.paddle.codequality.visitors.DuplicateBranchGuard;
import pt.iscte.paddle.codequality.visitors.DuplicateStatement;
import pt.iscte.paddle.codequality.visitors.Loop;
import pt.iscte.paddle.codequality.visitors.MagicNumbers;
import pt.iscte.paddle.codequality.visitors.ProcedureCall;
import pt.iscte.paddle.codequality.visitors.Return;
import pt.iscte.paddle.codequality.visitors.Selection;
import pt.iscte.paddle.codequality.visitors.Unreachable;
import pt.iscte.paddle.codequality.visitors.UselessAssignment;
import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.model.IModule;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.cfg.IControlFlowGraph;

public enum Linter {

	INSTANCE;
	private Linter() {}

	//	private Translator translator;
	private List<IModule> modules = new ArrayList<IModule>(1);

	private List<IProcedure> procedures = new ArrayList<>();

	private ArrayList<IVisitor> visitors = new ArrayList<>();
	private List<BadCodeAnalyser> analysers = new ArrayList<BadCodeAnalyser>();

	private ArrayList<QualityIssue> caughtIssues = new ArrayList<>();

	public static Linter getInstance() {
		return INSTANCE;
	}


	public Linter init(IModule... modules) {
		for (IModule module : modules) {
			this.modules.add(module);
			module.getProcedures().forEach(procedure -> this.procedures.add(procedure));		
		}
		return INSTANCE;
	}

	public Linter init(List<IModule> modules) {
		for (IModule module : modules) 
			init(module);
		return this;
	}

	public void loadVisitors() {
		this.visitors.add(Selection.build());
		this.visitors.add(Loop.build());
		this.visitors.add(MagicNumbers.build());
		this.visitors.add(DuplicateStatement.build(null));

	}

	public void loadAnalysers() {
		this.procedures.forEach(mProcedure -> {
			IControlFlowGraph cfg = mProcedure.generateCFG();
			cfg.display();
			this.visitors.add(new UselessAssignment(cfg));
			this.analysers.add(new UselessAssignment(cfg));
			this.analysers.add(Unreachable.build(cfg));
			this.analysers.add(DuplicateBranchGuard.build(cfg));
			this.analysers.add(DuplicateStatement.build(cfg));
			this.analysers.add(ProcedureCall.build(cfg));
			this.analysers.add(Return.build(cfg));
		});

	}

	public ArrayList<QualityIssue> analyse() {
		loadAnalysers();
		loadVisitors();
		this.analysers.forEach(analyser -> analyser.analyse());
		this.visitors.forEach(visitor -> this.procedures.forEach(mProcedure -> mProcedure.accept(visitor)));
		listResults();
		return caughtIssues;
	}

	public void listResults() {
		Map<IssueType, Integer> results = new HashMap<IssueType, Integer>();
		caughtIssues.forEach(issue -> {
			if(results.containsKey(issue.getIssueType())) 
				results.replace(issue.getIssueType(), results.get(issue.getIssueType()) + 1);
			else results.put(issue.getIssueType(), 1);
		});
		System.out.println("------------------- RESULTS -------------------");
		System.out.println("THE TOTAL NUMBER OF ISSUES IS: " + caughtIssues.size());
		System.out.println("THE ISSUES FOUND CATEGORIES WERE: ");
		results.forEach((category, result) -> {
			System.out.println("    " + category + " - " + result + " TIMES.");
		});
		System.out.println("-----------------------------------------------");
	}

	public ArrayList<IVisitor> getVisitors() {
		return this.visitors;
	}
	public ArrayList<QualityIssue> getCaughtCases() {
		return caughtIssues;
	}
	public void register(QualityIssue catchedCase) {
		// TODO add classification sorting.
		this.caughtIssues.add(catchedCase);
		this.caughtIssues.sort(new Comparator<QualityIssue>() {
			@Override
			public int compare(QualityIssue o1, QualityIssue o2) {
				if(o1.getClassification().equals(o2.getClassification()) ) return 0;
				else if(o1.getClassification().equals(Classification.SERIOUS) && o2.getClassification().equals(Classification.LIGHT)) return -1;
				else if(o1.getClassification().equals(Classification.LIGHT) && o2.getClassification().equals(Classification.SERIOUS)) return 1;
				else if(o1.getClassification().equals(Classification.SERIOUS) && o2.getClassification().equals(Classification.AVERAGE)) return -1;
				else if(o1.getClassification().equals(Classification.AVERAGE) && o2.getClassification().equals(Classification.SERIOUS)) return 1;
				else if(o1.getClassification().equals(Classification.AVERAGE) && o2.getClassification().equals(Classification.LIGHT)) return -1;
				else if(o1.getClassification().equals(Classification.LIGHT) && o2.getClassification().equals(Classification.AVERAGE)) return 1;
				else return 0;
			}
		});
	}
	public List<IModule> getModules() {
		return this.modules;
	}
	public List<IProcedure> getProcedures() {
		return this.procedures;
	}

	//	public static void main(String[] args) throws ExecutionError, InstantiationException, IllegalAccessException, ClassNotFoundException{
	//		Linter TheLinter = Linter.INSTANCE.init(new File("test3.javali"));
	//		
	//		TheLinter.loadVisitors().analyse().forEach(caughtCase -> System.out.println(caughtCase.getCaseCategory()));
	//		
	//		
	//		TheLinter.getModule();
	//	}
}


