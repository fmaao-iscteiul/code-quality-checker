module pt.iscte.paddle.linter {
	requires pt.iscte.paddle.model;
	requires pt.iscte.paddle.model.tests;
	requires pt.iscte.paddle.model.roles;
	requires junit;
	
	exports pt.iscte.paddle.linter.cases.base;
	exports pt.iscte.paddle.linter.examples;
	exports pt.iscte.paddle.linter.linter;
	exports pt.iscte.paddle.linter.misc;
	exports pt.iscte.paddle.linter.visitors;
	exports pt.iscte.paddle.linter.issues;
	
	
	opens pt.iscte.paddle.linter.examples;
	
}