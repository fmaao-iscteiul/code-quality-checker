import java.io.File;
import java.util.ArrayList;

import pt.iscte.paddle.interpreter.ExecutionError;
import pt.iscte.paddle.javali.translator.ElementLocation;
import pt.iscte.paddle.javali.translator.Model2Java;
import pt.iscte.paddle.javali.translator.Translator;
import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.model.IModule;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.ISelection;

public class DemoModelFromJavaAndVisitor {
	
	private static class Teste implements IVisitor{
			
		int counter = 0;
		ArrayList<ISelection> lista = new ArrayList<ISelection>();
		
		@Override
		public boolean visit(ISelection selection) {
			if(selection.getBlock().isEmpty()) {
				lista.add(selection);
				ElementLocation loc = (ElementLocation) selection.getProperty(ElementLocation.Part.WHOLE);
				System.out.println(loc.getLine());
			}
			this.counter++;
			return IVisitor.super.visit(selection);
		}
		
	}

	public static void main(String[] args) throws ExecutionError {

		// instantiate model from file
		Translator translator = new Translator(new File("naturals.javali").getAbsolutePath());
		IModule module = translator.createProgram();
		IProcedure nats = module.getProcedures().iterator().next(); // first procedure

		// prints model as code
		String src = module.translate(new Model2Java());
		System.out.println(src);

		// Model visitor
		System.out.println("Assignments to variable i:");
		
		Teste teste = new Teste();
		nats.accept(teste);
		System.out.println(teste.lista);
	}
}