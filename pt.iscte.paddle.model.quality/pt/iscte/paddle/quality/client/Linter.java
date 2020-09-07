package pt.iscte.paddle.quality.client;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.model.IModule;
import pt.iscte.paddle.model.cfg.IControlFlowGraph;
import pt.iscte.paddle.quality.cases.base.CodeAnalyser;
import pt.iscte.paddle.quality.cases.base.QualityIssue;
import pt.iscte.paddle.quality.misc.BadCodeAnalyser;
import pt.iscte.paddle.quality.visitors.DuplicateBranchGuard;
import pt.iscte.paddle.quality.visitors.Loop;
import pt.iscte.paddle.quality.visitors.MagicNumbers;
import pt.iscte.paddle.quality.visitors.ProcedureCall;
import pt.iscte.paddle.quality.visitors.Return;
import pt.iscte.paddle.quality.visitors.Selection;
import pt.iscte.paddle.quality.visitors.Unreachable;
import pt.iscte.paddle.quality.visitors.UselessAssignment;

public class Linter {

	private ArrayList<Class<? extends CodeAnalyser>> visitors = new ArrayList<>();

	public Linter() {
		loadAllVisitors();
	}

	@SafeVarargs
	public Linter(Class<? extends CodeAnalyser>... issueAnalyser) {
		for (Class<? extends CodeAnalyser> class1 : issueAnalyser) {
			this.visitors.add(class1);
		}
	}

	private void loadAllVisitors() {
		this.visitors.add(Selection.class);
		this.visitors.add(Loop.class);
//		this.visitors.add(MagicNumbers.class);
		this.visitors.add(UselessAssignment.class);
		this.visitors.add(Unreachable.class);
		this.visitors.add(DuplicateBranchGuard.class);
		this.visitors.add(ProcedureCall.class);
		this.visitors.add(Return.class);
	}

	public List<QualityIssue> analyse(IModule... modules) {
		ArrayList<QualityIssue> caughtIssues = new ArrayList<>();
		for (IModule module : modules) {
			MagicNumbers mNumbers = new MagicNumbers();
			module.getProcedures().forEach(proc -> {
				proc.accept(mNumbers);
				IControlFlowGraph cfg = proc.generateCFG();
				cfg.getNodes().forEach(n -> System.out.println(n));
				visitors.forEach(visitor -> {
					CodeAnalyser analyser;
					try {
						analyser = visitor.getConstructor().newInstance();
						if(IVisitor.class.isAssignableFrom(visitor)) 
							proc.accept((IVisitor) analyser);
						else
							((BadCodeAnalyser) analyser).analyse(cfg);
						caughtIssues.addAll(analyser.getQualityIssues());
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
							| InvocationTargetException | NoSuchMethodException | SecurityException e1) {
						e1.printStackTrace();
					}
				});
			});
			caughtIssues.addAll(mNumbers.getQualityIssues());
		}
		return caughtIssues;
	}

	//	public void register(QualityIssue catchedCase) {
	//		// TODO add classification sorting.
	//		this.caughtIssues.add(catchedCase);
	//		this.caughtIssues.sort(new Comparator<QualityIssue>() {
	//			@Override
	//			public int compare(QualityIssue o1, QualityIssue o2) {
	//				if(o1.getClassification().equals(o2.getClassification()) ) return 0;
	//				else if(o1.getClassification().equals(Classification.SERIOUS) && o2.getClassification().equals(Classification.LIGHT)) return -1;
	//				else if(o1.getClassification().equals(Classification.LIGHT) && o2.getClassification().equals(Classification.SERIOUS)) return 1;
	//				else if(o1.getClassification().equals(Classification.SERIOUS) && o2.getClassification().equals(Classification.AVERAGE)) return -1;
	//				else if(o1.getClassification().equals(Classification.AVERAGE) && o2.getClassification().equals(Classification.SERIOUS)) return 1;
	//				else if(o1.getClassification().equals(Classification.AVERAGE) && o2.getClassification().equals(Classification.LIGHT)) return -1;
	//				else if(o1.getClassification().equals(Classification.LIGHT) && o2.getClassification().equals(Classification.AVERAGE)) return 1;
	//				else return 0;
	//			}
	//		});
	//	}
}


