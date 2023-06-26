package fr.iutfbleau.jbltool.utils;

import java.util.HashMap;
import org.objectweb.asm.Label;
import org.objectweb.asm.tree.LabelNode;

public class LabelNameGenerator {
    private int currentNumber; // used to count the number of labels and assigned them a uniq number
    private HashMap<Label, String> labelsName;
    private final String LABEL_PREFIX = "labl_";

    /**
     * This class generates names for {@link org.objectweb.asm.Label} and {@link org.objectweb.asm.LabelNode}.
     */
    public LabelNameGenerator() {
        currentNumber = 0;
        labelsName = new HashMap<>();
    }

    /**
     * Generate a name for a {@link org.objectweb.asm.LabelNode} (from ASM). Note that the same label will have the same name though the life of the program.
     * @param ln
     * The {@link org.objectweb.asm.LabelNode} to get a name from.
     * @return
     * A name for the label.
     */
    public String generateLabelName(LabelNode ln) {
        if(labelsName.containsKey(ln.getLabel())) {
            return labelsName.get(ln.getLabel());
        }
        String resultString = LABEL_PREFIX + currentNumber;
        currentNumber++;

        labelsName.put(ln.getLabel(), resultString);
        return resultString;
    }

    /**
     * Generate a name for a {@link org.objectweb.asm.Label}. Note that the same label will have the same name though the life of the program.
     * @param l
     * The {@link org.objectweb.asm.Label} to get a name from.
     * @return
     * A name for the label.
     */
    public String generateLabelName(Label l) {
        if(labelsName.containsKey(l)) {
            return labelsName.get(l);
        }
        String resultString = LABEL_PREFIX + currentNumber;
        currentNumber++;

        labelsName.put(l, resultString);
        return resultString;
    }
}
