package pt.iscte.paddle.linter.gui.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.ServiceLoader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import pt.iscte.javardise.javaeditor.api.IClassWidget;
import pt.iscte.javardise.javaeditor.api.IJavardiseService;
import pt.iscte.paddle.linter.cases.base.QualityIssue;
import pt.iscte.paddle.linter.examples.UselessAssignments;
import pt.iscte.paddle.linter.linter.Linter;
import pt.iscte.paddle.model.IModule;
import pt.iscte.paddle.model.javaparser.Java2Paddle;
import pt.paddle.linter.gui.component.QualityIssueHighlight;

public class LinterDemoGUI {

	private static Shell shell;
	private static QualityIssueHighlight activeQualityIssue;

	public static void main(String[] args)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
		
		
		ServiceLoader<IJavardiseService> loader = ServiceLoader.load(IJavardiseService.class);
		IJavardiseService serv = loader.findFirst().get();

		UselessAssignments issue = new UselessAssignments();
		issue.setup();

		Display display = new Display();
		shell = new Shell(display);
		shell.setText("Sprinter - Code Quality Checker");
		shell.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		Font font = new Font(display, new FontData("Courier", 16, SWT.NORMAL));

		FillLayout layout = new FillLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		shell.setLayout(layout);

		SashForm sash = new SashForm(shell, SWT.NONE);

		Composite rightComp = new Composite(sash, SWT.NONE);
		
		FillLayout rightLayout = new FillLayout(SWT.VERTICAL);
		rightLayout.marginHeight = 20;
		rightLayout.marginWidth = 20;
		rightLayout.spacing = 5;
		rightComp.setLayout(rightLayout);
		
		ScrolledComposite scroll = new ScrolledComposite(sash, SWT.H_SCROLL | SWT.V_SCROLL);
		scroll.setLayout(new GridLayout(1, false));
		scroll.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Composite area = new Composite(scroll, SWT.NONE);
		area.setLayout(new FillLayout());

		scroll.setContent(area);
		scroll.setMinSize(100, 100);
		scroll.setExpandHorizontal(true);
		scroll.setExpandVertical(true);
//		scroll.setShowFocusedControl(true);

