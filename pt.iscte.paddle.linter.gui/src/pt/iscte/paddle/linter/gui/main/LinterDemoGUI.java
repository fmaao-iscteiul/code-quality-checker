package pt.iscte.paddle.linter.gui.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

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
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.mozilla.universalchardet.UniversalDetector;

import pt.iscte.javardise.javaeditor.api.IClassWidget;
import pt.iscte.javardise.javaeditor.api.IJavardiseService;
import pt.iscte.paddle.linter.cases.base.QualityIssue;
import pt.iscte.paddle.linter.linter.Linter;
import pt.iscte.paddle.linter.misc.IssueType;
import pt.iscte.paddle.model.IModule;
import pt.iscte.paddle.model.javaparser.Java2Paddle;
import pt.paddle.linter.gui.component.QualityIssueHighlight;

public class LinterDemoGUI {

	private static Shell shell;
	private static QualityIssueHighlight activeQualityIssue;
	private static Link explanation;

	public static void main(String[] args)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {

		ServiceLoader<IJavardiseService> loader = ServiceLoader.load(IJavardiseService.class);
		IJavardiseService serv = loader.findFirst().get();

		Display display = new Display();
		shell = new Shell(display);
		shell.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		Font font = new Font(display, new FontData("Arial", 12, SWT.NORMAL));

		FillLayout layout = new FillLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		shell.setLayout(layout);

		SashForm sash = new SashForm(shell, SWT.VERTICAL);

		SashForm topComp = new SashForm(sash, SWT.NONE);

		RowLayout rightLayout = new RowLayout(SWT.VERTICAL);
		rightLayout.marginHeight = 20;
		rightLayout.marginWidth = 20;
		rightLayout.spacing = 5;
		topComp.setLayout(new GridLayout(3, false));

		
		SashForm sashTemp = new SashForm(sash, SWT.NONE);
		

		Text srcText = new Text(sashTemp, SWT.MULTI | SWT.H_SCROLL);
		srcText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		ScrolledComposite scroll = new ScrolledComposite(sashTemp, SWT.H_SCROLL | SWT.V_SCROLL);
		scroll.setLayout(new GridLayout(1, false));
		scroll.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Composite area = new Composite(scroll, SWT.NONE);
		area.setLayout(new FillLayout());

		scroll.setContent(area);
		scroll.setMinSize(100, 100);
		scroll.setExpandHorizontal(true);
		scroll.setExpandVertical(true);
		scroll.setAlwaysShowScrollBars(true);

		area.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				Point size = area.computeSize(SWT.DEFAULT, SWT.DEFAULT);
//				size.x += 100;
//				size.y += 100;
				scroll.setMinSize(size);
				scroll.requestLayout();
				if (activeQualityIssue != null)
					activeQualityIssue.show();
			}
		});

		area.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		area.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		sash.setWeights(new int[] { 1, 5 });

		Label label = new Label(area, SWT.NONE);
		label.setFont(font);
		label.setText("Please select a file...");


//		Text paddleText = new Text(rightComp, SWT.MULTI | SWT.H_SCROLL);
//		paddleText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		
		final List moduleList = new List(topComp, SWT.BORDER | SWT.V_SCROLL);
		moduleList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Map<String, IModule> map = new HashMap<>();
		Map<String, File> mapFile = new HashMap<>();
		Map<String, java.util.List<QualityIssue>> issueTable = new HashMap<>();

		if (args.length == 0) {
			DirectoryDialog dialog = new DirectoryDialog(shell);
			args = new String[] { dialog.open() };
		}

		

		File src = new File(args[0]);
		File[] files;
		if (src.isFile())
			files = new File[] { src };
		else
			files = src.listFiles(f -> f.getName().endsWith(".java") && !f.getName().equals("ImageUtil.java"));

		PrintWriter log = new PrintWriter(new File(src.getParentFile(), "log.txt"));
		long time = System.currentTimeMillis();

		log.println("Start: " + new Date());

		for (File f : files)
			log.println("File: " + f.getAbsolutePath());

		Linter linter = new Linter();
		

		IModule m = IModule.create(args[0]);

		Java2Paddle parser = new Java2Paddle(src, e -> e.getName().equals("ImageUtil.java"), m);
		parser.parse();

		java.util.List<QualityIssue> issues = linter.analyse(m);
	
		// martelo para excluir metodos static void teste() 
		issues.removeIf(q -> 
				!q.getProcedure().is("INSTANCE") && 
				q.getProcedure().getReturnType().isVoid() &&
				q.getProcedure().getParameters().isEmpty());
//				q.getIssueType() == IssueType.USELESS_RETURN &&
//				!q.getProcedure().is("INSTANCE") && 
//				q.getProcedure().getParameters().isEmpty());

		java.util.List<IssueType> issueTypes = java.util.List.of(Arrays.copyOfRange(IssueType.values(), 5, 15));
				
		issues.removeIf(q -> !issueTypes.contains(q.getIssueType()));
		
