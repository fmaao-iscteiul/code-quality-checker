package pt.iscte.paddle.codequality.misc;

public class CaseNames {

	public static String getCaseName(Category category) {
		switch (category) {
		case DUPLICATE_CODE:
			return "Duplicated statement";
		case DUPLICATE_PROCEDURE_CALL:
			return "Duplicated method call";
		case DUPLICATE_SELECTION_GUARD:
			return "Duplicated condition";
		case EMPTY_ALTERNATIVE:
			return "Empty else block";
		case EMPTY_LOOP:
			return "Empty loop block";
		case EMPTY_SELECTION:
			return "Empty if block";
		case FAULTY_ASSIGNMENT:
			return "Useless code";
		case FAULTY_BOOLEAN_CHECK:
			return "Boolean condition verbose";
		case FAULTY_METHOD_CALL:
			return "Method call issue";
		case FAULTY_RETURN_BOOLEAN_CHECK:
			return "Unnecessary boolean condition";
		case MAGIC_NUMBER:
			return "Magic number";
		case NESTED_CODE:
			return "Nested code";
		case SELECTION_MISCONCEPTION:
			return "If condition misconception";
		case UNREACHABLE_CODE:
			return "Unreachable code";
		case TALTOLOGY:
			return "Tautology";
		case CONTRADICTION:
			return "Dead Code";
		default:
			return category.toString();
		}
	}

}
