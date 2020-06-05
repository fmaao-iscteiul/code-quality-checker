package pt.iscte.paddle.quality.visitors;
import java.util.ArrayList;
import java.util.List;

import pt.iscte.paddle.model.IBinaryExpression;
import pt.iscte.paddle.model.IControlStructure;
import pt.iscte.paddle.model.IExpression;
import pt.iscte.paddle.model.IOperator;
import pt.iscte.paddle.model.IOperator.OperationType;
import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.model.IStatement;
import pt.iscte.paddle.model.IType;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.IVariableDeclaration;
import pt.iscte.paddle.model.cfg.IBranchNode;
import pt.iscte.paddle.model.cfg.IControlFlowGraph;
import pt.iscte.paddle.model.cfg.INode;
import pt.iscte.paddle.quality.cases.base.CodeAnalyser;
import pt.iscte.paddle.quality.cases.base.QualityIssue;
import pt.iscte.paddle.quality.client.Linter;
import pt.iscte.paddle.quality.issues.Contradiction;
import pt.iscte.paddle.quality.issues.Tautology;
import pt.iscte.paddle.quality.issues.UselessSelfAssignment;
import pt.iscte.paddle.quality.issues.UselessVariableAssignment;
import pt.iscte.paddle.quality.misc.BadCodeAnalyser;
import pt.iscte.paddle.quality.misc.Explanations;


public class UselessAssignment extends CodeAnalyser implements BadCodeAnalyser {

	private List<Statement> assignmentStatements = new ArrayList<Statement>();

	public class Statement {
		private IVariableDeclaration var;
		private IStatement assignment;
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
							issues.add(new UselessVariableAssignment(ass.assignment));
						}
						ass.assignment = assignment;
						ass.node = node;
						exists = true;
					} 
				}
				if(!exists) assignmentStatements.add(new Statement(node, assignment));

				if(assignment.getTarget().toString().equals(assignment.getExpression().toString())) {
					issues.add(new UselessSelfAssignment(assignment));
				}
					

			} else if(node instanceof IBranchNode) {
				IControlStructure selection = element.getProperty(IControlStructure.class);
				if(selection.getGuard().isSame(IType.BOOLEAN.literal(true))) {
					issues.add(new Tautology(Explanations.TAUTOLOGY, selection.getGuard()));
					return;
				}
				else if(selection.getGuard().isSame(IType.BOOLEAN.literal(false))) {
					issues.add(new Contradiction(Explanations.CONTRADICTION, selection.getGuard()));
					return;
				}
				guardPartsDeepSearch(cfg, node, selection.getGuard());
			}
		}	
	}

	// TODO 20-3-2020 - change/refactor the shit out of this mess.
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
				if(statement.var.expression().isSame(guard.expression()) 
						&& !cfg.usedOrChangedBetween(statement.node, node, statement.var)) {
					if(((IVariableAssignment) statement.node.getElement()).getExpression().isSame(IType.BOOLEAN.literal(true))) {
						issues.add(new Tautology(Explanations.TAUTOLOGY, guard));
						return;
					}
					else if(((IVariableAssignment) statement.node.getElement()).getExpression().isSame(IType.BOOLEAN.literal(false))) {
						issues.add(new Contradiction(Explanations.CONTRADICTION, guard));
						return;
					}
				}
			}
		}
	}

}
