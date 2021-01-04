package pt.iscte.paddle.linter.misc;

public class CaseNames {

	public static String getCaseName(IssueType category) {
		switch (category) {
		case REDUNDANT_BRANCHES:
			return "Duplicated statement";
		case REDUNDANT_CALL:
			return "Duplicated method call";
		case REDUNDANT_GUARD:
			return "Duplicated condition";
		case EMPTY_BLOCK:
			return "Empty if block";
		case USELESS_SELFASSIGN:
			return "Useless code";
		case USELESS_COMPARISON:
			return "Boolean condition verbose";
		case USELESS_CALL:
			return "Method call issue";
		case USELESS_BOOLEAN_MAP:
			return "Unnecessary boolean condition";
		case MAGIC_NUMBER:
			return "Magic number";
		case USELESS_BRANCH:
			return "If condition misconception";
		case UNREACHABLE_CODE:
			return "Unreachable code";
		case TAUTOLOGY:
			return "Tautology";
		case CONTRADICTION:
			return "Dead Code";
		case USELESS_ASSIGN:
			return "Unused assignment value";
		default:
			return category.toString();
		}
	}

}
