package pt.iscte.paddle.codequality.linter;


import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import pt.iscte.paddle.codequality.cases.base.QualityIssue;
import pt.iscte.paddle.codequality.examples.BooleanConditionVerbose;
import pt.iscte.paddle.codequality.examples.ContraditionAndTautology;
import pt.iscte.paddle.codequality.examples.DuplicateGuard;
import pt.iscte.paddle.codequality.examples.DuplicateStatement;
import pt.iscte.paddle.codequality.examples.EmptyBlocks;
import pt.iscte.paddle.codequality.examples.EmptyIfWithFilledElse;
import pt.iscte.paddle.codequality.examples.FaultyReturns;
import pt.iscte.paddle.codequality.examples.MagicNumbers;
import pt.iscte.paddle.codequality.examples.NonVoidPureFunction;
import pt.iscte.paddle.codequality.examples.SelectionMisconception;
import pt.iscte.paddle.codequality.examples.Tautology;
import pt.iscte.paddle.codequality.examples.UnreachableCode;
import pt.iscte.paddle.codequality.examples.UselessAssignments;
import pt.iscte.paddle.codequality.misc.CaseNames;
import pt.iscte.paddle.codequality.misc.SelfAssignment;
import pt.iscte.paddle.javardise.service.IClassWidget;
import pt.iscte.paddle.javardise.service.IJavardiseService;
import pt.iscte.paddle.javardise.util.HyperlinkedText;
import pt.iscte.paddle.model.IModule;
import pt.iscte.paddle.model.tests.BaseTest;

public class LinterDemo {

	private static Shell shell;

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {

		UnreachableCode t = new UnreachableCode();
		NonVoidPureFunction t1 = new NonVoidPureFunction();
		SelectionMisconception t2 = new SelectionMisconception();
		DuplicateGuard t3 = new DuplicateGuard();
		DuplicateStatement t4 = new DuplicateStatement();
		FaultyReturns t5 = new FaultyReturns();
		UselessAssignments t6 = new UselessAssignments();
		ContraditionAndTautology t7 = new ContraditionAndTautology();
		SelfAssignment t8 = new SelfAssignment();
		MagicNumbers t9 = new MagicNumbers();
		
		NonVoidPureFunction issue = new NonVoidPureFunction();
		issue.setup();

		Display display = new Display();
		shell = new Shell(display);
		shell.setText("ISCTECKER - CODE QUALITY CHECKER");
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

		Composite codeAndCFG = new Composite(outer, SWT.WRAP | SWT.V_SCROLL);
		codeAndCFG.setLayout(new FillLayout(SWT.WRAP | SWT.V_SCROLL));

		IClassWidget widget = IJavardiseService.createClassWidget(codeAndCFG, issue.getModule());
		widget.setReadOnly(true);

		Composite rightComp = new Composite(outer, SWT.BORDER);
		FillLayout rightLayout = new FillLayout(SWT.VERTICAL);
		rightLayout.marginHeight = 5;
		rightLayout.marginWidth = 5;
		rightLayout.spacing = 5;
		rightComp.setLayout(rightLayout);

		//		Label label = new Label(rightComp, SWT.WRAP | SWT.CENTER);
		//		label.setText("CODE QUALITY CHECKER \n\n\n"
		//				+ "This tool was designed to help you improve your code base, when it comes code quality!"
		//				+ " It will detect and highlight many code quality related issues and alert you about them, including a brief explanation"
		//				+ "regarding the reason why they should be avoid. With this being, this tool will help you in eliminating bad practises in an "
		//				+ "autonomous way. \n\n"
		//				+ "The issue caught in your code were:"
		//				);

		//		final List moduleList = new List(rightComp, SWT.V_SCROLL);
		//		moduleList.addSelectionListener(new SelectionListener() {
		//			@Override
		//			public void widgetSelected(SelectionEvent e) {
		////				if(rightComp.getChildren().length > 1) rightComp.getChildren()[1].dispose();
		//				
		//				BaseTest baseTest = modules[moduleList.getSelectionIndex()];
		//				baseTest.setup();
		//				codeAndCFG.update();
		//				IClassWidget widget = IJavardiseService.createClassWidget(codeAndCFG, baseTest.getModule());
		//				widget.setReadOnly(true);
		//			}
		//
		//			@Override
		//			public void widgetDefaultSelected(SelectionEvent e) {}
		//		});
		//		
		final List caseList = new List(rightComp, SWT.V_SCROLL);
		caseList.setBackground(new org.eclipse.swt.graphics.Color(display, 255, 255, 255));
		caseList.setForeground(new org.eclipse.swt.graphics.Color(display, 0, 0, 0));


		Link link = new HyperlinkedText(null).words("").create(rightComp, SWT.WRAP | SWT.V_SCROLL);
		link.requestLayout();


//		ArrayList<IModule> mod = new ArrayList<IModule>();
//		mod.add(module2);
//		mod.add(module);
		
		
		// LINTER INIT
		Linter TheLinter = Linter.INSTANCE.init(issue.getModule());
		TheLinter.analyse();
		System.out.println(TheLinter.getResults());
		
		//		CFGViewer cfg = new CFGViewer(codeAndCFG);
		//		cfg.setInput(TheLinter.getProcedures().get(0).generateCFG().getNodes());
		//		cfg.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		ArrayList<QualityIssue> qualityIssues = TheLinter.getCaughtCases();

		for(int i=0; i < qualityIssues.size(); i++) {
			QualityIssue badCase = qualityIssues.get(i);
			if(badCase != null) caseList.add(CaseNames.getCaseName(badCase.getIssueType()));
		}

		caseList.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(caseList.getSelectionIndex() == -1) return;
				if(rightComp.getChildren().length > 1) rightComp.getChildren()[1].dispose();

				QualityIssue badCodeCase = qualityIssues.get(caseList.getSelectionIndex());
				qualityIssues.forEach(badCase -> badCase.hideAll());
				badCodeCase.generateComponent(widget, rightComp, SWT.BORDER_DOT); // passar widget
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});

		shell.setSize(800, 800);
		//		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

}
