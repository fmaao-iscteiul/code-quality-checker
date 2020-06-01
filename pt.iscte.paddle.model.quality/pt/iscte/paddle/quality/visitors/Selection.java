package pt.iscte.paddle.quality.visitors;

import java.util.ArrayList;

import pt.iscte.paddle.model.IBinaryExpression;
import pt.iscte.paddle.model.IBinaryOperator;
import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.model.IOperator.OperationType;
import pt.iscte.paddle.quality.cases.base.CodeAnalyser;
import pt.iscte.paddle.quality.cases.base.QualityIssue;
import pt.iscte.paddle.quality.client.Linter;
import pt.iscte.paddle.quality.issues.BooleanCheck;
import pt.iscte.paddle.quality.issues.Duplicate;
import pt.iscte.paddle.quality.issues.EmptySelection;
import pt.iscte.paddle.quality.issues.SelectionMisconception;
import pt.iscte.paddle.quality.misc.Explanations;
import pt.iscte.paddle.model.ISelection;
import pt.iscte.paddle.model.IType;

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

	@Override
	public boolean visit(ISelection selection) {
		if(selection.isEmpty()) {
			issues.add(new EmptySelection(Explanations.EMPTY_SELECTION, selection));

			if(selection.hasAlternativeBlock() && !selection.getAlternativeBlock().isEmpty()) {
				issues.add(new SelectionMisconception(Explanations.SELECTION_MISCONCEPTION, selection));
			}
		}
		ArrayList<IProgramElement> occurrences = new ArrayList<IProgramElement>();
		duplicatesDeepSearch(selection, occurrences);
		if(!occurrences.isEmpty()) issues.add(new Duplicate(occurrences));
		return true;
	}

	public void duplicatesDeepSearch(ISelection selection, ArrayList<IProgramElement> occurrences) {
		if(!selection.hasAlternativeBlock()) return;

		selection.getBlock().getChildren().forEach(c -> {
			selection.getAlternativeBlock().getChildren().forEach(rC -> {
				if(rC instanceof ISelection) {
					((ISelection) rC).getBlock().getChildren().forEach(c2 -> {
						if(c.isSame(c2)) {
							occurrences.add(c);
							occurrences.add(c2);
						}
					});
					if(((ISelection) rC).hasAlternativeBlock())
						((ISelection) rC).getAlternativeBlock().getChildren().forEach(c2 -> {
							if(c.isSame(c2)) {
								occurrences.add(c);
								occurrences.add(c2);
							}
						});
					duplicatesDeepSearch((ISelection) rC, occurrences);
				}
				else {
					if(c.isSame(rC)) {
						occurrences.add(c);
						occurrences.add(rC);
					}
				}
			});
		});
	}

	@Override
	public boolean visitAlternative(ISelection selection) {

		if(selection.getAlternativeBlock().isEmpty()) {
			issues.add(new EmptySelection(Explanations.EMPTY_SELECTION, selection.getAlternativeBlock()));
		}

		return true;
	}
}
