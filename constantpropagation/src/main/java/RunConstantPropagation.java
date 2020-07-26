import soot.*;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;

import java.util.Iterator;
import java.util.stream.StreamSupport;

public class RunConstantPropagation {

    public static final String CLASS_NAME = "Test";
    public static final String SOOT_CLASS_PATH = "G:\\repository\\assignment\\constantpropagation\\target\\classes";

    public static void main(String[] args) {

        args = new String[]{
                "-cp", SOOT_CLASS_PATH,
                "-pp", CLASS_NAME
        };

        soot.options.Options.v().parse(args);

        // Load all the necessary classes based on the options
        Scene.v().loadNecessaryClasses();

        SootClass c = Scene.v().loadClassAndSupport(CLASS_NAME);

        c.setApplicationClass();

        Iterator methodIt = c.getMethods().iterator();
        while (methodIt.hasNext()) {
            SootMethod m = (SootMethod) methodIt.next();
            Body b = m.retrieveActiveBody();

            if (m.getName().matches("main")) {
                System.out.println("============== method =========================");
                System.out.println(m.toString());

                for (Unit unit : b.getUnits()) {
                    System.out.println(unit);
                }

                UnitGraph graph = new ExceptionalUnitGraph(b);
                ConstantPropagation cp = new ConstantPropagation(graph);

                for (Unit unit : b.getUnits()) {
                    System.out.println(unit);
                    System.out.println("");
                    System.out.println(StreamSupport.stream(cp.getFlowAfter(unit).spliterator(), false)
                            .map(v -> String.format(" (%s, %s), ", v.getVariableName(), v.getVariableValue()))
                            .reduce("\t { ", String::concat)
                            .concat(" } "));
                    System.out.println("");
                }

            }
        }

    }
}
