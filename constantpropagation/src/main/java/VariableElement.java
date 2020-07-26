import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.stream.Stream;

public class VariableElement {

    private ImmutablePair<String, Object> immutablePair;

    public VariableElement(String variable, Object value) {
        immutablePair = new ImmutablePair<>(variable, value);
    }

    public String getVariableName() {
        return immutablePair.left;
    }

    public Object getVariableValue() {
        return immutablePair.right;
    }

    @Override
    public int hashCode() {
        return immutablePair.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof VariableElement)) {
            return false;
        }
        return immutablePair.equals(((VariableElement) obj).immutablePair);
    }

    public boolean variableNameEquals(Object obj) {
        if (!(obj instanceof VariableElement)) {
            return false;
        }
        return immutablePair.left.equals(((VariableElement) obj).immutablePair.left);
    }

    public VariableElement meet(VariableElement other) {

        if (equals(other)) {
            return new VariableElement(this.immutablePair.left, this.immutablePair.right);
        } else if (anyValueMatch(Value.NON_CONSTANT, this, other)) {
            return new VariableElement(this.immutablePair.left, Value.NON_CONSTANT);
        } else {
            VariableElement v = valueMatch(Value.UNDEFINED, this, other);
            if (v != null) {
                v = v.equals(this) ? other : this;
            } else {
                v = new VariableElement(this.immutablePair.left, Value.NON_CONSTANT);
            }
            return v;
        }
    }

    private boolean anyValueMatch(Object value, VariableElement v1, VariableElement v2) {
        return valueMatch(value, v1, v2) != null;
    }

    private VariableElement valueMatch(Object value, VariableElement v1, VariableElement v2) {
        return Stream.of(v1, v2).filter(e -> e.immutablePair.getValue().equals(value))
                .findFirst()
                .orElse(null);
    }

    enum Value {
        NON_CONSTANT, UNDEFINED
    }

}
