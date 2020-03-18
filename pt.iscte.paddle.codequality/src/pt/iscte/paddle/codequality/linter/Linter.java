package pt.iscte.paddle.codequality.linter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pt.iscte.paddle.codequality.Icfg.IControlFlowGraphBuilder;
import pt.iscte.paddle.codequality.cases.BadCodeCase;
import pt.iscte.paddle.codequality.misc.BadCodeAnalyser;
import pt.iscte.paddle.codequality.misc.ModuleProcedure;
import pt.iscte.paddle.codequality.visitors.DeadCode;
import pt.iscte.paddle.codequality.visitors.DuplicateGuard;
import pt.iscte.paddle.codequality.visitors.DuplicateStatement;
import pt.iscte.paddle.codequality.visitors.Loop;
import pt.iscte.paddle.codequality.visitors.MagicNumbers;
import pt.iscte.paddle.codequality.visitors.MethodCall;
import pt.iscte.paddle.codequality.visitors.Return;
import pt.iscte.paddle.codequality.visitors.Selection;
import pt.iscte.paddle.codequality.visitors.Unreachable;
import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.model.IModule;
import pt.iscte.paddle.model.IProcedure;

public enum Linter {
	
	INSTANCE;
	private Linter() {}
	
//	private Translator translator;
	private IModule module;
	
	private List<ModuleProcedure> procedures = new ArrayList<>();

	private ArrayList<IVisitor> visitors = new ArrayList<>();
	private List<BadCodeAnalyser> analysers = new ArrayList<BadCodeAnalyser>();
	
	private ArrayList<BadCodeCase> caughtCases = new ArrayList<>();
	
	public static Linter getInstance() {
		return INSTANCE;
	}

	
	public Linter init(IModule module) {
		this.module = module;
		module.getProcedures().forEach(procedure -> this.procedures.add(new ModuleProcedure(procedure)));		
		
		return INSTANCE;
	}

	public Linter loadVisitors() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		this.visitors.add(Selection.build());
		this.visitors.add(Loop.build());
		this.visitors.add(Return.build());
		this.visitors.add(MagicNumbers.build());
		this.visitors.add(DeadCode.build());
		this.visitors.add(MethodCall.build());
		this.visitors.add(DuplicateStatement.build(null));
				
		return this;
	}
	
	public Linter loadAnalysers() {
		this.procedures.forEach(mProcedure -> {
			this.analysers.add(Unreachable.build(mProcedure.getCfgBuilder()));
			this.analysers.add(DuplicateGuard.build(mProcedure.getCfgBuilder()));
			this.analysers.add(DuplicateStatement.build(mProcedure.getCfgBuilder()));
			this.analysers.add(MethodCall.build(mProcedure.getCfgBuilder()));
		});
		
		return this;
	}
	
	public ArrayList<BadCodeCase> analyse() {
		this.analysers.forEach(analyser -> analyser.analyse());
		this.procedures.forEach(p -> p.getCfgBuilder().display());
		this.visitors.forEach(visitor -> this.procedures.forEach(mProcedure -> mProcedure.getProcedure().accept(visitor)));
		
		return caughtCases;
	}

	public ArrayList<IVisitor> getVisitors() {
		return this.visitors;
	}
	public ArrayList<BadCodeCase> getCaughtCases() {
		return caughtCases;
	}
	public void register(BadCodeCase catchedCase) {
		this.caughtCases.add(catchedCase);
	}
	public IModule getModule() {
		return this.module;
	}
	public List<ModuleProcedure> getProcedures() {
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


