package pt.iscte.paddle.codequality.visitors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import pt.iscte.paddle.codequality.issues.DuplicateMethodCall;
import pt.iscte.paddle.codequality.issues.FaultyProcedureCall;
import pt.iscte.paddle.codequality.linter.Linter;
import pt.iscte.paddle.codequality.misc.BadCodeAnalyser;
import pt.iscte.paddle.model.IExpression;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IProcedureCall;
import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.model.IType;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.IVariableDeclaration;
import pt.iscte.paddle.model.cfg.IControlFlowGraph;
import pt.iscte.paddle.model.cfg.IControlFlowGraph.Path;
import pt.iscte.paddle.model.cfg.INode;
import pt.iscte.paddle.model.roles.IFunctionClassifier.Status;
import pt.iscte.paddle.model.roles.impl.FixedValue;
import pt.iscte.paddle.model.roles.impl.FunctionClassifier;

public class ProcedureCall implements BadCodeAnalyser {

	private ArrayList<INode> calls;
	private ArrayList<DuplicateProcedureCall> duplicates;

	private IControlFlowGraph cfg;

	public ProcedureCall(IControlFlowGraph cfg) {
		this.calls = new ArrayList<INode>();
		this.duplicates = new ArrayList<DuplicateProcedureCall>();
		this.cfg = cfg;
	}

	public static ProcedureCall build(IControlFlowGraph cfg) {
		return new ProcedureCall(cfg);
	}

	class DuplicateProcedureCall {
		private ArrayList<INode> occurences;
		private Set<INode> realDuplicates;
		private INode occ;

		public DuplicateProcedureCall(INode node) {
			this.occurences = new ArrayList<INode>();
			this.realDuplicates= new HashSet<INode>();
			this.occurences.add(node);
			this.occ = node;
		}
		@Override
		public String toString() {
			return occ.toString();
		}
	}

	@Override
	public void analyse() {
		this.gatherProcedureCallDuplicates();
		this.pairGatheredDuplicates();

	}

	private void pairGatheredDuplicates() {
		for (DuplicateProcedureCall duplicateBranchGuard : duplicates) {
			for(int i = 1; i < duplicateBranchGuard.occurences.size(); i++) {
				INode start = duplicateBranchGuard.occurences.get(i - 1);
				INode end = duplicateBranchGuard.occurences.get(i);

				duplicateBranchGuard.realDuplicates.add(start);
				duplicateBranchGuard.realDuplicates.add(end);

				if(hasBeenChanged(cfg, start, end)) duplicateBranchGuard.realDuplicates.remove(start);

			}
			if(duplicateBranchGuard.realDuplicates.size() > 1 
					&& !new FunctionClassifier((IProcedure) ((IProcedureCall) duplicateBranchGuard.occ.getElement()).getProcedure()).getClassification().equals(Status.PROCEDURE)) {
				ArrayList<IProgramElement> occurrences = new ArrayList<IProgramElement>();
				duplicateBranchGuard.realDuplicates.forEach(d -> occurrences.add(d.getElement()));
				Linter.getInstance().register(new DuplicateMethodCall(occurrences));			
			}

		}
	}

	public static boolean hasBeenChanged(IControlFlowGraph cfg, INode start, INode end) {
		IProcedureCall call = (IProcedureCall) start.getElement();
		List<Path> paths = cfg.pathsBetweenNodes(start, end);

		for (Path path : paths) {
			List<INode> pathNodes = path.getNodes();
			if(pathNodes.size() > 2) {
				pathNodes.remove(0);
				pathNodes.remove(pathNodes.size() - 1);
				for (INode node : pathNodes) {
					if(node.getElement() != null && node.getElement() instanceof IVariableAssignment) {
						for (IExpression argument : call.getArguments()) {
							if(((IVariableAssignment) node.getElement()).getTarget().expression().isSame(argument))
								return true;
						}
					}
				}
			}
		}
		return false;
	}

	private void gatherProcedureCallDuplicates() {
		for (INode node : cfg.getNodes()) {
			if(node.getElement() != null && node.getElement() instanceof IProcedureCall) {
				IProcedureCall call = (IProcedureCall) node.getElement();

				if(!call.getProcedure().getReturnType().equals(IType.VOID)) {
					String explanation = "This method call is being used as if the method was void. The " + call.getProcedure().longSignature() + " method that was called returns the type: " 
							+ call.getProcedure().getReturnType() + ". Non void methods should not be called as void ones because it's return value can be relevant.";
					Linter.getInstance().register(new FaultyProcedureCall(explanation, call));
				}

				for (IVariableDeclaration var : call.getProcedure().getParameters()) {
					if(new FunctionClassifier((IProcedure) call.getProcedure()).getClassification().equals(Status.PROCEDURE) 
							|| !FixedValue.isFixedValue(var)) continue;
				}

				for(INode proc: calls) {
					if(call.isSame(proc.getElement())) {

						boolean occExists = false;
						for (DuplicateProcedureCall dup : duplicates) {
							if(dup.occ.getElement().isSame(call)) {
								dup.occurences.add(node);
								occExists = true;
								break;
							}
						}
						if(!occExists) {
							DuplicateProcedureCall dup = new DuplicateProcedureCall(proc);
							dup.occurences.add(node);
							duplicates.add(dup);
						}	

						break;
					}
				}
				calls.add(node);			

			}
		}
	}

}
