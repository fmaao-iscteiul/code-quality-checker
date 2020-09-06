package pt.iscte.paddle.quality.visitors;

import java.util.ArrayList;

import pt.iscte.paddle.model.IArrayElementAssignment;
import pt.iscte.paddle.model.ILiteral;
import pt.iscte.paddle.model.IType;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.quality.cases.base.CodeAnalyser;
import pt.iscte.paddle.quality.client.Linter;
import pt.iscte.paddle.quality.issues.MagicNumber;
import pt.iscte.paddle.quality.issues.UselessSelfAssignment;
import pt.iscte.paddle.quality.misc.Explanations;

public class MagicNumbers extends CodeAnalyser implements IVisitor {

	private ArrayList<MagicNumber> mNumbers = new ArrayList<MagicNumber>();
	
	@Override
	public void visit(ILiteral exp) {
		boolean exists = false;
		if(exp.getType().isNumber() && !exp.isSame(IType.INT.literal(2))
				&& !exp.isSame(IType.INT.literal(1)) && !exp.isSame(IType.INT.literal(0))
				&& !exp.isSame(IType.DOUBLE.literal(1.0)) && !exp.isSame(IType.DOUBLE.literal(0.0))) {
			
			for (MagicNumber magicNumber : mNumbers) {
				
				if(magicNumber.getOccurences().get(0).isSame(exp)) {
					exists = true;
					magicNumber.addAssignment(exp);
					if(!issues.contains(magicNumber))
						{
						issues.add(magicNumber);
						}
				}
			}

			if(!exists) {
				MagicNumber mNumb = new MagicNumber(Explanations.MAGIC_NUMBER, exp);
				mNumbers.add(mNumb);
			}
		}
	}
}
