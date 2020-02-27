package pt.iscte.paddle.codequality.linter;

import static pt.iscte.paddle.model.IOperator.ADD;
import static pt.iscte.paddle.model.IOperator.SMALLER;
import static pt.iscte.paddle.model.IType.INT;

import java.io.File;
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

import pt.iscte.paddle.codequality.cases.BadCodeCase.Category;
import pt.iscte.paddle.javardise.ClassWidget;
import pt.iscte.paddle.javardise.Constants;
import pt.iscte.paddle.javardise.Decoration;
import pt.iscte.paddle.javardise.MarkerService;
import pt.iscte.paddle.javardise.MarkerService.Mark;
import pt.iscte.paddle.model.IArrayElementAssignment;
import pt.iscte.paddle.model.IBlock;
import pt.iscte.paddle.model.IBlockElement;
import pt.iscte.paddle.model.ILoop;
import pt.iscte.paddle.model.IModule;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IReturn;
import pt.iscte.paddle.model.IVariable;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.IBlock.IVisitor;

public class LinterDemo {

	private static Shell shell;

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		
		IModule module = IModule.create();
		IProcedure naturals = module.addProcedure(INT.array());
		IVariable n = naturals.addParameter(INT);
		IBlock body = naturals.getBody();
		IVariable array = body.addVariable(INT.array());
		IVariableAssignment ass1 = body.addAssignment(array, INT.array().stackAllocation(n));
		IVariable i = body.addVariable(INT, INT.literal(0));
		ILoop loop = body.addLoop(SMALLER.on(i, n));
		IArrayElementAssignment ass2 = loop.addArrayElementAssignment(array, ADD.on(i, INT.literal(1)), i);
		IVariableAssignment ass3 = loop.addAssignment(i, ADD.on(i, INT.literal(1)));
		IReturn addReturn = body.addReturn(array);
		
		System.out.println(module);
		
		Linter TheLinter = Linter.INSTANCE.init(module);
		TheLinter.loadVisitors();
		TheLinter.getModule().setId("test"); 
		
		Display display = new Display();
		shell = new Shell(display);
		shell.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		GridLayout layout = new GridLayout(1, false);
		layout.marginTop = 50;
		layout.marginLeft = 50;
		layout.verticalSpacing = 20;
		shell.setLayout(layout);
		
		ClassWidget widget = new ClassWidget(shell, TheLinter.getModule());
		widget.setEnabled(false);
		
		
		List<IBlockElement> children = new ArrayList<IBlockElement>();
		TheLinter.analyse().forEach(badCase -> {
			System.out.println(badCase.getCaseCategory());
			if(badCase.getCaseCategory().equals(Category.DUPLICATE_CODE))
				children.add(badCase.getBlock());
		});
	
		Composite comp = new Composite(shell, SWT.BORDER);
		comp.setLayout(new FillLayout());
		
		Button next = new Button(comp, SWT.PUSH);
		next.setText("next");
		next.addSelectionListener(new SelectionAdapter() {
			Color red = new Color (display, 255, 0, 0);
			Iterator<IBlockElement> it = children.iterator();
			IBlockElement element = it.next();
			Mark mark;
			public void widgetSelected(SelectionEvent e) {
				if(mark != null)
					mark.unmark();

				mark = MarkerService.mark(red, element);
				if(it.hasNext())
					element = it.next();
			}
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
