package pt.iscte.paddle.codequality.linter;
import java.util.ArrayList;
import java.util.List;

import pt.iscte.paddle.codequality.cases.BadCodeCase;
import pt.iscte.paddle.codequality.misc.BadCodeAnalyser;
import pt.iscte.paddle.codequality.visitors.DeadCode;
import pt.iscte.paddle.codequality.visitors.DuplicateGuard;
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
	private IModule module;
	
	private List<IProcedure> procedures = new ArrayList<>();

	private ArrayList<IVisitor> visitors = new ArrayList<>();
	private List<BadCodeAnalyser> analysers = new ArrayList<BadCodeAnalyser>();
	
	private ArrayList<BadCodeCase> caughtCases = new ArrayList<>();
	
	public static Linter getInstance() {
		return INSTANCE;
	}

	
	public Linter init(IModule module) {
		this.module = module;
		module.getProcedures().forEach(procedure -> this.procedures.add(procedure));		
		
		return INSTANCE;
	}

	public Linter loadVisitors() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		this.visitors.add(Selection.build());
		this.visitors.add(Loop.build());
		this.visitors.add(MagicNumbers.build());
		this.visitors.add(DeadCode.build());
		this.visitors.add(new UselessAssignment());
		this.visitors.add(DuplicateStatement.build(null));
				
		return this;
	}
	
	public Linter loadAnalysers() {
		this.procedures.forEach(mProcedure -> {
			IControlFlowGraph cfg = mProcedure.generateCFG();
			cfg.display();			
			this.analysers.add(Unreachable.build(cfg));
			this.analysers.add(DuplicateGuard.build(cfg));
			this.analysers.add(DuplicateStatement.build(cfg));
			this.analysers.add(ProcedureCall.build(cfg));
			this.analysers.add(Return.build(cfg));
		});
		
		return this;
	}
	
	public ArrayList<BadCodeCase> analyse() {
		this.analysers.forEach(analyser -> analyser.analyse());
		this.visitors.forEach(visitor -> this.procedures.forEach(mProcedure -> mProcedure.accept(visitor)));
		
		return caughtCases;
	}

	public ArrayList<IVisitor> getVisitors() {
		return this.visitors;
	}
	public ArrayList<BadCodeCase> getCaughtCases() {
		return caughtCases;
	}
	public void register(BadCodeCase catchedCase) {
		// TODO add classification sorting.
		this.caughtCases.add(catchedCase);
	}
	public IModule getModule() {
		return this.module;
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


