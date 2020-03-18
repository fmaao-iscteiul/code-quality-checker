package pt.iscte.paddle.codequality.misc;

public enum Category {
	FAULTY_ASSIGNMENT,
	SELECTION_MISCONCEPTION,
	EMPTY_SELECTION,
	EMPTY_ALTERNATIVE,
	EMPTY_LOOP,
	DUPLICATE_CODE,
	UNREACHABLE_CODE,
	NESTED_CODE,
	FAULTY_BOOLEAN_CHECK,
	FAULTY_RETURN_BOOLEAN_CHECK,
	MAGIC_NUMBER,
	DUPLICATE_SELECTION_GUARD, 
	FAULTY_METHOD_CALL
}


//static String generateCategoryTitle(Category cat) {
//	switch (cat) {
//	case FAULTY_ASSIGNMENT: {
//		return 'FAULTY_ASSIGNMENT'
//	}
//	default:
//		throw new IllegalArgumentException("Unexpected value: " + cat);
//	}
//}