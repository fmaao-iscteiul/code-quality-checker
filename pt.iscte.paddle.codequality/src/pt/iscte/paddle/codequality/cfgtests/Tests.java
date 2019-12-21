package pt.iscte.paddle.codequality.cfgtests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.Test;

import pt.iscte.paddle.codequality.cfg.IControlFLowGraphBuilder;
import pt.iscte.paddle.javali.translator.Translator;
import pt.iscte.paddle.model.IModule;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.cfg.IControlFlowGraph;

class Tests {

//	@Test
//	public void teste1() {
//		
//		File codeToCheck = new File("test2.javali");
//		Translator translator = new Translator(codeToCheck.getAbsolutePath());
//		IModule module1 = translator.createProgram();
//		IProcedure procedure = module1.getProcedures().iterator().next(); // first procedure
//		
//		IControlFLowGraphBuilder cfgBuilder = IControlFLowGraphBuilder.create(procedure);
//		
//		cfgBuilder.display();
//		System.out.println("----------------------------------------");
//		CFGcases.case1().getNodes().forEach(n -> System.out.println(n));
//		
//		assertEquals(CFGcases.case1(), cfgBuilder.getCFG(), "The graphs don't look the same!");
//	}
	
	@Test
	public void teste2() {
		
		File codeToCheck = new File("test2.javali");
		Translator translator = new Translator(codeToCheck.getAbsolutePath());
		IModule module1 = translator.createProgram();
		IProcedure procedure = module1.getProcedures().iterator().next(); // first procedure
		
		IControlFLowGraphBuilder cfgBuilder = IControlFLowGraphBuilder.create(procedure);
		IControlFlowGraph hard_coded = CFGcases.case2();
		
		assertTrue(hard_coded.equals(cfgBuilder.getCFG()), "The graphs don't look the same!");
	}

}


