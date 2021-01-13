package pt.paddle.linter.gui.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;

import pt.iscte.javardise.api.ICodeDecoration;
import pt.iscte.javardise.api.IWidget;
import pt.iscte.javardise.javaeditor.api.HyperlinkedText;
import pt.iscte.javardise.javaeditor.api.IClassWidget;
import pt.iscte.javardise.javaeditor.api.IJavardiseService;
import pt.iscte.paddle.linter.cases.base.MultipleOccurrencesIssue;
import pt.iscte.paddle.linter.cases.base.QualityIssue;
import pt.iscte.paddle.linter.cases.base.SingleOcurrenceIssue;
import pt.iscte.paddle.linter.issues.BooleanCheck;
import pt.iscte.paddle.linter.issues.BooleanReturnCheck;
import pt.iscte.paddle.linter.issues.Contradiction;
import pt.iscte.paddle.linter.issues.Duplicate;
import pt.iscte.paddle.linter.issues.DuplicateGuard;
import pt.iscte.paddle.linter.issues.DuplicateMethodCall;
import pt.iscte.paddle.linter.issues.EmptyBlock;
import pt.iscte.paddle.linter.issues.FaultyProcedureCall;
import pt.iscte.paddle.linter.issues.IfEmptyElse;
import pt.iscte.paddle.linter.issues.MagicNumber;
import pt.iscte.paddle.linter.issues.NegativeBooleanCheck;
import pt.iscte.paddle.linter.issues.PositiveBooleanCheck;
import pt.iscte.paddle.linter.issues.Tautology;
import pt.iscte.paddle.linter.issues.UnreachableCode;
import pt.iscte.paddle.linter.issues.UselessReturn;
import pt.iscte.paddle.linter.issues.UselessSelfAssignment;
import pt.iscte.paddle.linter.issues.UselessVariableAssignment;
import pt.iscte.paddle.model.IControlStructure;
import pt.iscte.paddle.model.IExpression;
import pt.iscte.paddle.model.ILiteral;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IProcedureCall;
import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.model.IReturn;
import pt.iscte.paddle.model.ISelection;
import pt.iscte.paddle.model.IVariableExpression;

public class QualityIssueHighlight {

	private List<ICodeDecoration> decorations = new ArrayList<ICodeDecoration>();

	private IClassWidget widget;
	private QualityIssue issue;

	IJavardiseService serv = ServiceLoader.load(IJavardiseService.class).findFirst().get();

	public QualityIssueHighlight(IClassWidget widget, QualityIssue issue) {
		this.widget = widget;
		this.issue = issue;
	}

	protected Color getColor() {
		switch (getIssue().getClassification()) {
		case AVERAGE: {
			return Display.getDefault().getSystemColor(SWT.COLOR_MAGENTA);
		}
		case LIGHT: {
			return Display.getDefault().getSystemColor(SWT.COLOR_CYAN);
		}
		case SERIOUS: {
			return Display.getDefault().getSystemColor(SWT.COLOR_RED);
		}
		}
		return null;
	}

	private void decorate(ICodeDecoration<Canvas> d) {
		Optional<ICodeDecoration> o = decorations.stream().filter(e -> e.getControl() == d.getControl()).findAny();
		if (o.isEmpty()) {
			decorations.add(d);
			d.show();
		}
	}

	private HyperlinkedText createLink() {
		return new HyperlinkedText((list) -> {
			list.forEach(e -> {
				ICodeDecoration<Canvas> d = serv.getWidget(e).addMark(getColor());
				decorate(d);
			});
			return null;
		});
	}

