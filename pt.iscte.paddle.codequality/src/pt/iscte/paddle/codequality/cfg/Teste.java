package pt.iscte.paddle.codequality.cfg;

import static pt.iscte.paddle.model.IOperator.GREATER;
import static pt.iscte.paddle.model.IOperator.SMALLER;
import static pt.iscte.paddle.model.IType.INT;

import java.io.File;
import java.util.ArrayList;

import pt.iscte.paddle.javali.translator.Translator;
import pt.iscte.paddle.model.IBlock;
import pt.iscte.paddle.model.ILoop;
import pt.iscte.paddle.model.IModule;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IReturn;
import pt.iscte.paddle.model.ISelection;
import pt.iscte.paddle.model.IVariable;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.cfg.IBranchNode;
import pt.iscte.paddle.model.cfg.IControlFlowGraph;
import pt.iscte.paddle.model.cfg.IStatementNode;

public class Teste {
	
	public static void main(String[] args) {
		
		File codeToCheck = new File("test.javali");
		Translator translator = new Translator(codeToCheck.getAbsolutePath());
		IModule module1 = translator.createProgram();
		IProcedure procedure = module1.getProcedures().iterator().next(); // first procedure
		
		IControlFLowGraphBuilder cfgBuilder = IControlFLowGraphBuilder.create(procedure);
		cfgBuilder.display();
	}

}
