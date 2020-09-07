package main;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
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
import pt.iscte.paddle.quality.issues.EmptySelection;
import pt.iscte.paddle.quality.misc.CaseNames;
import pt.iscte.paddle.quality.misc.SelfAssignment;
import pt.iscte.paddle.quality.visitors.Loop;
import pt.iscte.paddle.quality.visitors.Selection;

public class LinterDemo {

	static ArrayList<IModule> searchDirectoryForModules(File f) {
		ArrayList<IModule> modules = new ArrayList<IModule>();
		File[] files = f.listFiles(file -> file.getName().endsWith("Example.java"));
//		File[] files = f.listFiles(file -> !file.getName().endsWith("Example.java"));
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


		Display display = new Display();
		shell = new Shell(display);
		shell.setText("ISCTECKER - CODE QUALITY CHECKER");
		shell.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		FillLayout layout = new FillLayout(SWT.VERTICAL);
		layout.marginHeight = 5;
		layout.marginWidth = 5;
		shell.setLayout(layout);

		//		Composite outer = new Composite(shell, SWT.BORDER | SWT.VERTICAL);
		//		FillLayout outerLayout = new FillLayout(SWT.VERTICAL  | SWT.V_SCROLL | SWT.SCROLL_LINE);
		//		outerLayout.marginHeight = 5;
		//		outerLayout.marginWidth = 5;
		//		outerLayout.spacing = 5;
		//		outer.setLayout(outerLayout);

		SashForm outer = new SashForm(shell, SWT.VERTICAL);

		Composite codeAndCFG = new Composite(outer, SWT.V_SCROLL);
		codeAndCFG.setLayout(new FillLayout(SWT.V_SCROLL));

		Composite rightComp = new Composite(outer, SWT.BORDER );
		FillLayout rightLayout = new FillLayout(SWT.VERTICAL);
		rightLayout.marginHeight = 5;
		rightLayout.marginWidth = 5;
		rightLayout.spacing = 5;
		rightComp.setLayout(rightLayout);

		//		final Label label = new Label(rightComp, SWT.LEFT);
		//		label.setText("Modules");

		//		final List moduleList = new List(rightComp, SWT.V_SCROLL);
		//		moduleList.setBackground(new org.eclipse.swt.graphics.Color(display, 255, 255, 255));
		//		moduleList.setForeground(new org.eclipse.swt.graphics.Color(display, 0, 0, 0));


		//		final Label label2 = new Label(rightComp, SWT.LEFT);
		//		label2.setText("Quality Issues");

		//		final List caseList = new List(rightComp, SWT.V_SCROLL);

		ArrayList<QualityIssueHighlight> highlights = new ArrayList<QualityIssueHighlight>();

		Link link = new HyperlinkedText(null).words("").create(rightComp, SWT.WRAP | SWT.V_SCROLL);
		link.requestLayout();

		File f = new File("/Users/franciscoalfredo/Desktop/uni/tese/trabalhos4paddle/src"); // uni/tese/trabalhos4paddle/src
		ArrayList<IModule> parsedModules = searchDirectoryForModules(f);

//		parsedModules.sort(new Comparator<IModule>() {
//			@Override
//			public int compare(IModule o1, IModule o2) {
//				if(Integer.parseInt(o1.getId().split("_")[1]) > Integer.parseInt(o2.getId().split("_")[1]))
//					return 1;
//				else return -1;
//			}
//		});

		parsedModules.forEach(module -> {
			System.out.println(module.getId());
		});


		ServiceLoader<IJavardiseService> service = ServiceLoader.load(IJavardiseService.class);
		IJavardiseService javardise = service.findFirst().get();

		// LINTER INIT
		Linter linter = new Linter();

		Map<String, java.util.List<QualityIssue>> globalResults = new HashMap<String, java.util.List<QualityIssue>>();

		parsedModules.forEach(m -> {
			//			moduleList.add(m.getId());
			java.util.List<QualityIssue> qIssues = linter.analyse(m);
			globalResults.put(m.getId(), qIssues);
		});


		IModule testCase = parsedModules.get(0);

		IClassWidget widget = javardise.createClassWidget(codeAndCFG, testCase, testCase.getId());
		widget.setReadOnly(true);


		System.out.println("------------------------------------");
		System.out.println("NUMBER OF PARSED MODULES: " + parsedModules.size());
		LintingResult results = new LintingResult(globalResults);
		System.out.println(results.finalResults());

		outer.addKeyListener(new KeyListener() {

			int counter = 0 ;
			IClassWidget widget;


			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println(counter);
				switch (e.keyCode) {
				case SWT.ARROW_LEFT: {
					if(rightComp.getChildren().length >= 1) rightComp.getChildren()[rightComp.getChildren().length - 1].dispose();
					if(counter <= 0) break;
					counter--;
					highlights.forEach(i -> i.hide());
					if(rightComp.getChildren().length >= 1) rightComp.getChildren()[rightComp.getChildren().length - 1].dispose();
					System.out.println("left");
					IModule testCase = parsedModules.get(counter);
					codeAndCFG.getChildren()[0].dispose();
					widget = javardise.createClassWidget(codeAndCFG, testCase, testCase.getId());
					widget.setReadOnly(true);
					outer.forceFocus();
					codeAndCFG.redraw();
					break;
				}
				case SWT.ARROW_RIGHT: {
					if(rightComp.getChildren().length >= 1) rightComp.getChildren()[rightComp.getChildren().length - 1].dispose();
					if(counter >= 9) break;
					counter++;
					highlights.forEach(i -> i.hide());
					//					if(rightComp.getChildren().length >= 1) rightComp.getChildren()[rightComp.getChildren().length - 1].dispose();
					IModule testCase = parsedModules.get(counter);
					codeAndCFG.getChildren()[0].dispose();
					widget = javardise.createClassWidget(codeAndCFG, testCase, testCase.getId());
					widget.setReadOnly(true);
					System.out.println("right");
					outer.forceFocus();
					codeAndCFG.redraw();
					break;
				}
				case SWT.SPACE: {
					if(rightComp.getChildren().length >= 1) rightComp.getChildren()[rightComp.getChildren().length - 1].dispose();
					highlights.forEach(i -> i.hide());
					IModule testCase = parsedModules.get(counter);
					java.util.List<QualityIssue> issues = linter.analyse(testCase);
					//					codeAndCFG.getChildren()[0].dispose();
					//					widget = javardise.createClassWidget(codeAndCFG, testCase, testCase.getId());
					codeAndCFG.redraw();
					outer.forceFocus();
					if(issues.isEmpty()) break;
					QualityIssueHighlight highlight = new QualityIssueHighlight(widget, rightComp, issues.size() > 1? issues.get(1) : issues.get(0));
					highlights.add(highlight);
					highlight.show();
					System.out.println("space: " + issues.size());
					outer.forceFocus();
					break;
				}
				}

			}
		});