	public Control generateExplanation(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		FillLayout l = new FillLayout();
		l.marginHeight = 15;
		l.marginWidth = 15;
		comp.setLayout(l);

		HyperlinkedText desc = createLink();
		if (getIssue() instanceof NegativeBooleanCheck) {
			NegativeBooleanCheck c = (NegativeBooleanCheck) getIssue();
			desc.words("You want to check if ").link(c.getExpression().toString(), c.getExpression())
					.words(" is not true.").skipline()
					.line("Problem: Unnecessarily extensive code may affect readability.").newline()
					.line("Suggestion: use the negation operator (!), making the expression shorter.");

		}
		if (getIssue() instanceof PositiveBooleanCheck) {
			PositiveBooleanCheck c = (PositiveBooleanCheck) getIssue();
			ILiteral lit = c.getLiteral();
			IExpression exp = c.getExpression();
			desc.words("The result of the expression ").link(exp.toString(), exp).words(" is a boolean value:")
					.skipline().bullet().words("if true, comparing to ").link(lit.toString(), lit)
					.words(" will always result in true;").skipline().bullet()
					.words("if false, it will always result in false.").skipline()
					.line("Problem: Unnecessarily extensive code may affect readability.").newline()
					.line("Suggestion: simplify the expression, making it shorter.");
		} else if (getIssue() instanceof BooleanReturnCheck) {
			IExpression guard = ((BooleanReturnCheck) getIssue()).getGuard();
			desc.words("The expression ").link(guard.toString(), guard).line(" will give the return result directly.").newline()
					.bullet().line("There is no need to check the value of a boolean variable before returning true or false.").newline()
					.bullet().line("The expression itself already represents the value meant to be returned.").newline()
					.line("Suggestion: return the expression directly instead of checking it's value and then returning true or false.");
		} else if (getIssue() instanceof Contradiction) {
			desc.words("The condition ").link(((Contradiction) getIssue()).getOccurrence().toString(), () -> {

			}).words(" represents a contradiction case. ")
					.words("\n\n - This means that this condition will allways be avaliated as false.")
					.words("\n - The program will never execute the code inside this condition, which is useless.")
					.words("\n\nSuggestion: Change this condition so that it isn't always true.");
		} else if (getIssue() instanceof Duplicate) {
			desc.words("The statement ").link(((Duplicate) getIssue()).getOccurrences().get(0).toString(), () -> {
			}).words(" was found in a condition and it's alternatives (elses).").words(
					"\n\n - This generates code duplication that should be avoided in order to maintain a good quality code.")
					.words("\n - Try extrating the duplicates from the condition blocks, this will help preventing code duplication.");
		} else if (getIssue() instanceof DuplicateGuard) {
			DuplicateGuard d = (DuplicateGuard) getIssue();
			desc.words("The expression ").link(d.getDuplicateExpression().toString(), d.getDuplicateExpression())
					.line(" was found duplicated inside the code block.").newline()
					.bullet().line("The expression is true inside the block.").newline()
					.bullet().line("Double-checking the expression is unnessessary and makes code longer.").newline()
					.line("Suggestion: Remove the second verification.");
		} else if (getIssue() instanceof DuplicateMethodCall) {
			desc.words("The function ").link(((DuplicateMethodCall) getIssue()).getOccurrences().get(0).toString(), () -> {
				ICodeDecoration<Canvas> d = serv
						.getWidget(
								((IProcedureCall) ((DuplicateMethodCall) getIssue()).getOccurrences().get(0)).getProcedure())
						.addMark(getColor());
				d.show();
				decorations.add(d);
			}).words(" was called more than once. \n\n - Its arguments values aren't changed between calls.")
					.words("\n - The method doesn't change any variable that affects the program execution flow.")
					.words("\n - This means that both calls will result in the same.")
					.words("\n - There is no point on having two "
							+ ((DuplicateMethodCall) getIssue()).getOccurrences().get(0)
							+ " calls that return the exact same result.")
					.words("\n\nSuggestion: \n\nSince both calls result in the same, you should remove one of them.");
		} else if (getIssue() instanceof EmptyBlock) {
			desc.words("The highlighted block has no actions inside. \n ").words("\n - If the condition ")
					.link(((ISelection) ((SingleOcurrenceIssue) getIssue()).getOccurrence()).getGuard().toString(), () -> {
					}).words(" is true, nothing will happen.")
					.words("\n - Empty code blocks don't add actions to the program execution.")
					.words("\n\nSuggestion:\n\n Either Add some logic to the if block, or remove it.");
		} else if (getIssue() instanceof FaultyProcedureCall) {
			IProcedureCall call = (IProcedureCall) ((SingleOcurrenceIssue) getIssue()).getOccurrence();
			desc.words(" doesn't have any impact on the program.")
					.words("\n\n- The call " + call + " doesn't change it's arguments .")
					.words("\n- The method's return value ")
					.link(call.getProcedure().getReturnType().toString(), () -> {

						ICodeDecoration<Canvas> d2 = widget.getProcedure((IProcedure) call.getProcedure())
								.getReturnType().addMark(getColor());
						ICodeDecoration<Text> d3 = widget.getProcedure((IProcedure) call.getProcedure()).getReturnType()
								.addNote("Return not used!", ICodeDecoration.Location.TOP);
						d2.show();
						d3.show();
						decorations.add(d2);
						decorations.add(d3);
					}).words(" was not used.")
					.words("\n- The method call doesn't change any variables that can affect the program's execution flow.")
					.words("\n- With this being, the method call has no impact in the execution of the program.")
					.words("\n\nSuggestion: \n- Either remove the call or think if it's return value should be used.");
		} else if (getIssue() instanceof MagicNumber) {
			MagicNumber n = (MagicNumber) getIssue();
			String magic = n.getMagicNumber().toString();
			desc.words("The highlighted number ").link(magic)
					.line(" doesn't really tell what it represents.").newline()
					.bullet().line("It can be hard for another programmer to look at the number "
							+ magic + " and figure out what it represents.").newline()
					.bullet().line("It can also lead to bugs because it's more difficult to change it in every place that it is used.").newline()
					.newline().line("Suggestion: replace the occurences of the number "
							+ magic + " with a variable with a suggestive name.").newline();
		} else if (getIssue() instanceof IfEmptyElse) {
			desc.words("The code was written in the else block and the if section was left empty.")
					.words("\n\n - This means that only the else block contains code that may be executed.")
					.words("\n - The empty if should be avoided, because empty code blocks don't contribute to the program and affect code readibility.")
					.words("\n - Conditions don't always need to be true.").words("\n\nSuggestion:")
					.words("\n\n Try using the negation (!) operator in the if condition and write the code directly inside the if block.");
		} else if (getIssue() instanceof Tautology) {
			desc.words("The condition ").link(((Tautology) getIssue()).getOccurrence().toString(), () -> {
			}).words(" represents a tautology case. ")
					.words("\n\n - This means that this condition will allways be avaliated as true.")
					.words("\n - The program will always execute the code inside this condition, which means that"
							+ " it is not necessary, or wrong.")
					.words("\n\nSuggestion: Change this condition so that it isn't always true.");
		} else if (getIssue() instanceof UnreachableCode) {
			IWidget a = serv.getWidget(((UnreachableCode) getIssue()).getOccurrences().get(0));
			desc.words("There is a").link(" return statement ", () -> {
				Image img = new Image(Display.getDefault(), "arrow.png");
				ICodeDecoration<Label> i = a.addImage(img, ICodeDecoration.Location.LEFT);
				i.show();
				decorations.add(i);
				ICodeDecoration<Text> d1 = a.addNote("Terminates the function execution!",
						ICodeDecoration.Location.RIGHT);
				d1.show();
				decorations.add(d1);
			}).words("that causes the function execution to end.\n").words("\n - The function ends after the ")
					.link(((UnreachableCode) getIssue()).getOccurrences().get(0).toString(), () -> {
					}).words(" statement.").words("\n - This means that code after that won't be executed.")
					.words("\n - The unreachable lines become useless because they will never run.")
					.words("\n\nSuggestion:")
					.words("\n\nChange the place of the " + ((UnreachableCode) getIssue()).getOccurrences().get(0).toString()
							+ " statement or add a proper condition before calling it, in order to avoid chunks of unreachable code.");
		} else if (getIssue() instanceof UselessSelfAssignment) {
			desc.words("The assignment ").link(((UselessSelfAssignment) getIssue()).getOccurrence().toString(), () -> {
			}).words(" means that the variable was assigned with the value that it already had. ")
					.words("\n\n - It is an useless assignment because it has no impact in the program.")
					.words("\n\n Suggestion: \n\n- Remove this useless assignment.");
		} else if (getIssue() instanceof UselessVariableAssignment) {
			desc.words("The assignment ").link(((UselessVariableAssignment) getIssue()).getOccurrence().toString(), () -> {
			}).words(" value was not used. ")
					.words("\n\n - Variables don't need to be initialized before being assigned with a value. ")
					.words("\n - The value was not used before the second assignment, which means that is as no impact. ")
					.words("\n - Unused values are useless to the program.")
					.words("\n\n Suggestion: \n\n- Remove this useless assignment because its value isn't used.");
		} else if (getIssue() instanceof UselessReturn) {
			desc.words("The marked return statement is considered to be useless.");
		}

		Link link = desc.create(comp, SWT.WRAP | SWT.V_SCROLL);
		link.setFont(new Font(Display.getDefault(), "Arial", 12, SWT.NORMAL));
		return comp;
	}

