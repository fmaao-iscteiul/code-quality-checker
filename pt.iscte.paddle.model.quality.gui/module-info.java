module pt.iscte.paddle.model.quality.gui {
	requires pt.iscte.paddle.model.quality;
	requires pt.iscte.paddle.model.tests;
	requires pt.iscte.paddle.model;
	requires pt.iscte.paddle.javardise;
	requires pt.iscte.paddle.model.javaparser;
	requires org.eclipse.swt;
	
	exports component;
	exports main;
	exports issues;
	
	uses pt.iscte.paddle.javardise.service.IJavardiseService;
	
}