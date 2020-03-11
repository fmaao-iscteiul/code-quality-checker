package pt.iscte.paddle.codequality.visitors;

import pt.iscte.paddle.model.cfg.IBranchNode;
import pt.iscte.paddle.model.cfg.INode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.iscte.paddle.codequality.Icfg.IControlFlowGraphBuilder;
import pt.iscte.paddle.codequality.cases.Duplicate;
import pt.iscte.paddle.codequality.cases.MagicNumber;
import pt.iscte.paddle.codequality.linter.Linter;
import pt.iscte.paddle.model.IArrayElementAssignment;
import pt.iscte.paddle.model.IExpression;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.IVariableDeclaration;
import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.codequality.misc.BadCodeAnalyser;

public class DuplicateStatement implements BadCodeAnalyser, IVisitor{

	private IControlFlowGraphBuilder cfgBuilder;

	private Map<IVariableDeclaration, IExpression> variableAssignments = new HashMap<IVariableDeclaration, IExpression>();
	private Map<IVariableDeclaration, IExpression> arrayElementAssignments = new HashMap<IVariableDeclaration, IExpression>();
	
	private Duplicate duplicateCase = null;
	
	private DuplicateStatement(IControlFlowGraphBuilder cfgBuilder) {
		this.cfgBuilder = cfgBuilder;
	}

	public static DuplicateStatement build(IControlFlowGraphBuilder cfgBuilder) {
		return new DuplicateStatement(cfgBuilder);
	}

	@Override
	public void analyse() {
		for(INode node : this.cfgBuilder.getCFG().getNodes()) {
			List<INode> stack = new ArrayList<INode>();
			List<INode> duplicates = new ArrayList<INode>();
			for (INode incoming : node.getIncomming()) {
				if((incoming instanceof IBranchNode)) continue;
				else if(duplicates.size() == 0) {
					duplicates.add(incoming);
					stack.add(incoming);
				}
				else if(stack.contains(incoming)) {
					duplicates.add(incoming);
				} 
				else stack.add(incoming);
			}
			if(duplicates.size() > 1) Linter.getInstance().register(new Duplicate("", duplicates));
		};
	}
	
	@Override
	public boolean visit(IArrayElementAssignment assignment) {
		System.out.println(assignment.getTarget());
		return IVisitor.super.visit(assignment);
	}
	
	@Override
	public boolean visit(IVariableAssignment assignment) {
		System.out.println("IVariableAssignment: " + assignment);
		return IVisitor.super.visit(assignment);
	}
	
//	@Override
//	public boolean visit(IVariableAssignment assignment) {
//		if(variableAssignments.containsKey(assignment.getTarget())
//				&& variableAssignments.get(assignment.getTarget()).equals(assignment.getExpression())) {
//			if(duplicateCase == null) {
//				duplicateCase = new Duplicate("dwadwa", assignment);
//				Linter.getInstance().register(duplicateCase);
//			} else duplicateCase.addAssignment(assignment);
//			
//		} else variableAssignments.put(assignment.getTarget(), assignment.getExpression());
//		return IVisitor.super.visit(assignment);
//	}
}
