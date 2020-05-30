package pt.iscte.paddle.quality.misc;

public final class Explanations {
	
	public final static String EMPTY_SELECTION = "The highlight reporesents a block of without any code. This empty selection serves no use because it generates no actions and damages the quality of the code base.";
	
	public final static String EMPTY_LOOP = "The highlight reporesents a block of without any code. For other than iteration porpuses, an empty loop is an unnecessary chunk of code! It serves only tyhe porpuse of filling the code base"
			+ "with non-relevant chunks.";
	
	public final static String UNREACHABLE_CODE = "There is a return statement that causes the non-execution of the highlighted blocks of code. Such code will never"
			+ "be executed and becomes obsolete, unnecessary and therefore should be avoided at all cost";
	
	public final static String MAGIC_NUMBER = "A magic number is a numeric literal that is used in the middle of a block of code without any kind of explanation."
					+ " A number in isolation can be difficult for other programmers to understand because it provides no context. If this value is also duplicated, it becomes"
					+ " harder to update the code because it needs to be replaced in multiple places.";
	
	public final static String FAULTY_BOOLEAN_CHECK = "This selection represents the comparision between a boolean variable and one of it's binary possible values (true or false). "
			+ "In order to improve the quality of the written code, this comparision could be replaced with a simpler operation.";
	public final static String FAULTY_RETURN_BOOLEAN_CHECK = "";
	public final static String SELECTION_MISCONCEPTION = "The code was written in the else block and the if left empty because the"
			+ " target condition was the opposite written in the if guard. This demonstrates a misconception when in comes to handling conditions."
			+ "A negative condition can be used to fight these empty blocks of code.";
	public static final String DUPLICATE_SELECTION_GUARD = "The highlighted selections are duplicates! The state of the variables used in the conditions hasn't change in between them. With this being, these multiple"
			+ "selection guards server no porpuse.";
	public static final String SELF_ASSIGNMENT = "The highlighted variable was assigned to itself. This leads to the assignment of a value that"
			+ " the variable already had. With this being, it is considered an useless assignment.";
	public static final String DUPLICATE_BRANCH_CODE = "The highlighted statements are duplicated. This"
			+ "logic could be simplified in order to remove code duplication and improve the code base quality.";
	public static final String FAULTY_PROCEDURE_CALL = "This method call is being used as if the method was void. The method that was called returns the type: ";
	public static final String DUPLICATE_STATEMENT = "This statement is duplicated in this method! In order to avoid this situations, it could be extracted to a new method and called instead.";
	public static final String TAUTOLOGY = "The statement is classified as a tautology because it will allways be true.";
	public static final String CONTRADICTION = "The statement is classified as a contradiction because it will allways be false.";
	public static final String DUPLICATE_METHOD_CALL = "The method call is duplicated. It makes the code more difficult to understand, the method parameters"
			+ "aren't even changed by the method call, which can lead to multiple calls which have the same result.";
}
