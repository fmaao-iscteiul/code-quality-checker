module pt.iscte.paddle.model.quality {
	requires pt.iscte.paddle.model;
	requires pt.iscte.paddle.model.tests;
	requires pt.iscte.paddle.model.javaparser;
	requires pt.iscte.paddle.model.roles;
	requires junit;
	
	exports pt.iscte.paddle.quality.client;
	exports pt.iscte.paddle.quality.visitors;
}