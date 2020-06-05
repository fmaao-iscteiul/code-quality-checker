package pt.iscte.paddle.quality.visitors;

import java.util.ArrayList;

import pt.iscte.paddle.model.IBinaryExpression;
import pt.iscte.paddle.model.IBinaryOperator;
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

	ArrayList<ISelection> previousSelections = new ArrayList<ISelection>();
	
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
		previousSelections.add(selection);
		if(!hasParent) {
			duplicatesDeepSearch(selection).forEach(duplicate -> issues.add(duplicate));
		}
			
		return true;
	}

	public ArrayList<Duplicate> duplicatesDeepSearch(ISelection selection) {
		ArrayList<Duplicate> duplicates = new ArrayList<Duplicate>();
		if(!selection.hasAlternativeBlock()) return duplicates;

		for (IBlockElement c: selection.getBlock().getChildren()) {
			boolean canProceed = true;
			ArrayList<IProgramElement> occurrences = new ArrayList<IProgramElement>();
			for (IBlockElement rC : selection.getAlternativeBlock().getChildren()) {

				if(rC instanceof ISelection) {
					boolean exists2 = false;
					for (IBlockElement c2 : ((ISelection) rC).getBlock().getChildren()) {							
						if(c.isSame(c2)) {
							exists2 = true;
							if(!occurrences.contains(c)) occurrences.add(c);
							if(!occurrences.contains(c2)) occurrences.add(c2);
						}
					}
					if(!exists2) canProceed = false;
					if(((ISelection) rC).hasAlternativeBlock()) {
						boolean exists = false;
						for (IBlockElement c2 : ((ISelection) rC).getAlternativeBlock().getChildren()) {							
							if(c.isSame(c2)) {
								exists = true;
								if(!occurrences.contains(c)) occurrences.add(c);
								if(!occurrences.contains(c2)) occurrences.add(c2);
							}
						}
						if(!exists) canProceed = false;
					}
					if(occurrences.size() > 1)
						duplicatesDeepSearch((ISelection) rC);
				}
				else {
					if(c.isSame(rC)) {
						if(!occurrences.contains(c)) occurrences.add(c);
						if(!occurrences.contains(rC)) occurrences.add(rC);
					}
				}
			}
			if(!occurrences.isEmpty() && canProceed) duplicates.add(new Duplicate(occurrences)); 
		};
		return duplicates;
	}

	@Override
	public boolean visitAlternative(ISelection selection) {

		if(selection.getAlternativeBlock().isEmpty()) {
			issues.add(new EmptySelection(Explanations.EMPTY_SELECTION, selection.getAlternativeBlock()));
		}

		return true;
	}
}
