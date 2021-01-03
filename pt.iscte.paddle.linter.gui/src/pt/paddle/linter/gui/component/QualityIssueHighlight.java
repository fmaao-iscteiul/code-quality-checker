package pt.paddle.linter.gui.component;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
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
import pt.iscte.paddle.linter.issues.EmptyLoop;
import pt.iscte.paddle.linter.issues.EmptySelection;
import pt.iscte.paddle.linter.issues.FaultyProcedureCall;
import pt.iscte.paddle.linter.issues.MagicNumber;
import pt.iscte.paddle.linter.issues.SelectionMisconception;
import pt.iscte.paddle.linter.issues.Tautology;
import pt.iscte.paddle.linter.issues.UnreachableCode;
import pt.iscte.paddle.linter.issues.UselessReturn;
import pt.iscte.paddle.linter.issues.UselessSelfAssignment;
import pt.iscte.paddle.linter.issues.UselessVariableAssignment;
import pt.iscte.paddle.model.IControlStructure;
import pt.iscte.paddle.model.ILoop;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IProcedureCall;
import pt.iscte.paddle.model.IProgramElement;
import pt.iscte.paddle.model.IReturn;
import pt.iscte.paddle.model.ISelection;

public class QualityIssueHighlight {

	private List<ICodeDecoration> decorations = new ArrayList<ICodeDecoration>();

	private IClassWidget widget;
	private Composite comp;
	private QualityIssue issue;

	IJavardiseService serv = ServiceLoader.load(IJavardiseService.class).findFirst().get();
	
	
	public QualityIssueHighlight(IClassWidget widget, Composite comp, QualityIssue issue) {
		this.widget = widget;
		this.comp = comp;
		this.issue = issue;
	}

