package pt.iscte.paddle.codequality.misc;

public final class Explanations {
	
	public final static String EMPTY_SELECTION = "The highlighted block represents a Selection block without any code written inside. This empty selection serves no use becase it generates no actions and damages the quality of the code base.";
	public final static String EMPTY_LOOP = "For other than iteration porpuses, an empty loop is an unnecessary chunk of code! It serves only tyhe porpuse of filling the code base"
			+ "with non-relevant chunks.";
	public final static String UNREACHABLE_CODE = "This return statement leads to the non-execution of the highlighted blocks of code. It's placement is most certainly wrong.";
	public final static String MAGIC_NUMBER = "A magic number is a numeric literal that is used in the middle of a block of code without explanation."
					+ " A number in isolation can be difficult for other programmers to understand. If this value is also duplicated, it becomes"
					+ "harder to update the code because it needs to be replaces in multiple places.";
	public final static String FAULTY_BOOLEAN_CHECK = "This selection represents the comparision between a boolean variable and one of it's binary possible values (true or false). "
			+ "In order to improve the quality of the written code, this comparision could be replaced with a simpler operation.";
	public final static String FAULTY_RETURN_BOOLEAN_CHECK = "";
	public final static String SELECTION_MISCONCEPTION = "";
	public static final String DUPLICATE_SELECTION_GUARD = "";
	public static final String SELF_ASSIGNMENT = "The highlighted variable was assigned to itself. This leads to the assignment of a value that"
			+ "the variable already had. With this being, it is considered an useless assignment.";
	public static final String DUPLICATE_BRANCH_CODE = "The highlighted statements are duplicated inside both the if and else selections. This"
			+ "logic could be simplified in order to reduce code duplicated and improve the code base quality.";
	public static final String FAULTY_METHOD_CALL = "faulty ndawjdjawndjawdjwanjdanj"; 
	
}
