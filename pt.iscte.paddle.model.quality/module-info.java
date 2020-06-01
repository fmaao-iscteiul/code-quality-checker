module pt.iscte.paddle.model.quality {
	requires transitive pt.iscte.paddle.model;
	requires transitive pt.iscte.paddle.model.tests;
	requires pt.iscte.paddle.model.javaparser;
	requires pt.iscte.paddle.model.roles;
	requires junit;
	
	exports pt.iscte.paddle.quality.client;
	exports pt.iscte.paddle.quality.visitors;
	opens pt.iscte.paddle.quality.examples;
	exports pt.iscte.paddle.quality.examples;
	exports pt.iscte.paddle.quality.cases.base;
	exports pt.iscte.paddle.quality.misc;
	exports pt.iscte.paddle.quality.issues;

}