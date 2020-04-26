package pt.iscte.paddle.codequality.visitors;
import java.util.ArrayList;
import java.util.List;

import pt.iscte.paddle.codequality.cases.UselessSelfAssignment;
import pt.iscte.paddle.codequality.cases.UselessVariableAssignment;
import pt.iscte.paddle.codequality.linter.Linter;
import pt.iscte.paddle.codequality.misc.BadCodeAnalyser;
import pt.iscte.paddle.codequality.misc.Explanations;
import pt.iscte.paddle.model.IArrayElementAssignment;
import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.model.IBlockElement;
import pt.iscte.paddle.model.IControlStructure;
import pt.iscte.paddle.model.IExpression;
import pt.iscte.paddle.model.ILoop;
import pt.iscte.paddle.model.IProcedureCall;
import pt.iscte.paddle.model.ISelection;
import pt.iscte.paddle.model.IStatement;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.IVariableDeclaration;
import pt.iscte.paddle.model.cfg.IControlFlowGraph;
import pt.iscte.paddle.model.cfg.IControlFlowGraph.Path;
import pt.iscte.paddle.model.cfg.INode;


public class UselessAssignment implements IVisitor, BadCodeAnalyser {

	private List<Statement> uselessStatements = new ArrayList<Statement>();

	public class Statement {
		private IVariableDeclaration var;
		private IStatement assignment;
		private INode node;

		public Statement(INode node, IVariableDeclaration variable, IStatement ass) {
			this.node = node;
			this.var = variable;
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
	
	private IControlFlowGraph cfg;
	
	public UselessAssignment(IControlFlowGraph cfg) {
		this.cfg = cfg;
	}	

	@Override
	public void analyse() {
		
		for (INode node : cfg.getNodes()) {
			if(node.getElement() instanceof IVariableAssignment) {
				IVariableAssignment assignment = (IVariableAssignment) node.getElement();
				boolean exists = false;
				for (Statement ass : uselessStatements) {
					if(ass.var.equals(assignment.getTarget())) {
						if(!included(cfg.pathsBetweenNodes(ass.node, node), ass.var)) 
							Linter.getInstance().register(new UselessVariableAssignment(ass.assignment));
							
						ass.assignment = assignment;
						ass.node = node;
						exists = true;
					} 
				}
				if(!exists) uselessStatements.add(new Statement(node, assignment.getTarget(), assignment));

				if(assignment.getTarget().toString().equals(assignment.getExpression().toString()))
					Linter.getInstance().register(new UselessSelfAssignment(Explanations.SELF_ASSIGNMENT, assignment));
				
			}
		}	
	}
	
	public boolean included(List<Path> paths, IVariableDeclaration variable) {
		if(paths.size() == 0) return true;
		for (Path path : paths) {
			
			INode first = path.getNodes().remove(0);
			INode last = path.getNodes().remove(path.getNodes().size() - 1);
			
			if(!((IBlockElement) first.getElement()).getParent().isSame(((IBlockElement) last.getElement()).getParent())) return true;
			
			for (INode node : path.getNodes()) {
				
				if(node.getElement() instanceof IArrayElementAssignment) {
					if(((IArrayElementAssignment) node.getElement()).getTarget().isSame(variable) || 
							((IArrayElementAssignment) node.getElement()).getExpression().includes(variable)) return true;
				}
				else if(node.getElement() instanceof IVariableAssignment) {
					if(((IVariableAssignment) node.getElement()).getTarget().isSame(variable) 
							|| ((IVariableAssignment) node.getElement()).getExpression().includes(variable)) return true;
				}
				else if(node.getElement() instanceof IProcedureCall) {
					for (IExpression argument : ((IProcedureCall) node.getElement()).getArguments()) {
						if(argument.includes(variable)) return true;
					}
				}
				else if((node.getElement() instanceof ISelection || node.getElement() instanceof ILoop) 
						&& ((IControlStructure) node.getElement()).getGuard().includes(variable)) {
					return true;
				}
			}
		}
		return false;
	}
}