		//		for (QualityIssue qIssue : issues) {
		//			//			caseList.add(CaseNames.getCaseName(qIssue.getIssueType()));
		//			highlights.add(new QualityIssueHighlight(widget, rightComp, qIssue));
		//		}
		//		caseList.setBackground(new org.eclipse.swt.graphics.Color(display, 255, 255, 255));
		//		caseList.setForeground(new org.eclipse.swt.graphics.Color(display, 0, 0, 0));
		//		caseList.requestLayout();
		//		rightComp.requestLayout();

		//		moduleList.addSelectionListener(new SelectionListener() {
		//
		//			@Override
		//			public void widgetSelected(SelectionEvent e) {
		//				if(moduleList.getSelectionIndex() == -1) return;
		//
		//				caseList.removeAll();
		//				highlights.forEach(i -> i.hide());
		//				highlights.clear();
		//
		//				IModule testCase = parsedModules.get(moduleList.getSelectionIndex());
		//
		//				codeAndCFG.getChildren()[0].dispose();
		//				IClassWidget widget = javardise.createClassWidget(codeAndCFG, testCase, testCase.getId());
		//				widget.setReadOnly(true);
		//				codeAndCFG.redraw();
		//
		//				// LINTER INIT
		//				java.util.List<QualityIssue> issues = linter.analyse(testCase);
		////				System.out.println(new LintingResult(issues));
		//				
		//				for (QualityIssue qIssue : issues) {
		//					if(qIssue == null) continue;
		//					caseList.add(CaseNames.getCaseName(qIssue.getIssueType()));
		//					highlights.add(new QualityIssueHighlight(widget, rightComp, qIssue));
		//				}
		//				caseList.setBackground(new org.eclipse.swt.graphics.Color(display, 255, 255, 255));
		//				caseList.setForeground(new org.eclipse.swt.graphics.Color(display, 0, 0, 0));
		//				caseList.redraw();
		//				rightComp.requestLayout();
		//			}
		//
		//			@Override
		//			public void widgetDefaultSelected(SelectionEvent e) {
		//				// TODO Auto-generated method stub
		//
		//			}
		//		});

		//		caseList.addSelectionListener(new SelectionListener() {
		//			@Override
		//			public void widgetSelected(SelectionEvent e) {
		//				if(caseList.getSelectionIndex() == -1) return;
		//				if(rightComp.getChildren().length > 1) rightComp.getChildren()[rightComp.getChildren().length - 1].dispose();
		//
		//				QualityIssueHighlight badQualityIssue = highlights.get(caseList.getSelectionIndex());
		//
		//				highlights.forEach(i -> i.hide());
		//				badQualityIssue.show();
		//			}
		//
		//			@Override
		//			public void widgetDefaultSelected(SelectionEvent e) {}
		//
		//		});

		//		ArrayList<IModule> mod = new ArrayList<IModule>();
		//		mod.add(module2);
		//		mod.add(module);




		shell.setSize(800, 800);
		//		shell.pack();
//		shell.open();
//		while (!shell.isDisposed()) {
//			if (!display.readAndDispatch()) {
//				display.sleep();
//			}
//		}
//		display.dispose();
	}

}
