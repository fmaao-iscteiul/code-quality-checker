package pt.iscte.paddle.linter.visitors;

import java.util.ArrayList;

import pt.iscte.paddle.linter.cases.base.CodeAnalyser;
import pt.iscte.paddle.linter.issues.MagicNumber;
import pt.iscte.paddle.linter.issues.UselessSelfAssignment;
import pt.iscte.paddle.linter.linter.Linter;
import pt.iscte.paddle.linter.misc.Explanations;
import pt.iscte.paddle.model.IArrayElementAssignment;
import pt.iscte.paddle.model.ILiteral;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IType;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.IBlock.IVisitor;

public class MagicNumbers extends CodeAnalyser implements IVisitor {

	private ArrayList<MagicNumber> mNumbers = new ArrayList<MagicNumber>();

	public MagicNumbers(IProcedure procedure) {
		super(procedure);
	}


	@Override
	public void visit(ILiteral exp) {
		boolean exists = false;
		if(exp.getType().isNumber() 
				&& !exp.isSame(IType.INT.literal(1)) && !exp.isSame(IType.INT.literal(0))
				&& !exp.isSame(IType.DOUBLE.literal(1.0)) && !exp.isSame(IType.DOUBLE.literal(0.0))) {

			for (MagicNumber magicNumber : mNumbers) {
				if(magicNumber.getOccurrences().get(0).isSame(exp) && 
						(getProcedure() == magicNumber.getProcedure() || magicNumber.getOccurrences().size() > 1)) {
					exists = true;
					magicNumber.addAssignment(exp);

					if(!issues.contains(magicNumber)) issues.add(magicNumber);
				}
			}

			if(!exists) {
				MagicNumber mNumb = new MagicNumber(getProcedure(), exp);
				mNumbers.add(mNumb);
			}
		}
	}
}
