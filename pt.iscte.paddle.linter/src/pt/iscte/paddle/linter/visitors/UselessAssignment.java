package pt.iscte.paddle.linter.visitors;
import java.util.ArrayList;
import java.util.List;


import pt.iscte.paddle.linter.cases.base.CodeAnalyser;
import pt.iscte.paddle.linter.cases.base.QualityIssue;
import pt.iscte.paddle.linter.issues.Contradiction;
import pt.iscte.paddle.linter.issues.Tautology;
import pt.iscte.paddle.linter.issues.UselessSelfAssignment;
import pt.iscte.paddle.linter.issues.UselessVariableAssignment;
import pt.iscte.paddle.linter.linter.Linter;
import pt.iscte.paddle.linter.misc.BadCodeAnalyser;
import pt.iscte.paddle.linter.misc.Explanations;
import pt.iscte.paddle.model.IBinaryExpression;
import pt.iscte.paddle.model.IControlStructure;
import pt.iscte.paddle.model.IExpression;
import pt.iscte.paddle.model.IOperator;
import pt.iscte.paddle.model.IOperator.OperationType;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.model.IStatement;
import pt.iscte.paddle.model.IType;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.IVariableDeclaration;
import pt.iscte.paddle.model.cfg.IBranchNode;
import pt.iscte.paddle.model.cfg.IControlFlowGraph;
import pt.iscte.paddle.model.cfg.INode;


public class UselessAssignment extends CodeAnalyser implements BadCodeAnalyser {

	public UselessAssignment(IProcedure procedure) {
		super(procedure);
	}

	private List<Statement> assignmentStatements = new ArrayList<Statement>();


	public class Statement {
		private IVariableDeclaration var;
		private IVariableAssignment assignment;
		private INode node;

		public Statement(INode node, IVariableAssignment ass) {
			this.node = node;
			this.var = ass.getTarget();
			this.assignment = ass;
		}

		@Override
		public String toString() {
			return "Variable --> " + var.toString() + "  Assignment --> " + assignment;
		}

		@Override
		public boolean equals(Object obj) {
			if(obj instanceof IVariableDeclaration)
				return var.equals(obj);
			return false;
		}
	}

	@Override
	public void analyse(IControlFlowGraph cfg) {

		for (INode node : cfg.getNodes()) {
			IProgramElement element = node.getElement();
			if(element instanceof IVariableAssignment) {
				IVariableAssignment assignment = (IVariableAssignment) element;
				boolean exists = false;
				for (Statement ass : assignmentStatements) {
					if(ass.assignment.getParent().isSame(assignment.getParent()) && ass.var.equals(assignment.getTarget())) {
						if(!cfg.usedOrChangedBetween(ass.node, node, ass.var)) {
							issues.add(new UselessVariableAssignment(getProcedure(), ass.assignment));
						}
						ass.assignment = assignment;
						ass.node = node;
						exists = true;
					} 
				}
				if(!exists) assignmentStatements.add(new Statement(node, assignment));

				if(assignment.getTarget().toString().equals(assignment.getExpression().toString())) {
					issues.add(new UselessSelfAssignment(getProcedure(), assignment));
				}


			} else if(node instanceof IBranchNode) {
				IControlStructure selection = element.getProperty(IControlStructure.class);
				if(selection.getGuard().isSame(IType.BOOLEAN.literal(true))) {
					issues.add(new Tautology(Explanations.TAUTOLOGY, getProcedure(), selection.getGuard()));
					continue;
				}
				else if(selection.getGuard().isSame(IType.BOOLEAN.literal(false))) {
					issues.add(new Contradiction(Explanations.CONTRADICTION, getProcedure(), selection.getGuard()));
					continue;
				}

				guardPartsDeepSearch(cfg, node, selection.getGuard());
			}
		}	
	}

	void guardPartsDeepSearch(IControlFlowGraph cfg, INode node, IExpression guard) {
		if(guard instanceof IBinaryExpression) {
			IBinaryExpression condition = (IBinaryExpression) guard;
			if(guard.getOperationType().equals(OperationType.LOGICAL) && !condition.getOperator().equals(IOperator.OR)) return;
		}
		if(!guard.getParts().isEmpty())
			for (IExpression part : guard.getParts()) 
				guardPartsDeepSearch(cfg, node, part); 
		else {
			for (Statement statement : assignmentStatements) {
				IControlStructure selection = guard.expression().getProperty(IControlStructure.class);
				IVariableAssignment assignmentStatement = ((IVariableAssignment) statement.node.getElement());

				if(selection != null && selection.getParent().isSame(assignmentStatement.getParent())
						&& statement.var.expression().isSame(guard.expression()) 
						&& !cfg.usedOrChangedBetween(statement.node, node, statement.var)) {
					if(assignmentStatement.getExpression().isSame(IType.BOOLEAN.literal(true))) {
						issues.add(new Tautology(Explanations.TAUTOLOGY, getProcedure(), guard));
						return;
					}
					else if(assignmentStatement.getExpression().isSame(IType.BOOLEAN.literal(false))) {
						issues.add(new Contradiction(Explanations.CONTRADICTION, getProcedure(), guard));
						return;
					}
				}
			}
		}
	}

}
