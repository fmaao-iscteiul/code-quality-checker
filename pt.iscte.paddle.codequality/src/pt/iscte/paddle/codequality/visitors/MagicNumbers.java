package pt.iscte.paddle.codequality.visitors;

import java.util.ArrayList;

import pt.iscte.paddle.codequality.issues.MagicNumber;
import pt.iscte.paddle.codequality.issues.UselessSelfAssignment;
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


	private ArrayList<MagicNumber> mNumbers = new ArrayList<MagicNumber>();

	@Override
	public void visit(ILiteral exp) {
		boolean exists = false;
		if(exp.getType().isNumber() 
				&& exp.isSimple()
				&& Integer.parseInt(exp.getStringValue()) > 1) {

			for (MagicNumber magicNumber : mNumbers) 

				if(magicNumber.getMagicNumber().isSame(exp)) {
					exists = true;
					magicNumber.addAssignment(exp);
					if(magicNumber.getOccurrences().size() == 2) 
						Linter.getInstance().register(magicNumber);
				}

			if(!exists) {
				MagicNumber mNumb = new MagicNumber(Explanations.MAGIC_NUMBER, exp);
				mNumbers.add(mNumb);
			}
		}
	}
}
