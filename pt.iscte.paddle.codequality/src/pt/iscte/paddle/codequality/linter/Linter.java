package pt.iscte.paddle.codequality.linter;
import java.io.File;
import java.util.ArrayList;

import pt.iscte.paddle.codequality.cases.EmptyCondition;
import pt.iscte.paddle.interpreter.ExecutionError;
import pt.iscte.paddle.javali.translator.Translator;
import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.model.IModule;
import pt.iscte.paddle.model.IProcedure;

public class Linter {
	
	private Translator translator;
	private IModule module;
	private IProcedure procedure;
	
	private ArrayList<IVisitor> visitors = new ArrayList<>();
	private ArrayList<IVisitor> caughtCases = new ArrayList<>();

	public Linter(File file) {
		this.translator = new Translator(file.getAbsolutePath());
		this.module = translator.createProgram();
		this.procedure = module.getProcedures().iterator().next(); // first procedure
	}
	
	void loadVisitors() throws InstantiationException, IllegalAccessException, ClassNotFoundException {

		this.visitors.add(new pt.iscte.paddle.codequality.visitors.Condition());		
	}
	
	public ArrayList<IVisitor> getVisitors() {
		return this.visitors;
	}
	public IModule getModule() {
		return this.module;
	}
	public IProcedure getProcedure() {
		return this.procedure;
	}
	
	public static void main(String[] args) throws ExecutionError, InstantiationException, IllegalAccessException, ClassNotFoundException{
		File codeToCheck = new File("test3.javali");
		Linter TheChecker = new Linter(codeToCheck);
		
		TheChecker.loadVisitors();
				
		for (IVisitor visitor : TheChecker.getVisitors()) {
			TheChecker.getProcedure().accept(visitor);
		}
	}
}


