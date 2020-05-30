package snippet;

import java.io.File;
import java.io.IOException;

import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.model.IModule;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.javaparser.Java2Paddle;
import pt.iscte.paddle.model.javaparser.SourceLocation;

public class Snippet {
	//	public static void main(String[] args) { 
	//			IModule mod = IModule.create();
	//			IProcedure p = mod.addProcedure(IType.INT);
	//			p.setId("proc");
	//	System.out.println(mod);
	//	}

	public static void main(String[] args) throws IOException {
		File f = new File("snippet/Example.java"); // adaptar path
		Java2Paddle jparser = new Java2Paddle(f);
		IModule m = jparser.parse();
		m.getProcedure("test").accept(new IVisitor() {
			@Override
			public boolean visit(IVariableAssignment assignment) {
				System.out.println(assignment.getProperty(SourceLocation.class));
				return IVisitor.super.visit(assignment);
			}
		});
		System.out.println(m);
	}


}

