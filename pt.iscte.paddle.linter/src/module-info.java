module pt.iscte.paddle.linter {
	requires pt.iscte.paddle.model;
	requires pt.iscte.paddle.model.roles;
	//requires junit;
	requires pt.iscte.paddle.model.javaparser;
	exports pt.iscte.paddle.linter.cases.base;
	exports pt.iscte.paddle.linter.linter;
	exports pt.iscte.paddle.linter.misc;
	exports pt.iscte.paddle.linter.visitors;
	exports pt.iscte.paddle.linter.issues;
	
}