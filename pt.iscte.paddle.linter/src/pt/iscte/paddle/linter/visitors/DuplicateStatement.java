package pt.iscte.paddle.linter.visitors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import pt.iscte.paddle.linter.cases.base.CodeAnalyser;
import pt.iscte.paddle.linter.cases.base.QualityIssue;
import pt.iscte.paddle.linter.issues.Duplicate;
import pt.iscte.paddle.linter.linter.Linter;
import pt.iscte.paddle.linter.misc.BadCodeAnalyser;
import pt.iscte.paddle.model.IBlock;
import pt.iscte.paddle.model.IBlockElement;
import pt.iscte.paddle.model.IControlStructure;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.model.ISelection;
import pt.iscte.paddle.model.IStatement;
import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.model.cfg.IBranchNode;
import pt.iscte.paddle.model.cfg.IControlFlowGraph;
import pt.iscte.paddle.model.cfg.INode;

public class DuplicateStatement extends CodeAnalyser  implements IBlock.IVisitor { //implements BadCodeAnalyser,{

	public DuplicateStatement(IProcedure procedure) {
		super(procedure);
	}

//	@Override
//	public void analyse(IControlFlowGraph cfg) {
//		for (INode graphNode : cfg.getNodes()) {
//			IProgramElement e = graphNode.getElement();
//			if(graphNode instanceof IBranchNode) {
//				e = ((IBranchNode) graphNode).getElement().getProperty(IControlStructure.class);
//			}
//			
//			
//			if(e instanceof IBlockElement) {
//				IBlock block = (IBlock)((IBlockElement) e).getParent();
//				IBlockElement prev = block.getPrevious((IBlockElement) e);
//				if(prev instanceof ISelection) {
//					Set<INode> incomming = graphNode.getIncomming();
//					if (incomming.size() > 1) {
//						boolean allEqual = true;
//						Iterator<INode> iter = graphNode.getIncomming().iterator();
//						INode a = iter.next();
//						while (iter.hasNext() && allEqual) {
//							INode b = iter.next();
//							if (!a.getElement().isSame(b.getElement()))
//								allEqual = false;
//						}
//						if (allEqual) {
//							List<IProgramElement> dups = new ArrayList<>();
//							graphNode.getIncomming().forEach(n -> dups.add(n.getElement()));
//							issues.add(new Duplicate(getProcedure(), dups));
//						}
//					}
//				}
//			}
//			
//
////			Set<INode> duplicates = new HashSet<INode>();
////			for(INode incomingNode: graphNode.getIncomming()) {
////				
////				dance:
////				for(INode anotherIncoming: graphNode.getIncomming()) {
////					if(anotherIncoming != null && incomingNode != null && graphNode != null && anotherIncoming.getElement() != null && incomingNode.getElement() != null 
////							&& !graphNode.isExit() && !anotherIncoming.equals(incomingNode)
////
////							&& anotherIncoming.getElement().isSame(incomingNode.getElement()))
////						duplicates.add(anotherIncoming);
////					else break dance;
////				}
////			}
////
////			if(duplicates.size() > 1) {
////				ArrayList<IProgramElement> occurrences = new ArrayList<IProgramElement>();
////				duplicates.forEach(d -> occurrences.add(d.getElement()));
////				issues.add(new Duplicate(getProcedure(), occurrences));
////			}
//		}
//	}
	
	@Override
	public boolean visit(ISelection selection) {
		if(selection.hasAlternativeBlock()) {
			IBlock if_ = selection.getBlock();
			IBlock else_ = selection.getAlternativeBlock().getBlock();
			if(!if_.isEmpty() && !else_.isEmpty() && if_.getFirst().isSame(else_.getFirst()))
				issues.add(new Duplicate(getProcedure(), List.of(if_.getFirst(), else_.getFirst())));
			
			if(if_.getSize() > 1 && !else_.isEmpty() || !if_.isEmpty() && else_.getSize() > 1) {
				if(if_.getLast().isSame(else_.getLast())) {
					issues.add(new Duplicate(getProcedure(), List.of(if_.getLast(), else_.getLast())));
				}
			}
		}
		return true;
	}

}
