package pt.iscte.paddle.codequality.linter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import pt.iscte.paddle.codequality.cases.Duplicate;
import pt.iscte.paddle.codequality.cases.MagicNumber;
import pt.iscte.paddle.codequality.misc.BranchVerifications;
import pt.iscte.paddle.codequality.tests.linter.DuplicateStatement;
import pt.iscte.paddle.codequality.tests.linter.EmptySelection;
import pt.iscte.paddle.codequality.tests.linter.SelectionMisconception;
import pt.iscte.paddle.javardise.ClassWidget;
import pt.iscte.paddle.javardise.MarkerService;
import pt.iscte.paddle.javardise.MarkerService.Mark;
import pt.iscte.paddle.model.IModule;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IProgramElement;

public class LinterDemo {

	private static Shell shell;

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {

		DuplicateStatement t = new DuplicateStatement(); 
		t.setup();
		IModule module = t.getModule();

		Display display = new Display();
		shell = new Shell(display);
		shell.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		GridLayout layout = new GridLayout(1, false);
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
		
		Composite comp = new Composite(shell, SWT.BORDER);
		comp.setLayout(new FillLayout());

		List<IProgramElement> children = new ArrayList<IProgramElement>();
		TheLinter.analyse().forEach(badCase -> {
			badCase.generateComponent(display, comp, SWT.BORDER_DOT);
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
