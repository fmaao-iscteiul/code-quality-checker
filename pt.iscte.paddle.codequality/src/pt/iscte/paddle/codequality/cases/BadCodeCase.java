package pt.iscte.paddle.codequality.cases;

import java.util.EnumSet;
import java.util.Set;

import pt.iscte.paddle.javali.translator.ElementLocation;
import pt.iscte.paddle.model.IBlock;

public abstract class BadCodeCase {
	
	public enum Category { EMPTY_SELECTION, EMPTY_ALTERNATIVE, EMPTY_LOOP, DUPLICATE, DEAD_CODE, FAULTY_BOOLEAN_CHECK }
	public final Set<Category> categories;
	public String explanation;
	public IBlock block;
	public ElementLocation blockLocation;
	
	public abstract static class Builder<T extends Builder<T>> {
		EnumSet<Category> caseTypes = EnumSet.noneOf(Category.class);
		public String explanation = "Enter explanation here.";
		public IBlock block = null;
		public ElementLocation blockLocation = null;
		
		public T addCategory(Category type) {
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
		categories = builder.caseTypes.clone();
		explanation = builder.explanation;
		block = builder.block;
		blockLocation = builder.blockLocation;
	}
	
	public String getExplanation() {
		return explanation;
	}
	
	public ElementLocation getBlockLocation() {
		return blockLocation;
	}
	
	public Set<Category> getCaseTypes() {
		return categories;
	}
	

}
