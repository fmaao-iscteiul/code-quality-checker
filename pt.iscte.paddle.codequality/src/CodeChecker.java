import java.io.File;
import java.util.ArrayList;

import pt.iscte.paddle.codequality.cases.EmptyCondition;
import pt.iscte.paddle.interpreter.ExecutionError;
import pt.iscte.paddle.javali.translator.Translator;
import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.model.IModule;
import pt.iscte.paddle.model.IProcedure;

public class CodeChecker {
	
	private Translator translator;
	private IModule module;
	private IProcedure procedure;
	
	private ArrayList<IVisitor> visitors = new ArrayList<>();
	private ArrayList<IVisitor> hits = new ArrayList<>();

	public CodeChecker(File file) {
		this.translator = new Translator(file.getAbsolutePath());
		this.module = translator.createProgram();
		this.procedure = module.getProcedures().iterator().next(); // first procedure
	}
	
	void loadVisitors() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
//		File casesFolder = new File("src/pt/iscte/paddle/codequality/cases");
//		for (File caseFile : casesFolder.listFiles()) {
//			this.visitors.add((IVisitor) Class.forName(caseFile.getAbsolutePath() ).newInstance() ) ;
//		}
//		this.visitors.add(new EmptyExcepetion());
//		this.visitors.add(new CatchBlock());
//		this.visitors.add(new Loop());
//		this.visitors.add(new BlockChecker());
//		this.visitors.add(new VariableDeclaration());
//		CatchBlock catchBlock = new CatchBlock();
		
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
		File codeToCheck = new File("test.javali");
		CodeChecker TheChecker = new CodeChecker(codeToCheck);
		
		TheChecker.loadVisitors();
		
		for (IVisitor visitor : TheChecker.getVisitors()) {
			TheChecker.getProcedure().accept(visitor);
		}
	}
}


