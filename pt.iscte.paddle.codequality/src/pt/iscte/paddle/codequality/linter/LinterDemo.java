package pt.iscte.paddle.codequality.linter;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

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
		GridLayout layout = new GridLayout(2, false);
		layout.marginTop = 10;
		layout.marginLeft = 50;
		layout.marginRight = 150;
		layout.verticalSpacing = 20;

		shell.setLayout(layout);

		ClassWidget widget = new ClassWidget(shell, t.getModule());
		widget.setEnabled(false);

		Linter TheLinter = Linter.INSTANCE.init(module);
		TheLinter.loadVisitors();
		TheLinter.getModule().setId("test");

		//		IColorScheme ics = new ColorScheme();
		//		new CFGWindow(TheLinter.cfg.getCFG(), ics);
		System.out.println("dawdw: " +TheLinter.cfg);

		Composite comp = new Composite(shell, SWT.BORDER);

		comp.setLayout(new GridLayout());

		TheLinter.analyse().forEach(badCase -> {
			System.out.println(badCase.getCaseCategory());
			Button next = new Button(comp, SWT.PUSH);
			next.setText(badCase.getCaseCategory().toString());
			next.addSelectionListener(new SelectionListener() {
				boolean visible = false;
				@Override
				public void widgetSelected(SelectionEvent e) {
					TheLinter.getCaughtCases().forEach(badCase -> badCase.hideAll());

					badCase.generateComponent(display, comp, SWT.BORDER_DOT);


				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub

				}
			});
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
