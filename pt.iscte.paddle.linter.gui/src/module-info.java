import pt.iscte.javardise.javaeditor.api.IJavardiseService;

module pt.iscte.paddle.linter.gui {
	requires pt.iscte.paddle.linter;
	requires org.eclipse.swt;
	requires pt.iscte.paddle.model;
	requires pt.iscte.paddle.model.tests;
	requires pt.iscte.javardise;
	requires pt.iscte.javardise.javaeditor;
	requires pt.iscte.paddle.model.javaparser;
	
	uses IJavardiseService;
}