package pt.iscte.paddle.quality.visitors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pt.iscte.paddle.model.IBinaryExpression;
import pt.iscte.paddle.model.IBinaryOperator;
import pt.iscte.paddle.model.IBlock;
import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.model.IBlockElement;
import pt.iscte.paddle.model.IOperator.OperationType;
import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.model.ISelection;
import pt.iscte.paddle.model.IType;
import pt.iscte.paddle.quality.cases.base.CodeAnalyser;
import pt.iscte.paddle.quality.issues.BooleanCheck;
import pt.iscte.paddle.quality.issues.Duplicate;
import pt.iscte.paddle.quality.issues.EmptySelection;
import pt.iscte.paddle.quality.issues.SelectionMisconception;
import pt.iscte.paddle.quality.misc.Explanations;

public class Selection extends CodeAnalyser implements IVisitor {


	@Override
	public boolean visit(IBinaryExpression exp) {
		if(exp.getOperationType().equals(OperationType.RELATIONAL) 
				&& (exp.getOperator().equals(IBinaryOperator.EQUAL) || exp.getOperator().equals(IBinaryOperator.DIFFERENT))
				&& exp.getLeftOperand().getType().isBoolean() 
				&& exp.getRightOperand().getType().isBoolean()
				&& ((exp.getRightOperand().isSame(IType.BOOLEAN.literal(false)) || exp.getRightOperand().isSame(IType.BOOLEAN.literal(true)))
						||  (exp.getLeftOperand().isSame(IType.BOOLEAN.literal(false)) || exp.getLeftOperand().isSame(IType.BOOLEAN.literal(true))))) {
			issues.add(new BooleanCheck(Explanations.FAULTY_BOOLEAN_CHECK, exp));
		}

		return true;
	}

	HashSet<ISelection> previousSelections = new HashSet<ISelection>();

	@Override
	public boolean visit(ISelection selection) {

		if(selection.isEmpty()) {
			issues.add(new EmptySelection(Explanations.EMPTY_SELECTION, selection));

			if(selection.hasAlternativeBlock() && !selection.getAlternativeBlock().isEmpty()) {
				issues.add(new SelectionMisconception(Explanations.SELECTION_MISCONCEPTION, selection));
			}
		}

		boolean hasParent = false;
		for (ISelection sel : previousSelections) {
			if(selection.getParent().isSame(sel) 
					|| selection.getParent().isSame(sel.getAlternativeBlock())) hasParent = true;
		}

		if(!hasParent && selection.hasAlternativeBlock()) {
			ArrayList<Duplicate> duplicates = new ArrayList<Duplicate>();
			for (IBlockElement sElement: this.getRawChildren(selection.getBlock())) {
				ArrayList<IProgramElement> occurrences = new ArrayList<IProgramElement>(Arrays.asList(sElement));
				HashSet<IProgramElement> occurrencesFound = duplicatesDeepSearch(selection.getAlternativeBlock(), sElement);
				if(occurrencesFound == null) continue;
				else if(!occurrencesFound.isEmpty()) occurrences.addAll(occurrencesFound);
				if(occurrences.size() > 1) 
					duplicates.add(new Duplicate(occurrences));
			};
			issues.addAll(duplicates);
		}
		previousSelections.add(selection);
		return true;
	}

	public HashSet<IProgramElement> duplicatesDeepSearch(IBlock block, IProgramElement element) {

		HashSet<IProgramElement> occurrences = new HashSet<IProgramElement>();
		List<IBlockElement> alternativeElements = this.getRawChildren(block.getBlock());
		if(alternativeElements.isEmpty() || alternativeElements.size() > 1) return null;

		IBlockElement child = alternativeElements.get(0);
		boolean found = false;
		if(child instanceof ISelection 
				&& ((ISelection) child).hasAlternativeBlock()
				&& !((ISelection) child).getAlternativeBlock().isEmpty()) {

			for(IBlockElement aElement: ((ISelection) child).getBlock().getChildren()) {
				if(aElement.isSame(element)) {
					found = true;
					occurrences.add(aElement);
				}
			}
			HashSet<IProgramElement> occ = duplicatesDeepSearch(((ISelection) child).getAlternativeBlock(), element);
			if(occ != null && !occ.isEmpty()) occurrences.addAll(occ);
			else return null;
		} else {
			for(IBlockElement aElement: alternativeElements) {
				if(aElement.isSame(element)) {
					found = true;
					occurrences.add(aElement);
				}
			}
		}

		return found && occurrences.size() > 0 ? occurrences : null;
	}

	private List<IBlockElement> getRawChildren(IBlock block) {
		ArrayList<IBlockElement> children = new ArrayList<IBlockElement>();
		if(block == null || block != null && block.getChildren().size() == 0) return children;

		for(IBlockElement el: block.getBlock().getChildren()) {
			if(el instanceof IBlock) children.addAll(getRawChildren((IBlock) el));
			else {
				boolean contains = false;
				for(IBlockElement el2: children) {
					if(el.isSame(el2)) contains = true;
				}
				if(!contains) children.add(el);
			}
		}
		return children;
	}


	@Override
	public boolean visitAlternative(ISelection selection) {

		if(selection.getAlternativeBlock().isEmpty()) {
			issues.add(new EmptySelection(Explanations.EMPTY_SELECTION, selection.getAlternativeBlock()));
		}

		return true;
	}
}
