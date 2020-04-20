package pt.iscte.paddle.codequality.visitors;
import java.util.ArrayList;
import java.util.List;

import pt.iscte.paddle.codequality.cases.FaultyAssignment;
import pt.iscte.paddle.codequality.linter.Linter;
import pt.iscte.paddle.codequality.misc.Explanations;
import pt.iscte.paddle.model.IArrayElementAssignment;
import pt.iscte.paddle.model.IBlockElement;
import pt.iscte.paddle.model.IExpression;
import pt.iscte.paddle.model.ILiteral;
import pt.iscte.paddle.model.ILoop;
import pt.iscte.paddle.model.IProcedureCall;
import pt.iscte.paddle.model.IProcedureCallExpression;
import pt.iscte.paddle.model.ISelection;
import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.model.IStatement;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.IVariableDeclaration;


public class UselessAssignment implements IVisitor {

	private List<Statement> uselessStatements = new ArrayList<Statement>();

	public class Statement {
		private IVariableDeclaration var;
		private boolean used;

		public Statement(IVariableDeclaration variable) {
			this.var = variable;
			this.used = false;
		}

		@Override
		public String toString() {
			return var.toString();
		}

		@Override
		public boolean equals(Object obj) {
			if(obj instanceof IVariableDeclaration)
				return var.equals(obj);
			return false;
		}
	}

	@Override
	public boolean visit(IArrayElementAssignment assignment) {
		if(uselessStatements.contains(assignment.getTarget())) {
			uselessStatements.get(uselessStatements.indexOf(assignment.getTarget())).used = true;
		}

		return false;
	}

	@Override
	public boolean visit(IVariableAssignment assignment) {
		boolean exists = false;
		for (Statement ass : uselessStatements) {
			if(!ass.used && ass.var.equals(assignment.getTarget())) {
				System.out.println("aqui2: " + assignment);
				exists = true;
				ass.used = true;
			} else if (assignment.getExpression().includes(ass.var)) ass.used = true;
		}
		if(!exists) uselessStatements.add(new Statement(assignment.getTarget()));

		if(assignment.getTarget().toString().equals(assignment.getExpression().toString()))
			Linter.getInstance().register(new FaultyAssignment(Explanations.SELF_ASSIGNMENT, assignment));
		return false;
	}

	@Override
	public boolean visit(IProcedureCall call) {
		call.getArguments().forEach(argument -> {
			for (Statement statement : uselessStatements) {
				if(argument.includes(statement.var)) statement.used = true;
			}
		});
		return false;
	}
	
	@Override
	public boolean visit(ISelection selection) {
		for (Statement statement : uselessStatements) {
			if(selection.getGuard().includes(statement.var)) statement.used = true;
		}
		return false;
	}
	
	@Override
	public boolean visit(ILoop loop) {
		for (Statement statement : uselessStatements) {
			if(loop.getGuard().includes(statement.var)) statement.used = true;
		}
		return false;
	}
}
