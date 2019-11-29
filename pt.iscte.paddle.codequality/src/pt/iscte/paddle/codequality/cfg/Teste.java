package pt.iscte.paddle.codequality.cfg;

import java.io.File;

import pt.iscte.paddle.javali.translator.Translator;
import pt.iscte.paddle.model.IModule;
import pt.iscte.paddle.model.IProcedure;

public class Teste {
	
	public static void main(String[] args) {
		
		File codeToCheck = new File("test.javali");
		Translator translator = new Translator(codeToCheck.getAbsolutePath());
		IModule module1 = translator.createProgram();
		IProcedure procedure = module1.getProcedures().iterator().next(); // first procedure
		
		CFGBuilder cfgBuilder = new CFGBuilder(procedure);
		cfgBuilder.loadCFG();
		cfgBuilder.loadVisitors();
		cfgBuilder.build();
		cfgBuilder.displayCFG();
	}

}
