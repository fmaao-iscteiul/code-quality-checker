package pt.iscte.paddle.linter.gui.main;


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
import pt.iscte.paddle.javardise.service.IClassWidget;
import pt.iscte.paddle.javardise.service.IJavardiseService;
import pt.iscte.paddle.javardise.util.HyperlinkedText;
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
import pt.paddle.linter.gui.component.QualityIssueHighlight;


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

		IClassWidget widget = IJavardiseService.createClassWidget(codeAndCFG, issue.getModule());
		widget.setReadOnly(true);

		Composite rightComp = new Composite(outer, SWT.BORDER);
		FillLayout rightLayout = new FillLayout(SWT.VERTICAL);
		rightLayout.marginHeight = 5;
		rightLayout.marginWidth = 5;
		rightLayout.spacing = 5;
		rightComp.setLayout(rightLayout);

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

		ArrayList<QualityIssueHighlight> highlights = new ArrayList<QualityIssueHighlight>();

		for (QualityIssue qIssue : TheLinter.getCaughtCases()) {
			if(qIssue == null) continue;
			caseList.add(CaseNames.getCaseName(qIssue.getIssueType()));
			highlights.add(new QualityIssueHighlight(widget, rightComp, qIssue));
		}
		
		caseList.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(caseList.getSelectionIndex() == -1) return;
				if(rightComp.getChildren().length > 1) rightComp.getChildren()[1].dispose();

				QualityIssueHighlight badQualityIssue = highlights.get(caseList.getSelectionIndex());

				highlights.forEach(i -> i.hide());
				badQualityIssue.show();
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
