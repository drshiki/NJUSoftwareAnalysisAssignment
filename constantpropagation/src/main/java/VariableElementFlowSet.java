import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;

public class VariableElementFlowSet extends ArraySparseSet<VariableElement> {

    public VariableElementFlowSet() {
    }

    public VariableElementFlowSet(VariableElementFlowSet other) {
        numElements = other.numElements;
        maxElements = other.maxElements;
        VariableElement[] tmp = new VariableElement[numElements];
        int i = 0;
        for (VariableElement v : other) {
            tmp[i] = v;
            i++;
        }
        elements = tmp.clone();
    }

    public void replaceUseOtherIfExistVariableName(FlowSet<VariableElement> other) {

        //VariableElementFlowSet workingSet = this.clone();

        for (VariableElement t : other) {

            int i = 0, idx = -1;
            for (VariableElement t2 : this) {
                if (t2.variableNameEquals(t)) {
                    idx = i;
                    break;
                }
                i++;
            }

            if (idx != -1) {
                remove(idx);
            }
            add(t);
        }
    }

    @Override
    public VariableElementFlowSet clone() {
        return new VariableElementFlowSet(this);
    }

    public void meet(VariableElementFlowSet in, VariableElementFlowSet dest) {

        VariableElementFlowSet workingSet = new VariableElementFlowSet();

        for (VariableElement v1 : this) {
            for (VariableElement v2 : in) {
                if (v1.variableNameEquals(v2)) {
                    workingSet.add(v1.meet(v2));
                }
            }
        }

        dest.replaceUseOtherIfExistVariableName(workingSet);
    }
}
