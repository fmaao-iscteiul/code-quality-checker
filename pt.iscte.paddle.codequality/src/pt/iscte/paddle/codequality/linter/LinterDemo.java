package pt.iscte.paddle.codequality.linter;


import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import pt.iscte.paddle.codequality.cases.BadCodeCase;
import pt.iscte.paddle.codequality.tests.linter.DuplicateLoopGuard;
import pt.iscte.paddle.codequality.tests.linter.DuplicateStatement;
import pt.iscte.paddle.codequality.tests.linter.SelectionMisconception;
import pt.iscte.paddle.codequality.tests.linter.UnreachableCode;
import pt.iscte.paddle.javardise.ClassWidget;
import pt.iscte.paddle.model.IModule;

public class LinterDemo {

	private static Shell shell;

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {

		UnreachableCode t = new UnreachableCode(); 
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
		
		Composite widgetComp = new Composite(outer, SWT.V_SCROLL);
		widgetComp.setLayout(new FillLayout());

		ClassWidget widget = new ClassWidget(widgetComp, module);
		widget.setEnabled(false);
		
		Composite rightComp = new Composite(outer, SWT.BORDER);
		FillLayout rightLayout = new FillLayout(SWT.VERTICAL);
		
		rightComp.setLayout(rightLayout);

		final List list = new List(rightComp, SWT.V_SCROLL);
		list.setSize(300, 300);
		list.setBackground(new org.eclipse.swt.graphics.Color(display, 255, 255, 255));
		list.setForeground(new org.eclipse.swt.graphics.Color(display, 0, 0, 0));
		
		Text text = new Text(rightComp, SWT.WRAP | SWT.V_SCROLL);
		text.setEditable(false);
		
		// LINTER INIT
		Linter TheLinter = Linter.INSTANCE.init(module);
		TheLinter.loadVisitors();
		TheLinter.getModule().setId("test");
		ArrayList<BadCodeCase> badCodeCases = TheLinter.analyse();

		for(int i=0; i < badCodeCases.size(); i++) {
			BadCodeCase badCase = badCodeCases.get(i);
			if(badCase == null) continue;
			else list.add(badCase.getCaseCategory().toString());
		}
		
		list.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(list.getSelectionIndex() == -1) return;

				BadCodeCase badCodeCase = badCodeCases.get(list.getSelectionIndex());
				badCodeCases.forEach(badCase -> badCase.hideAll());
				badCodeCase.generateComponent(display, shell, SWT.BORDER_DOT);
				text.setText(badCodeCase.getExplanation());
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
