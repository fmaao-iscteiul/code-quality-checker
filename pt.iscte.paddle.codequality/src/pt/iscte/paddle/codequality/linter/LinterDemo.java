package pt.iscte.paddle.codequality.linter;


import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import pt.iscte.paddle.codequality.cases.BadCodeCase;
import pt.iscte.paddle.codequality.misc.CaseNames;
import pt.iscte.paddle.codequality.tests.linter.DuplicateLoopGuard;
import pt.iscte.paddle.codequality.tests.linter.FaultyReturns;
import pt.iscte.paddle.codequality.tests.linter.NonVoidPureFunction;
import pt.iscte.paddle.codequality.tests.linter.SelectionMisconception;
import pt.iscte.paddle.codequality.tests.linter.UnreachableCode;
import pt.iscte.paddle.javardise.service.IClassWidget;
import pt.iscte.paddle.javardise.service.IJavardiseService;
import pt.iscte.paddle.javardise.util.HyperlinkedText;
import pt.iscte.paddle.model.IModule;
import pt.iscte.pidesco.cfgviewer.ext.CFGViewer;

public class LinterDemo {

	private static Shell shell;

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {

		//				UnreachableCode t = new UnreachableCode();
		//		NonVoidPureFunction t = new NonVoidPureFunction();
		SelectionMisconception t = new SelectionMisconception();
//				DuplicateLoopGuard t = new DuplicateLoopGuard();
		//		DuplicateStatement t = new DuplicateStatement();
//						FaultyReturns t = new FaultyReturns();

		t.setup();
		IModule module = t.getModule();

		Display display = new Display();
		shell = new Shell(display);
		shell.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		FillLayout layout = new FillLayout();
		layout.marginHeight = 5;
		layout.marginWidth = 5;
		shell.setLayout(layout);

		Composite outer = new Composite(shell, SWT.BORDER);
		FillLayout outerLayout = new FillLayout();
		outerLayout.marginHeight = 5;
		outerLayout.marginWidth = 5;
		outerLayout.spacing = 5;
		outer.setLayout(outerLayout);

		//		Composite widgetComp = new Composite(outer, SWT.V_SCROLL);
		//		widgetComp.setLayout(new FillLayout());

		Composite codeAndCFG = new Composite(outer, SWT.NONE);
		codeAndCFG.setLayout(new GridLayout(1, true));

		IClassWidget widget = IJavardiseService.createClassWidget(codeAndCFG, module);
		widget.setReadOnly(true);

		Composite rightComp = new Composite(outer, SWT.BORDER);
		FillLayout rightLayout = new FillLayout(SWT.VERTICAL);
		rightLayout.marginHeight = 5;
		rightLayout.marginWidth = 5;
		rightLayout.spacing = 5;
		rightComp.setLayout(rightLayout);

		Label label = new Label(rightComp, SWT.WRAP | SWT.CENTER);
		label.setText("CODE QUALITY CHECKER \n\n\n"
				+ "This tool was designed to help you improve your code base, when it comes code quality!"
				+ " It will detect and highlight many code quality related issues and alert you about them, including a brief explanation"
				+ "regarding the reason why they should be avoid. With this being, this tool will help you in eliminating bad practises in an "
				+ "autonomous way. \n\n"
				+ "The issue caught in your code were:"
				);

		final List list = new List(rightComp, SWT.V_SCROLL);
		list.setBackground(new org.eclipse.swt.graphics.Color(display, 255, 255, 255));
		list.setForeground(new org.eclipse.swt.graphics.Color(display, 0, 0, 0));

		Link link = new HyperlinkedText(null).words("").create(rightComp, SWT.WRAP | SWT.V_SCROLL);
		link.requestLayout();

		// LINTER INIT
		Linter TheLinter = Linter.INSTANCE.init(module);
		TheLinter.loadVisitors().loadAnalysers();
		TheLinter.getModule().setId("test");

//		CFGViewer cfg = new CFGViewer(codeAndCFG);
//		cfg.setInput(TheLinter.getProcedures().get(0).generateCFG().getNodes());
//		cfg.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		ArrayList<BadCodeCase> badCodeCases = TheLinter.analyse();

		for(int i=0; i < badCodeCases.size(); i++) {
			BadCodeCase badCase = badCodeCases.get(i);
			if(badCase != null) list.add(CaseNames.getCaseName(badCase.getCaseCategory()));
		}

		list.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(list.getSelectionIndex() == -1) return;
				if(rightComp.getChildren().length > 1) rightComp.getChildren()[2].dispose();

				BadCodeCase badCodeCase = badCodeCases.get(list.getSelectionIndex());
				badCodeCases.forEach(badCase -> badCase.hideAll());
				badCodeCase.generateComponent(widget, rightComp, SWT.BORDER_DOT); // passar widget
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

}