	protected Color getColor() {
		switch (issue.getClassification()) {
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

	public Link generateExplanation() {
		
		Link link = null;
		if(issue instanceof BooleanCheck) {
			link = new HyperlinkedText(null)
					.words("The condition ")
					.link(((BooleanCheck) issue).getOccurrence().toString(), () -> {}) 
					.words(" could make use of the '!' operator. \n")
					.words("\n - It represents the comparision between a boolean variable and one of it's binary possible values (true or false).")
					.words("\n - It is more of a styling mather, but this can negatively affect the code readibility, due to the condition's length.")
					.words("\n\nSuggestion:\nTry using the negation (!) operator in order to improve readibility.")
					.create(comp, SWT.WRAP | SWT.V_SCROLL);
		} else if(issue instanceof BooleanReturnCheck) {
			link = new HyperlinkedText(null)
					.words("The ").link("if( "+((ISelection) ((BooleanReturnCheck) issue).getOccurrence()).getGuard().toString() + " )", ()->{}).words(" condition is unnecessary!\n ")
					.words("\n - There is no need to check the value of a boolean variable before returning true or false.")
					.words("\n - The condition itself already represents the value meant to be returned.")
					.words("\n\nSuggestion:\n\n Return the boolean variable instead of checking it's value and then returning true or false.")
					.create(comp, SWT.WRAP | SWT.SCROLL_LINE | SWT.V_SCROLL);
		} else if(issue instanceof Contradiction) {
			link = new HyperlinkedText(null)
					.words("The condition ").link(((Contradiction) issue).getOccurrence().toString(), () -> {

					})
					.words(" represents a contradiction case. ")
					.words("\n\n - This means that this condition will allways be avaliated as false.")
					.words("\n - The program will never execute the code inside this condition, which is useless.")
					.words("\n\nSuggestion: Change this condition so that it isn't always true.")
					.create(comp, SWT.WRAP | SWT.V_SCROLL);
		}
		else if(issue instanceof Duplicate) {
			link = new HyperlinkedText(null)
					.words("The statement ")
					.link(((Duplicate) issue).getOccurences().get(0).toString(), () -> {

					})
					.words(" was found in a condition and it's alternatives (elses).")
					.words("\n\n - This generates code duplication that should be avoided in order to maintain a good quality code.")
					.words("\n - Try extrating the duplicates from the condition blocks, this will help preventing code duplication.")
					.create(comp, SWT.WRAP | SWT.SCROLL_LINE | SWT.V_SCROLL);
		}
		else if(issue instanceof DuplicateGuard) {
			link = new HyperlinkedText(null)
					.words("The condition ").link(((DuplicateGuard) issue).getOccurences().get(0).toString(), () -> {})
					.words(" was found duplicated inside the code block.")
					.words("\n\n - Neither of the variables used in the conditions had their values changed in between.")
					.words("\n - Double checking a condition which parts don't change in between checks, will have the same result.")
					.words("\n - Since the result is the same, it doesn't need to be duplicated.")
					.words("\n\nSolution: \n\n Remove the second check since it isn't necessary.")
					.create(comp, SWT.WRAP | SWT.V_SCROLL);
		}
		else if(issue instanceof DuplicateMethodCall) {
			link = new HyperlinkedText(null)
					.words("The function ")
					.link(((DuplicateMethodCall) issue).getOccurences().get(0).toString(), () -> {
						ICodeDecoration<Canvas> d = serv.getWidget(((IProcedureCall) ((DuplicateMethodCall) issue).getOccurences().get(0)).getProcedure()).addMark(getColor());
						d.show();
						decorations.add(d);
					})
					.words(" was called more than once. \n\n - Its arguments values aren't changed between calls.")
					.words("\n - The method doesn't change any variable that affects the program execution flow.")
					.words("\n - This means that both calls will result in the same.")
					.words("\n - There is no point on having two " + ((DuplicateMethodCall) issue).getOccurences().get(0) + " calls that return the exact same result.")
					.words("\n\nSuggestion: \n\nSince both calls result in the same, you should remove one of them.")
					.create(comp, SWT.WRAP | SWT.V_SCROLL);
		}
		else if(issue instanceof EmptyLoop) {
			link = new HyperlinkedText(null)
					.words("The highlighted loop block has no actions inside. \n ")
					.words("\n - If the condition ").link(((ILoop) ((SingleOcurrenceIssue) issue).getOccurrence()).getGuard().toString(), ()->{})
					.words(" is true, nothing but empty iterations will happen.")
					.words("\n - Empty code blocks don't add actions to the program execution.")
					.words("\n\nSuggestion: \n\n You should avoid empty blocks by adding some logic, or removing them.")
					.create(comp, SWT.WRAP | SWT.V_SCROLL);
		}
		else if(issue instanceof EmptySelection) {
			link = new HyperlinkedText(null)
					.words("The highlighted if block has no actions inside. \n ")
					.words("\n - If the condition ").link(((ISelection) ((SingleOcurrenceIssue) issue).getOccurrence()).getGuard().toString(), ()->{})
					.words(" is true, nothing will happen.")
					.words("\n - Empty code blocks don't add actions to the program execution.")
					.words("\n\nSuggestion:\n\n Either Add some logic to the if block, or remove it.")
					.create(comp, SWT.WRAP | SWT.V_SCROLL);
		}
		else if(issue instanceof FaultyProcedureCall) {
			IProcedureCall call = (IProcedureCall) ((SingleOcurrenceIssue) issue).getOccurrence();
			link = new HyperlinkedText(null)
					.words("The method ")
					.link(call.toString(), () -> {
						ICodeDecoration<Canvas> d0 = widget.getProcedure((IProcedure) call.getProcedure()).getMethodName().addMark(getColor());
						d0.show();
						decorations.add(d0);
					})
					.words(" doesn't have any impact on the program.")
					.words("\n\n- The call " + call + " doesn't change it's arguments .")
					.words("\n- The method's return value ")
					.link(call.getProcedure().getReturnType().toString(), () -> {

						ICodeDecoration<Canvas> d2 = widget.getProcedure((IProcedure) call.getProcedure()).getReturnType().addMark(getColor());
						ICodeDecoration<Text> d3 = widget.getProcedure((IProcedure) call.getProcedure()).getReturnType().addNote("Return not used!", ICodeDecoration.Location.TOP);
						d2.show();
						d3.show();
						decorations.add(d2);
						decorations.add(d3);
					})
					.words(" was not used.")
					.words("\n- The method call doesn't change any variables that can affect the program's execution flow.")
					.words("\n- With this being, the method call has no impact in the execution of the program.")
					.words("\n\nSuggestion: \n- Either remove the call or think if it's return value should be used.")
					.create(comp, SWT.WRAP | SWT.V_SCROLL);
		}
		else if(issue instanceof MagicNumber) {
			link = new HyperlinkedText(null)
					.words("The highlighted number " ).link(((MagicNumber) issue).getOccurences().get(0).toString(), () -> {
					})
					.words(" doesn't really tell what it stands for.").newline()
					.line("\n\n - It can be hard for another programmer to look at the number " + ((MagicNumber) issue).getOccurences().get(0)  + " and figure out what it represents.")
					.line("\n - It can also lead to bugs because it's more difficult to change it in every place that it is used.")
					.newline()
					.line("Suggestion:")
					.line("\t- Try replacing this the number " + ((MagicNumber) issue).getOccurences().get(0) + " with a well named variable.")
					.create(comp, SWT.WRAP | SWT.V_SCROLL);
		}
		else if(issue instanceof SelectionMisconception) {
			link = new HyperlinkedText(null)
					.words("The code was written in the else block and the if section was left empty.")
					.words("\n\n - This means that only the else block contains code that may be executed.")
					.words("\n - The empty if should be avoided, because empty code blocks don't contribute to the program and affect code readibility.")
					.words("\n - Conditions don't always need to be true.")
					.words("\n\nSuggestion:")
					.words("\n\n Try using the negation (!) operator in the if condition and write the code directly inside the if block.")
					.create(comp, SWT.WRAP | SWT.V_SCROLL);
		}
		else if(issue instanceof Tautology) {
			link = new HyperlinkedText(null)
					.words("The condition ").link(((Tautology) issue).getOccurrence().toString(), () -> {}).words(" represents a tautology case. ")
					.words("\n\n - This means that this condition will allways be avaliated as true.")
					.words("\n - The program will always execute the code inside this condition, which means that"
							+ " it is not necessary, or wrong.")
					.words("\n\nSuggestion: Change this condition so that it isn't always true.")
					.create(comp, SWT.WRAP | SWT.V_SCROLL);
		}
		else if(issue instanceof UnreachableCode) {
			IWidget a = serv.getWidget(((UnreachableCode) issue).getOccurences().get(0));
			link = new HyperlinkedText(null)
					.words("There is a")
					.link(" return statement ", () -> {
						Image img = new Image(Display.getDefault(), "arrow.png");
						ICodeDecoration<Label> i = a.addImage(img, ICodeDecoration.Location.LEFT);
						i.show();
						decorations.add(i);
						//					ICodeDecoration<Canvas> d0 = a.addMark(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_MAGENTA));
						ICodeDecoration<Text> d1 = a.addNote("Terminates the function execution!", ICodeDecoration.Location.RIGHT);
						d1.show();
						//					d0.show();
						//					getDecorations().add(d0);
						decorations.add(d1);
					})
					.words("that causes the function execution to end.\n")
					.words("\n - The function ends after the ").link(((UnreachableCode) issue).getOccurences().get(0).toString(), ()->{}).words(" statement.")
					.words("\n - This means that code after that won't be executed.")
					.words("\n - The unreachable lines become useless because they will never run.")
					.words("\n\nSuggestion:")
					.words("\n\nChange the place of the "+((UnreachableCode) issue).getOccurences().get(0).toString()+" statement or add a proper condition before calling it, in order to avoid chunks of unreachable code.")
					.create(comp, SWT.WRAP | SWT.V_SCROLL);
		}
		else if(issue instanceof UselessSelfAssignment) {
			link = new HyperlinkedText(null)
					.words("The assignment ").link(((UselessSelfAssignment) issue).getOccurrence().toString(), () -> {
					})
					.words(" means that the variable was assigned with the value that it already had. ")
					.words("\n\n - It is an useless assignment because it has no impact in the program.")
					.words("\n\n Suggestion: \n\n- Remove this useless assignment.")
					.create(comp, SWT.WRAP | SWT.V_SCROLL);
		}
		else if(issue instanceof UselessVariableAssignment) {
			link = new HyperlinkedText(null)
					.words("The assignment ").link(((UselessVariableAssignment) issue).getOccurrence().toString(), () -> {
					})
					.words(" value was not used. ")
					.words("\n\n - Variables don't need to be initialized before being assigned with a value. ")
					.words("\n - The value was not used before the second assignment, which means that is as no impact. ")
					.words("\n - Unused values are useless to the program.")
					.words("\n\n Suggestion: \n\n- Remove this useless assignment because its value isn't used.")
					.create(comp, SWT.WRAP | SWT.V_SCROLL);
		}
		else if(issue instanceof UselessReturn) {
			link = new HyperlinkedText(null)
					.words("The marked return statement is considered to be useless.")
					.create(comp, SWT.WRAP | SWT.V_SCROLL);
		}

		if(link != null) {
			link.setFont(new Font(Display.getDefault(), "Arial", 12, SWT.NORMAL));
			link.requestLayout();
		}
		
		return link;
	}

	public void generateHighlight() {
		if(issue instanceof SingleOcurrenceIssue)
			System.out.println("EL: " + ((SingleOcurrenceIssue) issue).getOccurrence());
		else
			System.out.println("ELs: " + ((MultipleOccurrencesIssue) issue).getOccurences());
		IWidget w = (issue instanceof SingleOcurrenceIssue) 
				? serv.getWidget(((SingleOcurrenceIssue) issue).getOccurrence()) 
						: serv.getWidget(((MultipleOccurrencesIssue) issue).getOccurences().get(0));

				if(w == null) return;
				if(issue instanceof MultipleOccurrencesIssue) {
					for (IProgramElement occ : ((MultipleOccurrencesIssue) issue).getOccurences()) {

						w = serv.getWidget(occ);
						ICodeDecoration<Canvas> d = w.addMark(getColor());
						decorations.add(d);
					}
				} else {
					ICodeDecoration<Canvas> d = w.addMark(getColor());
					decorations.add(d);
				}

				if(issue instanceof BooleanCheck) {
					ICodeDecoration<Text> d2 = w.addNote("Maybe use\n the ! operator?", ICodeDecoration.Location.RIGHT);
					decorations.add(d2);
				}
				else if(issue instanceof BooleanReturnCheck) {
					ICodeDecoration<Text> t = w.addNote("Shouldn't you\n return the variable?", ICodeDecoration.Location.RIGHT);
					decorations.add(t);
				} 
				else if(issue instanceof Contradiction) {
					ICodeDecoration<Text> d2 = w.addNote("This condition will\n always be false!", ICodeDecoration.Location.RIGHT);
					decorations.add(d2);
				}
				else if(issue instanceof Duplicate) {
					ICodeDecoration<Text> t = w.addNote("Couldn't this be\n anywhere else?", ICodeDecoration.Location.RIGHT);
					decorations.add(t);
				}
				else if(issue instanceof DuplicateGuard) {
					ICodeDecoration<Text> d2 = w.addNote("Unnecessary double-check?", ICodeDecoration.Location.RIGHT);
					decorations.add(d2);
				}
				else if(issue instanceof EmptyLoop) {
					ICodeDecoration<Text> d2 = w.addNote("Why is this loop empty?", ICodeDecoration.Location.RIGHT);
					decorations.add(d2);
				}
				else if(issue instanceof EmptySelection) {
					ICodeDecoration<Text> d2 = w.addNote("Why is it empty?", ICodeDecoration.Location.RIGHT);
					decorations.add(d2);
				}
				else if(issue instanceof FaultyProcedureCall) {
					ICodeDecoration<Text> t = w.addNote("Doesn't this function\ncall return something?", ICodeDecoration.Location.RIGHT);
					decorations.add(t);
				}
				else if(issue instanceof MagicNumber) {
					for (IProgramElement el : ((MagicNumber) issue).getOccurrences()) {
						w = serv.getWidget(el);
						if(w != null) {
							if(((MagicNumber) issue).getOccurrences().indexOf(el) == 0) {
								ICodeDecoration<Text> d2 = w.addNote("What does this number \n represent?", ICodeDecoration.Location.RIGHT);
								decorations.add(d2);
							}
						}
					}
				}
				else if(issue instanceof SelectionMisconception) {
					ICodeDecoration<Text> d2 = w.addNote("Couln't you use \n the '!' operator?", ICodeDecoration.Location.RIGHT);
					decorations.add(d2);
				}
				else if(issue instanceof Tautology) {
					ICodeDecoration<Text> d2 = w.addNote("Isn't this always true?", ICodeDecoration.Location.RIGHT);
					decorations.add(d2);
				}
				else if(issue instanceof UnreachableCode) {
					if(((UnreachableCode) issue).getOccurences().get(0) instanceof IReturn) {
						w = serv.getWidget(((UnreachableCode) issue).getOccurences().get(1));
					}
					else w = serv.getWidget(((UnreachableCode) issue).getOccurences().get(0));

					IWidget[] elements = new IWidget[((UnreachableCode) issue).getOccurences().size()];

					for (int i = 0; i < elements.length; i++) {
						IControlStructure s = ((UnreachableCode) issue).getOccurences().get(i).getProperty(IControlStructure.class);
						if(s != null) elements[i] = serv.getWidget(s);
						else elements[i] = serv.getWidget(((UnreachableCode) issue).getOccurences().get(i));
					}

					ICodeDecoration<Canvas> dec = w.addRegionMark(getColor(), elements);
					ICodeDecoration<Text> d1 = w.addNote("This code won't \n be executed!", ICodeDecoration.Location.RIGHT);
					decorations.add(dec);
					decorations.add(d1);
				}
				else if(issue instanceof UselessSelfAssignment) {
					ICodeDecoration<Text> d2 = w.addNote("What does this actually do?", ICodeDecoration.Location.RIGHT);
					decorations.add(d2);
				}
				else if(issue instanceof UselessVariableAssignment) {
					ICodeDecoration<Text> d2 = w.addNote("This value won't be used!", ICodeDecoration.Location.RIGHT);
					decorations.add(d2);
				}
	}

	public void generateDecorations() {
		decorations.clear();
//		generateExplanation();
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
		if(decorations.isEmpty())
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
	}

	
}
