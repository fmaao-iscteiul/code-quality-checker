package pt.iscte.paddle.codequality.linter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pt.iscte.paddle.codequality.Icfg.IControlFlowGraphBuilder;
import pt.iscte.paddle.codequality.cases.BadCodeCase;
import pt.iscte.paddle.codequality.misc.BadCodeAnalyser;
import pt.iscte.paddle.codequality.visitors.Duplication;
import pt.iscte.paddle.codequality.visitors.Loop;
import pt.iscte.paddle.codequality.visitors.MagicNumbers;
import pt.iscte.paddle.codequality.visitors.Return;
import pt.iscte.paddle.codequality.visitors.Selection;
import pt.iscte.paddle.codequality.visitors.Unreachable;
import pt.iscte.paddle.interpreter.ExecutionError;
import pt.iscte.paddle.javali.translator.Translator;
import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.model.IModule;
import pt.iscte.paddle.model.IProcedure;

public enum Linter {

	INSTANCE;
	private Linter() {}
	
	private Translator translator;
	private IModule module;
	private IProcedure procedure;

	private ArrayList<IVisitor> visitors = new ArrayList<>();
	private List<BadCodeAnalyser> analysers = new ArrayList<BadCodeAnalyser>();
	
	private ArrayList<BadCodeCase> caughtCases = new ArrayList<>();
	
	IControlFlowGraphBuilder cfg;

	public static Linter getInstance() {
		return INSTANCE;
	}
	
	public Linter init(File file) {
		this.translator = new Translator(file.getAbsolutePath());
		this.module = translator.createProgram();
		this.procedure = module.getProcedures().iterator().next(); // first procedure
		this.cfg = IControlFlowGraphBuilder.create(procedure);
		
		return INSTANCE;
	}
	
	public Linter init(IModule module) {
		this.module = module;
		this.procedure = module.getProcedures().iterator().next(); // first procedure
		this.cfg = IControlFlowGraphBuilder.create(procedure);
		
		return INSTANCE;
	}

	public Linter loadVisitors() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		this.visitors.add(Selection.build());
		this.visitors.add(Loop.build());
		this.visitors.add(Return.build());
		this.visitors.add(MagicNumbers.build());
		this.analysers.add(Unreachable.build(cfg));
		this.analysers.add(Duplication.build(cfg));
		return this;
	}
	
	public ArrayList<BadCodeCase> analyse() {
		this.visitors.forEach(visitor -> this.procedure.accept(visitor));
		this.analysers.forEach(analyser -> analyser.analyse());
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
	public IProcedure getProcedure() {
		return this.procedure;
	}
	public IControlFlowGraphBuilder getCfgBuilder() {
		return cfg;
	}

	public static void main(String[] args) throws ExecutionError, InstantiationException, IllegalAccessException, ClassNotFoundException{
		Linter TheLinter = Linter.INSTANCE.init(new File("test3.javali"));
		
		TheLinter.loadVisitors().analyse().forEach(caughtCase -> System.out.println(caughtCase.getCaseCategory()));
		
		
		TheLinter.getModule();
	}
}


