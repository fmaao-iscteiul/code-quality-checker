package pt.iscte.paddle.codequality.cases;

import java.util.EnumSet;
import java.util.Set;

import pt.iscte.paddle.javali.translator.ElementLocation;
import pt.iscte.paddle.model.IBlock;

public abstract class BadCodeCase {
	
	public enum CaseType { EMPTY_SELECTION, EMPTY_LOOP, DUPLICATE, DEAD_CODE, FAULTY_CHECK }
	public final Set<CaseType> caseTypes;
	public String explanation;
	public IBlock block;
	public ElementLocation BlockLocation;
	
	abstract static class Builder<T extends Builder<T>> {
		EnumSet<CaseType> caseTypes = EnumSet.noneOf(CaseType.class);
		public String explanation = new String("Enter explanation here.");
		public IBlock block = null;
		public ElementLocation blockLocation = null;
		
		public T addType(CaseType type) {
			caseTypes.add(type);
			return self();
		}
		
		public T setExplanation(String exp) {
			explanation = exp;
			return self();
		}
		
		public T setBlock(IBlock b) {
			this.block = b;
			return self();
		}
		
		public T setLocation(ElementLocation location) {
			this.blockLocation = location;
			return self();
		}
		
		abstract BadCodeCase build();
		
		protected abstract T self();
	}
	
	BadCodeCase(Builder<?> builder){
		caseTypes = builder.caseTypes.clone();
	}
	
	public String getExplanation() {
		return explanation;
	}

}
