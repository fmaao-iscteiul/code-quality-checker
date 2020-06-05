package main;

import java.io.File;
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

import component.QualityIssueHighlight;
import pt.iscte.paddle.javardise.service.IClassWidget;
import pt.iscte.paddle.javardise.service.IJavardiseService;
import pt.iscte.paddle.javardise.util.HyperlinkedText;
import pt.iscte.paddle.model.IModule;
import pt.iscte.paddle.model.javaparser.Java2Paddle;
import pt.iscte.paddle.model.tests.BaseTest;
import pt.iscte.paddle.quality.cases.base.QualityIssue;
import pt.iscte.paddle.quality.client.Linter;
import pt.iscte.paddle.quality.client.LintingResult;
import pt.iscte.paddle.quality.examples.Contains;
import pt.iscte.paddle.quality.examples.ContraditionAndTautology;
import pt.iscte.paddle.quality.examples.DuplicateGuard;
import pt.iscte.paddle.quality.examples.DuplicateStatement;
import pt.iscte.paddle.quality.examples.FaultyReturns;
import pt.iscte.paddle.quality.examples.MagicNumbers;
import pt.iscte.paddle.quality.examples.NonVoidPureFunction;
import pt.iscte.paddle.quality.examples.SelectionMisconception;
import pt.iscte.paddle.quality.examples.UnreachableCode;
import pt.iscte.paddle.quality.examples.UselessAssignments;
import pt.iscte.paddle.quality.misc.CaseNames;
import pt.iscte.paddle.quality.misc.SelfAssignment;

public class LinterDemo {
	
	static ArrayList<IModule> searchDirectoryForModules(File f) {
		ArrayList<IModule> modules = new ArrayList<IModule>();
		File[] files = f.listFiles(file -> file.getName().endsWith("Project53.java"));
//		File[] files = f.listFiles();
		for (File file : files) {
			if(file.isDirectory()) {
				try {
					Java2Paddle jparser = new Java2Paddle(file, file.getName().replace(".java", ""));
					IModule m = jparser.parse();
					modules.add(m);
				} catch (Exception e) {
					System.out.println("ups");
				}
				searchDirectoryForModules(file);
			} else {
				try {
					System.out.println(file);
					Java2Paddle jparser = new Java2Paddle(file, file.getName().replace(".java", ""));
					IModule m = jparser.parse();
					System.out.println(m);
					modules.add(m);
				} catch (Exception e) {
					System.out.println("ups");
				}
			}
		}
		return modules;
	}

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

		modules.add(new Contains());
		modules.add(t1);
		modules.add(t6);
		modules.add(t3);
		modules.add(t4);
		modules.add(t5);
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
		
		File f = new File("/Users/franciscoalfredo/Desktop/uni/tese/trabalhos4paddle/src");
		ArrayList<IModule> parsedModules = searchDirectoryForModules(f);
		IModule testCase = parsedModules.get(0);
		
		ServiceLoader<IJavardiseService> service = ServiceLoader.load(IJavardiseService.class);
		IJavardiseService javardise = service.findFirst().get();
		
		
		IClassWidget widget = javardise.createClassWidget(codeAndCFG, testCase, testCase.getId());
		widget.setReadOnly(true);

		// LINTER INIT
		Linter linter = new Linter();
		java.util.List<QualityIssue> issues = linter.analyse(testCase);
		parsedModules.forEach(m -> System.out.println(new LintingResult(linter.analyse(m))));

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
				IClassWidget widget = javardise.createClassWidget(codeAndCFG, testCase.getModule(), null);
				widget.setReadOnly(true);
				codeAndCFG.redraw();

				// LINTER INIT
				java.util.List<QualityIssue> issues = linter.analyse(testCase.getModule());
				System.out.println(new LintingResult(issues));
				for (QualityIssue qIssue : issues) {
					if(qIssue == null) continue;
					caseList.add(CaseNames.getCaseName(qIssue.getIssueType()));
					highlights.add(new QualityIssueHighlight(widget, rightComp, qIssue));
				}
				caseList.setBackground(new org.eclipse.swt.graphics.Color(display, 255, 255, 255));
				caseList.setForeground(new org.eclipse.swt.graphics.Color(display, 0, 0, 0));
				caseList.redraw();
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
				badQualityIssue.show();
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
