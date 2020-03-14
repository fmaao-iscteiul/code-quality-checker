package pt.iscte.paddle.codequality.visitors;

import java.util.ArrayList;

import pt.iscte.paddle.codequality.cases.FaultyAssignment;
import pt.iscte.paddle.codequality.cases.MagicNumber;
import pt.iscte.paddle.codequality.linter.Linter;
import pt.iscte.paddle.codequality.misc.Explanations;
import pt.iscte.paddle.model.IArrayElementAssignment;
import pt.iscte.paddle.model.ILiteral;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.IBlock.IVisitor;

public class MagicNumbers implements IVisitor{

	public static MagicNumbers build() {
		return new MagicNumbers();
	}

	private ArrayList<MagicNumber> mNumbers = new ArrayList<MagicNumber>();;

	@Override
	public void visit(ILiteral exp) {
		boolean exists = false;
		if(exp.getType().isNumber() 
				&& !exp.isDecomposable()  
				&& Integer.parseInt(exp.getStringValue()) > 1) {

			if(mNumbers.isEmpty()) {
				MagicNumber mNumb = new MagicNumber(Explanations.MAGIC_NUMBER, exp.expression());
				mNumbers.add(mNumb);
				Linter.getInstance().register(mNumb);
			} 
			else {
				for (MagicNumber magicNumber : mNumbers) 

					if(magicNumber.getMagicNumber().isSame(exp)) {
						exists = true;
						magicNumber.addAssignment(exp);
					}

				if(!exists) {
					MagicNumber mNumb = new MagicNumber(Explanations.MAGIC_NUMBER, exp.expression());
					mNumbers.add(mNumb);
					Linter.getInstance().register(mNumb);
				}
			}
		}
	}

	@Override
	public boolean visit(IVariableAssignment assignment) {
		if(assignment.getTarget().toString().equals(assignment.getExpression().toString()))
			Linter.getInstance().register(new FaultyAssignment(Explanations.SELF_ASSIGNMENT, assignment));
		return true;
	}

	@Override
	public boolean visit(IArrayElementAssignment assignment) {
		if(assignment.getTarget().toString().equals(assignment.getExpression().toString()))
			Linter.getInstance().register(new FaultyAssignment(Explanations.SELF_ASSIGNMENT, assignment));
		return true;
	}
}