		area.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				Point size = area.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				size.x += 100;
				size.y += 100;
				scroll.setMinSize(size);
				scroll.requestLayout();
				if (activeQualityIssue != null)
					activeQualityIssue.show();
			}
		});

		area.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		
		sash.setWeights(new int[] { 1, 3 });
		
		Label label = new Label(area, SWT.NONE);
		label.setFont(font);
		label.setText("Please select a file...");

		

		Text srcText = new Text(rightComp, SWT.MULTI | SWT.H_SCROLL);

		Text paddleText = new Text(rightComp, SWT.MULTI | SWT.H_SCROLL);

		final List moduleList = new List(rightComp, SWT.BORDER | SWT.V_SCROLL);

		Map<String, IModule> map = new HashMap<>();
		Map<String, File> mapFile = new HashMap<>();

		if(args.length == 0) {
			DirectoryDialog dialog = new DirectoryDialog(shell);
		    //dialog.setFilterPath("c:\\"); // Windows specific
			args = new String[] {dialog.open()};
		    //System.out.println("RESULT=" + dialog.open());
		}
		
		
		File src = new File(args[0]);
		File[] files;
		if (src.isFile())
			files = new File[] { src };
		else
			files = src.listFiles(f -> f.getName().endsWith(".java") && !f.getName().equals("ImageUtil.java"));
		
		PrintWriter log = new PrintWriter(new File(files[0].getParentFile(), "log.txt"));
		long time = System.currentTimeMillis();
		
		log.println("Start: " + new Date());
		
		for(File f : files)
			log.println("File: " + f.getAbsolutePath());

		for (File f : files) {
			Java2Paddle parser = new Java2Paddle(f);
			parser.parse();
			//try {
			IModule m = parser.parse();
				moduleList.add(f.getName());
				map.put(f.getName(), m);
				mapFile.put(f.getName(), f);
//			} catch (Exception e) {
//				System.err.println(f.getName() + " not included");
//			}
		}

		final List caseList = new List(rightComp, SWT.BORDER | SWT.V_SCROLL);
		ArrayList<QualityIssueHighlight> highlights = new ArrayList<QualityIssueHighlight>();

		Linter linter = new Linter();

		moduleList.addSelectionListener(new SelectionAdapter() {
			int index = -1;

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (moduleList.getSelectionIndex() == -1 || moduleList.getSelectionIndex() == index)
					return;

				activeQualityIssue = null;
				index = moduleList.getSelectionIndex();
				caseList.removeAll();
				highlights.forEach(i -> i.remove());
				highlights.clear();
				rightComp.setEnabled(false);
				
				String fileName = moduleList.getItem(moduleList.getSelectionIndex());
				IModule m = map.get(fileName);
				IClassWidget widget = serv.createClassWidget(area, m);
				widget.getControl().setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
				widget.setReadOnly(true);

				Display.getDefault().asyncExec(() -> {
					java.util.List<QualityIssue> issues = linter.analyse(map.get(fileName));
					String[] cases = new String[issues.size()];
					int i = 0;
					for (QualityIssue qIssue : issues) {
						cases[i++] = qIssue.getIssueTitle(); // CaseNames.getCaseName(qIssue.getIssueType());
						highlights.add(new QualityIssueHighlight(widget, rightComp, qIssue));
					}
					caseList.setItems(cases);
					
					try {
						Scanner scanner = new Scanner(mapFile.get(fileName));
						String src = "";
						while (scanner.hasNextLine())
							src += scanner.nextLine() + "\n";
						scanner.close();
						srcText.setText(src);
						paddleText.setText(m.toString());
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					area.getChildren()[0].dispose();
					area.requestLayout();
					rightComp.setEnabled(true);

				});

			}
		});

		caseList.addSelectionListener(new SelectionAdapter() {
			Link explanation;
			
			public void widgetSelected(SelectionEvent e) {
				if (caseList.getSelectionIndex() == -1) {
					return;
				}
				if (explanation != null)
					explanation.dispose();

				if (activeQualityIssue != null)
					activeQualityIssue.remove();

				activeQualityIssue = highlights.get(caseList.getSelectionIndex());

//				highlights.forEach(i -> i.hide());	
				explanation = activeQualityIssue.generateExplanation();
				activeQualityIssue.generateDecorations();
				System.out.println("LOC " + activeQualityIssue.getYLocation());
				System.out.println(scroll.getOrigin() + " ");
				if (activeQualityIssue.getYLocation() < scroll.getOrigin().y
						|| activeQualityIssue.getYLocation() > scroll.getOrigin().y + scroll.getClientArea().height)
					scroll.setOrigin(0, activeQualityIssue.getYLocation() - 100);
				activeQualityIssue.show();
			}
		});

		scroll.getVerticalBar().addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (activeQualityIssue != null)
					activeQualityIssue.show();
			}
		});
		scroll.getHorizontalBar().addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (activeQualityIssue != null)
					activeQualityIssue.show();
			}
		});

		
		
		// shell.setSize(1600, 1000);
		shell.setMaximized(true);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		
		log.println("End: " + new Date());
		log.println("Seconds: " + (System.currentTimeMillis() - time)/1000 );
		log.close();
		
		display.dispose();
//		Shell over = new Shell(display, SWT.APPLICATION_MODAL);
//		over.setText("time");
//		over.setLayout(new FillLayout());
//		new Label(over, SWT.NONE).setText("Time: " + (System.currentTimeMillis() - time)/1000 + " seconds." );
//		over.open();
//		while (!over.isDisposed()) {
//			if (!display.readAndDispatch()) {
//				display.sleep();
//			}
//		}
	}

}
