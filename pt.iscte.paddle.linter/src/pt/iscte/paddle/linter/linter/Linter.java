package pt.iscte.paddle.linter.linter;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import pt.iscte.paddle.model.IBlock.IVisitor;
import pt.iscte.paddle.linter.cases.base.CodeAnalyser;
import pt.iscte.paddle.linter.cases.base.QualityIssue;
import pt.iscte.paddle.linter.misc.BadCodeAnalyser;
import pt.iscte.paddle.linter.misc.Classification;
import pt.iscte.paddle.linter.visitors.DuplicateBranchGuard;
import pt.iscte.paddle.linter.visitors.DuplicateStatement;
import pt.iscte.paddle.linter.visitors.Loop;
import pt.iscte.paddle.linter.visitors.MagicNumbers;
import pt.iscte.paddle.linter.visitors.ProcedureCall;
import pt.iscte.paddle.linter.visitors.Return;
import pt.iscte.paddle.linter.visitors.Selection;
import pt.iscte.paddle.linter.visitors.Unreachable;
import pt.iscte.paddle.linter.visitors.UselessAssignment;
import pt.iscte.paddle.model.IModule;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.cfg.IControlFlowGraph;

public class Linter {

	private ArrayList<Class<? extends CodeAnalyser>> visitors = new ArrayList<>();
	
	public Linter() {
		loadAllVisitors();
	}
	 
	public Linter(Class<? extends CodeAnalyser>... issueAnalyser) {
		for (Class<? extends CodeAnalyser> class1 : issueAnalyser) {
			this.visitors.add(class1);
		}
	}

	private void loadAllVisitors() {
		this.visitors.add(Selection.class);
		this.visitors.add(Loop.class);
		this.visitors.add(MagicNumbers.class);
		this.visitors.add(UselessAssignment.class);
		this.visitors.add(Unreachable.class);
		this.visitors.add(DuplicateBranchGuard.class);
		this.visitors.add(DuplicateStatement.class);
		this.visitors.add(ProcedureCall.class);
		this.visitors.add(Return.class);
	}

	public List<QualityIssue> analyse(IModule module) {
		ArrayList<QualityIssue> caughtIssues = new ArrayList<>();
		module.getProcedures().forEach(proc -> {
			IControlFlowGraph cfg = proc.generateCFG();

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


