package pt.iscte.paddle.codequality.cases;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;

import pt.iscte.paddle.codequality.misc.Category;
import pt.iscte.paddle.javardise.service.IClassWidget;
import pt.iscte.paddle.javardise.service.ICodeDecoration;
import pt.iscte.paddle.javardise.service.IJavardiseService;
import pt.iscte.paddle.javardise.service.IWidget;
import pt.iscte.paddle.javardise.util.HyperlinkedText;
import pt.iscte.paddle.model.IControlStructure;
import pt.iscte.paddle.model.IReturn;
import pt.iscte.paddle.model.cfg.INode;

public class UnreachableCode extends BadCodeCase {

	private final List<INode> deadNodes;

	public UnreachableCode(String explanation, List<INode> deadNodes) {
		super(Category.UNREACHABLE_CODE, explanation, null);
		this.deadNodes = deadNodes;
	}

	public List<INode> getDeadNodes() {
		return deadNodes;
	}

	@Override
	public void generateComponent(IClassWidget widget, Composite comp, int style) {
		IWidget w;
		if(deadNodes.get(0).getElement() instanceof IReturn) {
			w = IJavardiseService.getWidget(deadNodes.get(1).getElement());
		}
		else w = IJavardiseService.getWidget(deadNodes.get(0).getElement());

		IWidget[] elements = new IWidget[deadNodes.size()];

		for (int i = 0; i < elements.length; i++) {
			//			IControlStructure s = deadNodes.get(i).getElement().getProperty(IControlStructure.class);
			//			if(s != null) {
			//				elements[i] = IJavardiseService.getWidget(s);
			//			}
			//			else 
			elements[i] = IJavardiseService.getWidget(deadNodes.get(i).getElement());
		}

		Color cyan = Display.getDefault().getSystemColor(SWT.COLOR_CYAN);
		ICodeDecoration<Canvas> dec = w.addRegionMark(cyan, elements);
		ICodeDecoration<Text> note = w.addNote("Dwadwa", ICodeDecoration.Location.RIGHT);
		dec.show();
		note.show();
		super.addDecoration(note);
		super.addDecoration(dec);

		generateExplanation(widget, comp, style);
	}

	@Override
	protected void generateExplanation(IClassWidget widget, Composite comp, int style) {
		IWidget a = IJavardiseService.getWidget(deadNodes.get(0).getElement());
		Link link = new HyperlinkedText(null)
				.words("There is a")
				.link(" return statement ", () -> {
					Image img = new Image(Display.getDefault(), "arrow.png");
					ICodeDecoration<Label> i = a.addImage(img, ICodeDecoration.Location.LEFT);
					i.show();
					getDecorations().add(i);
					ICodeDecoration<Canvas> d0 = a.addMark(Display.getDefault().getSystemColor(SWT.COLOR_RED));
					ICodeDecoration<Text> d1 = a.addNote("Causes the unreachable code!", ICodeDecoration.Location.RIGHT);
					d1.show();
					d0.show();
					getDecorations().add(d0);
					getDecorations().add(d1);
				})
				.words("that causes the non-execution of "+ deadNodes.size() + " of your code instructions.") 
				.words(" Such code will never "
						+ "be executed and becomes obsolete, unnecessary and therefore should be avoided at all cost")
				.create(comp, SWT.WRAP | SWT.V_SCROLL);
		link.requestLayout();
	}
}
