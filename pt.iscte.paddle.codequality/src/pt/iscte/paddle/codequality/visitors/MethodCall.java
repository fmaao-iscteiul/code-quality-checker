package pt.iscte.paddle.codequality.visitors;

import pt.iscte.paddle.codequality.Icfg.IControlFlowGraphBuilder;
import pt.iscte.paddle.codequality.cases.FaultyMethodCall;
import pt.iscte.paddle.codequality.linter.Linter;
import pt.iscte.paddle.codequality.misc.BadCodeAnalyser;
import pt.iscte.paddle.codequality.misc.Explanations;
import pt.iscte.paddle.model.IBinaryExpression;
import pt.iscte.paddle.model.IProcedureCall;
import pt.iscte.paddle.model.IType;
import pt.iscte.paddle.model.IUnaryExpression;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.IBlock.IVisitor;

public class MethodCall implements BadCodeAnalyser, IVisitor {

	private IControlFlowGraphBuilder cfg;
	
	public MethodCall(IControlFlowGraphBuilder cfg) {
		this.cfg = cfg;
	}

	public MethodCall() {
	}
	
	public static MethodCall build(IControlFlowGraphBuilder cfg) {
		return new MethodCall(cfg);
	}
	
	public static MethodCall build() {
		return new MethodCall();
	}
		
	@Override
	public boolean visit(IUnaryExpression exp) {
		System.out.println("unaryExpression: " + exp);
		return IVisitor.super.visit(exp);
	}
	
	@Override
	public boolean visit(IVariableAssignment assignment) {
//		System.out.println("IVariableAssignment: " + (assignment.getExpression() instanceof IProcedureCall));
		return IVisitor.super.visit(assignment);
	}
	
	@Override
	public boolean visit(IProcedureCall call) {
		System.out.println("IProcedureCall: " + call.getParts());
		if(call instanceof IBinaryExpression) System.out.println("ndwadnwanjkdwnjkda");
		return IVisitor.super.visit(call);
	}

	@Override
	public void analyse() {
		cfg.getCFG().getNodes().forEach(node -> {
			if(node != null && node.getElement() instanceof IProcedureCall && !((IProcedureCall) node.getElement()).getType().equals(IType.VOID)) {
				Linter.getInstance().register(new FaultyMethodCall(Explanations.FAULTY_METHOD_CALL, node.getElement()));
			}
				
		});
	}
}