	public void generateHighlight() {
//		if(issue instanceof SingleOcurrenceIssue)
//			System.out.println("EL: " + ((SingleOcurrenceIssue) issue).getOccurrence());
//		else
//			System.out.println("ELs: " + ((MultipleOccurrencesIssue) issue).getOccurences());
		IWidget w = (getIssue() instanceof SingleOcurrenceIssue)
				? serv.getWidget(((SingleOcurrenceIssue) getIssue()).getOccurrence())
				: serv.getWidget(((MultipleOccurrencesIssue) getIssue()).getOccurrences().get(0));

		if (w == null)
			return;
		if (getIssue() instanceof MagicNumber) {
			IProgramElement occ = (IProgramElement) ((MultipleOccurrencesIssue) getIssue()).getOccurrences().get(0);
			w = serv.getWidget(occ);
			ICodeDecoration<Canvas> d = w.addMark(getColor());
			decorations.add(d);
		} else if (getIssue() instanceof MultipleOccurrencesIssue) {
			for (IProgramElement occ : ((MultipleOccurrencesIssue) getIssue()).getOccurrences()) {

				w = serv.getWidget(occ);
				ICodeDecoration<Canvas> d = w.addMark(getColor());
				decorations.add(d);
			}
		} else {
			ICodeDecoration<Canvas> d = w.addMark(getColor());
			decorations.add(d);
		}

		if (getIssue() instanceof PositiveBooleanCheck) {
			ICodeDecoration<Text> d2 = w.addNote("This expression alone will determine\n if the whole expression is true.", ICodeDecoration.Location.BOTTOM);
			decorations.add(d2);
		} else if (getIssue() instanceof NegativeBooleanCheck) {
			ICodeDecoration<Text> d2 = w.addNote("Should you check instead if not true?",
					ICodeDecoration.Location.RIGHT_BOTTOM);
			decorations.add(d2);
		} else if (getIssue() instanceof BooleanReturnCheck) {
			IExpression guard = ((BooleanReturnCheck) getIssue()).getGuard();
			String s = guard instanceof IVariableExpression ? "variable" : "expression";
			ICodeDecoration<Text> t = w.addNote("Shouldn't you\n return the " + s + "?",
					ICodeDecoration.Location.RIGHT);
			decorations.add(t);
		} else if (getIssue() instanceof Contradiction) {
			ICodeDecoration<Text> d2 = w.addNote("This condition will\n always be false!",
					ICodeDecoration.Location.RIGHT);
			decorations.add(d2);
		} else if (getIssue() instanceof Duplicate) {
			ICodeDecoration<Text> t = w.addNote("Couldn't this be\n anywhere else?", ICodeDecoration.Location.RIGHT);
			decorations.add(t);
		} else if (getIssue() instanceof DuplicateGuard) {
			ICodeDecoration<Text> d2 = w.addNote("Unnecessary double-check?", ICodeDecoration.Location.RIGHT);
			decorations.add(d2);
		} else if (getIssue() instanceof EmptyBlock) {
			ICodeDecoration<Text> d2 = w.addNote("Why is it empty?", ICodeDecoration.Location.RIGHT);
			decorations.add(d2);
		} else if (getIssue() instanceof FaultyProcedureCall) {
			ICodeDecoration<Text> t = w.addNote("Doesn't this function\ncall return something?",
					ICodeDecoration.Location.RIGHT);
			decorations.add(t);
		} else if (getIssue() instanceof MagicNumber) {
			for (IProgramElement i : ((MagicNumber) getIssue()).getOccurrences()) {

//				IProgramElement el = ((MagicNumber) issue).getOccurrences().get(i);
				w = serv.getWidget(i);
				if (w != null) {
					if (((MagicNumber) getIssue()).getOccurrences().indexOf(i) == 0) {
						ICodeDecoration<Text> d2 = w.addNote("What does this number \n represent?",
								ICodeDecoration.Location.TOP);
						decorations.add(d2);
					}
					else {
						ICodeDecoration<Canvas> d2 = w.addMark(getColor());
						decorations.add(d2);
					}
				}
			}
		} else if (getIssue() instanceof IfEmptyElse) {
			ICodeDecoration<Text> d2 = w.addNote("Couln't you use \n the '!' operator?",
					ICodeDecoration.Location.RIGHT);
			decorations.add(d2);
		} else if (getIssue() instanceof Tautology) {
			ICodeDecoration<Text> d2 = w.addNote("Isn't this always true?", ICodeDecoration.Location.RIGHT);
			decorations.add(d2);
		} else if (getIssue() instanceof UnreachableCode) {
			if (((UnreachableCode) getIssue()).getOccurrences().get(0) instanceof IReturn) {
				w = serv.getWidget(((UnreachableCode) getIssue()).getOccurrences().get(1));
			} else
				w = serv.getWidget(((UnreachableCode) getIssue()).getOccurrences().get(0));

			IWidget[] elements = new IWidget[((UnreachableCode) getIssue()).getOccurrences().size()];

			for (int i = 0; i < elements.length; i++) {
				IControlStructure s = ((UnreachableCode) getIssue()).getOccurrences().get(i)
						.getProperty(IControlStructure.class);
				if (s != null)
					elements[i] = serv.getWidget(s);
				else
					elements[i] = serv.getWidget(((UnreachableCode) getIssue()).getOccurrences().get(i));
			}

			ICodeDecoration<Canvas> dec = w.addRegionMark(getColor(), elements);
			ICodeDecoration<Text> d1 = w.addNote("This code won't \n be executed!", ICodeDecoration.Location.RIGHT);
			decorations.add(dec);
			decorations.add(d1);
		} else if (getIssue() instanceof UselessSelfAssignment) {
			ICodeDecoration<Text> d2 = w.addNote("What does this actually do?", ICodeDecoration.Location.RIGHT);
			decorations.add(d2);
		} else if (getIssue() instanceof UselessVariableAssignment) {
			ICodeDecoration<Text> d2 = w.addNote("This value won't be used!", ICodeDecoration.Location.RIGHT);
			decorations.add(d2);
		}
	}

	public void generateDecorations() {
		decorations.clear();
		generateHighlight();
	}

	public void show() {
		decorations.forEach(mark -> {
			mark.show();
		});
	}

	public void hide() {
		decorations.forEach(mark -> {
			mark.hide();
		});
	}

	public int getYLocation() {
		if (decorations.isEmpty())
			return 0;
		else
			return decorations.get(0).locationHeight();
	}

	public Control getControl() {
		return decorations.get(0).getControl();
	}

	public void remove() {
		decorations.forEach(mark -> {
			mark.delete();
		});
		decorations.clear();
	}

	public QualityIssue getIssue() {
		return issue;
	}

}
