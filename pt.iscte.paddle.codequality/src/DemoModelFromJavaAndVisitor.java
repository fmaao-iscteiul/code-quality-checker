import java.io.File;
import java.util.ArrayList;

import pt.iscte.paddle.interpreter.ExecutionError;
import pt.iscte.paddle.javali.translator.ElementLocation;
import pt.iscte.paddle.javali.translator.Model2Java;
import pt.iscte.paddle.javali.translator.Translator;
import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.model.IBlock;
import pt.iscte.paddle.model.IBreak;
import pt.iscte.paddle.model.IModule;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.ISelection;
import pt.iscte.paddle.codequality.cases.*;

public class DemoModelFromJavaAndVisitor {

	public static void main(String[] args) throws ExecutionError {

		// instantiate model from file
		Translator translator = new Translator(new File("test.javali").getAbsolutePath());
		IModule module = translator.createProgram();
		IProcedure nats = module.getProcedures().iterator().next(); // first procedure

		// prints model as code
		String src = module.translate(new Model2Java());
//		System.out.println(src);

		// Model visitor
		System.out.println("Checking the following block of code: ");
		System.out.println(nats);
		EmptyCondition IfChecker = new EmptyCondition();
		
		nats.accept(IfChecker);
	}
}