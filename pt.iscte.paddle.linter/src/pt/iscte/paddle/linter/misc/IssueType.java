package pt.iscte.paddle.linter.misc;

public enum IssueType {
	
	EMPTY_BLOCK,   // empty blocks on IF, ELSE, or LOOP
	TAUTOLOGY, 
	CONTRADICTION,
	UNREACHABLE_CODE,
	
	/*
	 a = a;
	 */
	USELESS_SELFASSIGN,
	
	
	
	
	/*
	 if(bool == true)
	 */
	USELESS_COMPARISON,
	
	/*
	if(bool) return true;
	else return false;
	*/
	USELESS_BOOLEAN_MAP,
	
	/*
	 if(...) {
	 }
	 else {
	 	...
	 }
	 */
	USELESS_BRANCH,
	
	/*
	 copy(array);
	 */
	USELESS_CALL,
	
	/*
	 int a = 1;
	 int b = 0;
	 a = 2;
	 */
	USELESS_ASSIGN,
	
	/*
	 ...
	 return;
	 }
	 */
	USELESS_RETURN,
	
	/*
	 if(...) {
	 	...
	 	i++;
	 }
	 else {
	    ...
	    i++;
	 }
	 */
	REDUNDANT_BRANCHES,
	
	/*
	 while(i > 0) {
	 	if(i > 0) ...
	 }
	 */
	REDUNDANT_GUARD, 
	
	/*
	int m = max(list);
	list.remove(max(list));
	*/
	REDUNDANT_CALL,
	
	MAGIC_NUMBER,

	
}