package pt.iscte.paddle.linter.gui.main;


import java.util.ArrayList;
import java.util.ServiceLoader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import pt.iscte.javardise.javaeditor.api.HyperlinkedText;
import pt.iscte.javardise.javaeditor.api.IClassWidget;
import pt.iscte.javardise.javaeditor.api.IJavardiseService;
import pt.iscte.paddle.linter.cases.base.QualityIssue;
import pt.iscte.paddle.linter.examples.ContraditionAndTautology;
import pt.iscte.paddle.linter.examples.DuplicateGuard;
import pt.iscte.paddle.linter.examples.DuplicateStatement;
import pt.iscte.paddle.linter.examples.FaultyReturns;
import pt.iscte.paddle.linter.examples.MagicNumbers;
import pt.iscte.paddle.linter.examples.NonVoidPureFunction;
import pt.iscte.paddle.linter.examples.SelectionMisconception;
import pt.iscte.paddle.linter.examples.UnreachableCode;
import pt.iscte.paddle.linter.examples.UselessAssignments;
import pt.iscte.paddle.linter.linter.Linter;
import pt.iscte.paddle.linter.misc.CaseNames;
import pt.iscte.paddle.linter.misc.SelfAssignment;
import pt.iscte.paddle.linter.visitors.DuplicateBranchGuard;
import pt.iscte.paddle.linter.visitors.Selection;
import pt.iscte.paddle.model.tests.BaseTest;
import pt.paddle.linter.gui.component.QualityIssueHighlight;


public class LinterDemo {

	private static Shell shell;

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {

		ArrayList<BaseTest> modules = new ArrayList<BaseTest>(); 
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

		modules.add(t2);
//		modules.add(t1);
//		modules.add(t6);
//		modules.add(t3);
//		modules.add(t4);
//		modules.add(t5);
		UselessAssignments issue = new UselessAssignments();
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

		Composite codeAndCFG = new Composite(outer, SWT.WRAP | SWT.V_SCROLL);
		codeAndCFG.setLayout(new FillLayout(SWT.WRAP | SWT.V_SCROLL));

		Composite rightComp = new Composite(outer, SWT.BORDER);
		FillLayout rightLayout = new FillLayout(SWT.VERTICAL);
		rightLayout.marginHeight = 5;
		rightLayout.marginWidth = 5;
		rightLayout.spacing = 5;
		rightComp.setLayout(rightLayout);

		//		final Label label = new Label(rightComp, SWT.LEFT);
		//		label.setText("Modules");

		final List moduleList = new List(rightComp, SWT.V_SCROLL);
		moduleList.setBackground(new org.eclipse.swt.graphics.Color(display, 255, 255, 255));
		moduleList.setForeground(new org.eclipse.swt.graphics.Color(display, 0, 0, 0));

		for (BaseTest module : modules) moduleList.add(module.getClass().getSimpleName());

		//		final Label label2 = new Label(rightComp, SWT.LEFT);
		//		label2.setText("Quality Issues");

		final List caseList = new List(rightComp, SWT.V_SCROLL);

		ArrayList<QualityIssueHighlight> highlights = new ArrayList<QualityIssueHighlight>();

		Link link = new HyperlinkedText(null).words("").create(rightComp, SWT.WRAP | SWT.V_SCROLL);
		link.requestLayout();

		BaseTest testCase = modules.get(0);
		testCase.setup();

		ServiceLoader<IJavardiseService> loader = ServiceLoader.load(IJavardiseService.class);
		IJavardiseService serv = loader.findFirst().get();
		IClassWidget widget = serv.createClassWidget(codeAndCFG, testCase.getModule());
		widget.setReadOnly(true);

		// LINTER INIT
		Linter linter = new Linter(Selection.class, DuplicateBranchGuard.class);
		//		TheLinter.loadAnalysers(new Selection(), new MagicNumbers());
		java.util.List<QualityIssue> issues = linter.analyse(testCase.getModule());
//		System.out.println(TheLinter.getResults());

		for (QualityIssue qIssue : issues) {
			caseList.add(CaseNames.getCaseName(qIssue.getIssueType()));
			highlights.add(new QualityIssueHighlight(widget, rightComp, qIssue));
		}
		caseList.setBackground(new org.eclipse.swt.graphics.Color(display, 255, 255, 255));
		caseList.setForeground(new org.eclipse.swt.graphics.Color(display, 0, 0, 0));
		caseList.requestLayout();
		rightComp.requestLayout();

		moduleList.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if(moduleList.getSelectionIndex() == -1) return;

				caseList.removeAll();
				highlights.forEach(i -> i.hide());
				highlights.clear();

				BaseTest testCase = modules.get(moduleList.getSelectionIndex());
				testCase.setup();

				codeAndCFG.getChildren()[0].dispose();
				ServiceLoader<IJavardiseService> loader = ServiceLoader.load(IJavardiseService.class);
				IJavardiseService serv = loader.findFirst().get();
				
				IClassWidget widget = serv.createClassWidget(codeAndCFG, testCase.getModule());
				widget.setReadOnly(true);
				codeAndCFG.requestLayout();

				// LINTER INIT
				java.util.List<QualityIssue> issues = linter.analyse(testCase.getModule());

				for (QualityIssue qIssue : issues) {
					if(qIssue == null) continue;
					caseList.add(CaseNames.getCaseName(qIssue.getIssueType()));
					highlights.add(new QualityIssueHighlight(widget, rightComp, qIssue));
				}
				caseList.setBackground(new org.eclipse.swt.graphics.Color(display, 255, 255, 255));
				caseList.setForeground(new org.eclipse.swt.graphics.Color(display, 0, 0, 0));
				caseList.requestLayout();
				rightComp.requestLayout();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		caseList.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(caseList.getSelectionIndex() == -1) return;
				if(rightComp.getChildren().length > 1) rightComp.getChildren()[rightComp.getChildren().length - 1].dispose();

				QualityIssueHighlight badQualityIssue = highlights.get(caseList.getSelectionIndex());

				highlights.forEach(i -> i.hide());
				badQualityIssue.generateDecorations();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}

		});

		//		ArrayList<IModule> mod = new ArrayList<IModule>();
		//		mod.add(module2);
		//		mod.add(module);




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
