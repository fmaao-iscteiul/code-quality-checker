package pt.iscte.paddle.codequality.linter;
import java.io.File;
import java.util.ArrayList;

import pt.iscte.paddle.codequality.cases.BadCodeCase;
import pt.iscte.paddle.codequality.cases.EmptySelection;
import pt.iscte.paddle.codequality.visitors.Selection;
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
	private ArrayList<BadCodeCase> caughtCases = new ArrayList<>();

	public static Linter getInstance() {
		return INSTANCE;
	}
	
	public Linter init(File file) {
		this.translator = new Translator(file.getAbsolutePath());
		this.module = translator.createProgram();
		this.procedure = module.getProcedures().iterator().next(); // first procedure
		
		return INSTANCE;
	}

	public ArrayList<IVisitor> loadVisitors() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		this.visitors.add(Selection.buildSelectionVisitor());
		return this.visitors;
	}

	public ArrayList<IVisitor> getVisitors() {
		return this.visitors;
	}
	public ArrayList<BadCodeCase> getCaughtCases() {
		return caughtCases;
	}
	public void registerCatchedCase(BadCodeCase catchedCase) {
		this.caughtCases.add(catchedCase);
	}
	public IModule getModule() {
		return this.module;
	}
	public IProcedure getProcedure() {
		return this.procedure;
	}

	public static void main(String[] args) throws ExecutionError, InstantiationException, IllegalAccessException, ClassNotFoundException{
		Linter TheLinter = Linter.INSTANCE.init(new File("test3.javali"));
		IProcedure currentProcedure = TheLinter.getProcedure();
		TheLinter.loadVisitors().forEach(visitor -> currentProcedure.accept(visitor));
	}
}


