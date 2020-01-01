import java.io.File;

import pt.iscte.paddle.codequality.cases.EmptyCondition;
import pt.iscte.paddle.interpreter.ExecutionError;
import pt.iscte.paddle.javali.translator.Translator;
import pt.iscte.paddle.model.IModel2CodeTranslator;
import pt.iscte.paddle.model.IModule;
import pt.iscte.paddle.model.IProcedure;

public class DemoModelFromJavaAndVisitor {

	public static void main(String[] args) throws ExecutionError {

		// instantiate model from file
		Translator translator = new Translator(new File("test.javali").getAbsolutePath());
		IModule module = translator.createProgram();
		IProcedure nats = module.getProcedures().iterator().next(); // first procedure

		// prints model as code
		String src = module.translate(new IModel2CodeTranslator.Java());
//		System.out.println(src);

		// Model visitor
		System.out.println("Checking the following block of code: ");
		System.out.println(nats);
		EmptyCondition IfChecker = new EmptyCondition();
		
		nats.accept(IfChecker);
	}
}