//		System.out.println(issues.size() + ":\n" + issues);
		
		shell.setText("Sprinter - " + args[0] + " " + issues.size());
		for (File f : files) {

			// try {
//			parser.parse();
			java.util.List<QualityIssue> list = issues.stream().filter(
					i -> i.getProcedure().getNamespace().equals(f.getName().substring(0, f.getName().indexOf('.'))))
					.collect(Collectors.toList());
			if (!list.isEmpty()) {
				moduleList.add(f.getName());
				map.put(f.getName(), m);
				mapFile.put(f.getName(), f);
				issueTable.put(f.getName(), list);
			}
//			} catch (Exception e) {
//				System.err.println(f.getName() + " not included");
//			}
		}
		final List caseList = new List(topComp, SWT.BORDER | SWT.V_SCROLL);
		caseList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		ScrolledComposite scrollExp = new ScrolledComposite(topComp, SWT.H_SCROLL | SWT.V_SCROLL);
		FillLayout l = new FillLayout();
		l.marginHeight = 20;
		l.marginWidth = 20;
		
		scrollExp.setLayout(l);
		scrollExp.setMinSize(100, 100);
		scrollExp.setExpandHorizontal(true);
		scrollExp.setExpandVertical(true);
		explanation = new Link(scrollExp, SWT.NONE);
		
		

		ArrayList<QualityIssueHighlight> highlights = new ArrayList<QualityIssueHighlight>();

		moduleList.addSelectionListener(new SelectionAdapter() {
			int index = -1;

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (moduleList.getSelectionIndex() == -1 || moduleList.getSelectionIndex() == index)
					return;

				activeQualityIssue = null;
				index = moduleList.getSelectionIndex();
				explanation.setText("");
				caseList.removeAll();
				highlights.forEach(i -> i.remove());
				highlights.clear();
				topComp.setEnabled(false);
				area.getChildren()[0].dispose();
				
				String fileName = moduleList.getItem(moduleList.getSelectionIndex());
				String namespace = fileName.substring(0, fileName.indexOf('.'));
				IModule m = map.get(fileName);
				IClassWidget widget = serv.createClassWidget(area, m, namespace);
//				widget.getControl().setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
//				widget.getControl().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
				widget.setReadOnly(true);

				java.util.List<QualityIssue> issues = issueTable.get(fileName);// linter.analyse(map.get(fileName));
				String[] cases = new String[issues.size()];
				int i = 0;
				for (QualityIssue qIssue : issues) {
					cases[i++] = qIssue.getIssueTitle(); // CaseNames.getCaseName(qIssue.getIssueType());
					highlights.add(new QualityIssueHighlight(widget, scrollExp, qIssue));
				}
				caseList.setItems(cases);

				try {
					File file = mapFile.get(fileName);
					String charset = UniversalDetector.detectCharset(file);
					if (charset == null || !Charset.isSupported(charset))
						charset = "UTF-8";
					Scanner scanner = new Scanner(file, charset);
					String src = "";
					while (scanner.hasNextLine())
						src += scanner.nextLine() + "\n";
					scanner.close();
					srcText.setText(src);
					srcText.requestLayout();
//						paddleText.setText(m.toString());
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				
				area.requestLayout();
				caseList.requestLayout();
				topComp.setEnabled(true);

				Point size = area.computeSize(SWT.DEFAULT, SWT.DEFAULT);
//				size.x += 100;
//				size.y += 100;
				scroll.setMinSize(size);
				scroll.requestLayout();
				scroll.setOrigin(0, 0);
			}
		});

		caseList.addSelectionListener(new SelectionAdapter() {
			
			public void widgetSelected(SelectionEvent e) {
				explanation.setText("");
				
				if (caseList.getSelectionIndex() == -1) {
					return;
				}

				if (activeQualityIssue != null)
					activeQualityIssue.remove();
				
				activeQualityIssue = highlights.get(caseList.getSelectionIndex());

				explanation.dispose();
				explanation = activeQualityIssue.generateExplanation();
				scrollExp.setContent(explanation);
				Point size = explanation.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				scrollExp.setMinSize(size);
				scrollExp.requestLayout();
				
				activeQualityIssue.generateDecorations();
				int decY = activeQualityIssue.getYLocation();
				int sY = scroll.toDisplay(0,0).y;
				int sYmax = scroll.toDisplay(0,scroll.getClientArea().height).y;
				
				if( decY < sY)
					scroll.setOrigin(0, Math.max(0, scroll.getOrigin().y - Math.abs(scroll.getOrigin().y - decY)) - 50);
				else if(decY > sYmax)
					scroll.setOrigin(0, scroll.getOrigin().y + Math.abs(scroll.getOrigin().y - decY));
				
				activeQualityIssue.show();
			}
		});
		
		topComp.setWeights(new int[] { 1, 2, 5 });

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
		log.println("Seconds: " + (System.currentTimeMillis() - time) / 1000);
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
