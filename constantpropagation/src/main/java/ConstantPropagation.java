import soot.Local;
import soot.Unit;
import soot.jimple.AssignStmt;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ForwardFlowAnalysis;

public class ConstantPropagation extends ForwardFlowAnalysis<Unit, VariableElementFlowSet> {

    private VariableElementFlowSet initialSet;

    public ConstantPropagation(UnitGraph graph) {
        super(graph);

        // 定义初始状态
        initialSet = new VariableElementFlowSet();

        for (Local local : graph.getBody().getLocals()) {
            initialSet.add(new VariableElement(local.getName(), VariableElement.Value.UNDEFINED));
        }

        doAnalysis();
    }


    @Override
    protected void flowThrough(VariableElementFlowSet in, Unit d, VariableElementFlowSet out) {
        VariableElementFlowSet inClone = in.clone();

        if (d instanceof AssignStmt) {

            String varName = d.getDefBoxes().get(0).getValue().toString();
            String varValue = d.getUseBoxes().get(0).getValue().toString();

            VariableElementFlowSet gen = new VariableElementFlowSet();
            gen.add(new VariableElement(varName, varValue));

            inClone.replaceUseOtherIfExistVariableName(gen);
        }

        out.replaceUseOtherIfExistVariableName(inClone);
    }

    @Override
    protected VariableElementFlowSet newInitialFlow() {
        return initialSet.clone();
    }

    @Override
    protected void merge(VariableElementFlowSet in1, VariableElementFlowSet in2, VariableElementFlowSet out) {
        in1.meet(in2, out);
        in1.union(in1);
    }

    @Override
    protected void copy(VariableElementFlowSet source, VariableElementFlowSet dest) {
        source.copy(dest);
    }
}
