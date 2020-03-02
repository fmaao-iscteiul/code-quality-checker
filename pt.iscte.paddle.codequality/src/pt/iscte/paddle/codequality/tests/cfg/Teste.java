package pt.iscte.paddle.codequality.tests.cfg;

import java.io.File;

import pt.iscte.paddle.codequality.Icfg.IControlFlowGraphBuilder;
import pt.iscte.paddle.javali.translator.Translator;
import pt.iscte.paddle.model.IModule;
import pt.iscte.paddle.model.IProcedure;

public class Teste {
	
	public static void main(String[] args) {
		
		File codeToCheck = new File("test.javali");
		Translator translator = new Translator(codeToCheck.getAbsolutePath());
		IModule module1 = translator.createProgram();
		IProcedure procedure = module1.getProcedures().iterator().next(); // first procedure
		
		IControlFlowGraphBuilder cfgBuilder = IControlFlowGraphBuilder.create(procedure);
		cfgBuilder.display();
	}

}
