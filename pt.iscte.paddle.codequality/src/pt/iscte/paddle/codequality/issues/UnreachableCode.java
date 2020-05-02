package pt.iscte.paddle.codequality.issues;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;

import pt.iscte.paddle.codequality.cases.base.MultipleOccurrencesIssue;
import pt.iscte.paddle.codequality.misc.Classification;
import pt.iscte.paddle.codequality.misc.IssueType;
import pt.iscte.paddle.javardise.service.IClassWidget;
import pt.iscte.paddle.javardise.service.ICodeDecoration;
import pt.iscte.paddle.javardise.service.IJavardiseService;
import pt.iscte.paddle.javardise.service.IWidget;
import pt.iscte.paddle.javardise.util.HyperlinkedText;
import pt.iscte.paddle.model.IControlStructure;
import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.model.IReturn;

public class UnreachableCode extends MultipleOccurrencesIssue {

	public UnreachableCode(List<IProgramElement> occurrences) {
		super(IssueType.UNREACHABLE_CODE, Classification.SERIOUS, occurrences);
	}

	@Override
	public void generateComponent(IClassWidget widget, Composite comp, int style) {
		IWidget w;
		if(occurrences.get(0) instanceof IReturn) {
			w = IJavardiseService.getWidget(occurrences.get(1));
		}
		else w = IJavardiseService.getWidget(occurrences.get(0));

		IWidget[] elements = new IWidget[occurrences.size()];

		for (int i = 0; i < elements.length; i++) {
			IControlStructure s = occurrences.get(i).getProperty(IControlStructure.class);
			if(s != null) elements[i] = IJavardiseService.getWidget(s);
			else elements[i] = IJavardiseService.getWidget(occurrences.get(i));
		}

		ICodeDecoration<Canvas> dec = w.addRegionMark(getColor(), elements);
		dec.show();
		ICodeDecoration<Text> d1 = w.addNote("This code won't \n be executed!", ICodeDecoration.Location.RIGHT);
		d1.show();
		super.addDecoration(dec);
		super.addDecoration(d1);

		generateExplanation(widget, comp, style);
	}

	@Override
	protected void generateExplanation(IClassWidget widget, Composite comp, int style) {
		IWidget a = IJavardiseService.getWidget(occurrences.get(0));
		Link link = new HyperlinkedText(null)
				.words("Issue:\n\n")
				.words("There is a")
				.link(" return statement ", () -> {
					Image img = new Image(Display.getDefault(), "arrow.png");
					ICodeDecoration<Label> i = a.addImage(img, ICodeDecoration.Location.LEFT);
					i.show();
					getDecorations().add(i);
					//					ICodeDecoration<Canvas> d0 = a.addMark(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_MAGENTA));
					ICodeDecoration<Text> d1 = a.addNote("Terminates the function execution!", ICodeDecoration.Location.RIGHT);
					d1.show();
					//					d0.show();
					//					getDecorations().add(d0);
					getDecorations().add(d1);
				})
				.words("that causes the function execution to end.\n")
				.words("\n - The function ends after the ").link(occurrences.get(0).toString(), ()->{}).words(" statement.")
				.words("\n - This means that code after that won't be executed.")
				.words("\n - The unreachable lines become useless because they will never run.")
				.words("\n\nSuggestion:")
				.words("\n\nChange the place of the "+occurrences.get(0).toString()+" statement or add a proper condition before calling it, in order to avoid chunks of unreachable code.")
				.create(comp, SWT.WRAP | SWT.V_SCROLL);
		link.requestLayout();
	}
}
