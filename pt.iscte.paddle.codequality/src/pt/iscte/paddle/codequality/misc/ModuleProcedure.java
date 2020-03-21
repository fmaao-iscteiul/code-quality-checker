package pt.iscte.paddle.codequality.misc;

import pt.iscte.paddle.codequality.Icfg.IControlFlowGraphBuilder;
import pt.iscte.paddle.model.IProcedure;

public class ModuleProcedure {
	private IProcedure procedure;
	private IControlFlowGraphBuilder cfg;
	
	public ModuleProcedure(IProcedure procedure) {
		this.procedure = procedure;
		this.cfg = IControlFlowGraphBuilder.create(procedure);	
	}
	
	public IControlFlowGraphBuilder getCfgBuilder() {
		return cfg;
	}
	public IProcedure getProcedure() {
		return procedure;
	}
}