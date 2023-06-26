package fr.iutfbleau.jbltool.documentation;

import java.util.ArrayList;
import java.util.Objects;

public class InstructionDocumentation {
    private String name;
    private ArrayList<String> parameters;
    private String description;
    private String exemple;
    private int opcode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        Objects.requireNonNull(name);
        this.name = name;
    }

    public ArrayList<String> getParameters() {
        return parameters;
    }

    public void setParameters(ArrayList<String> parameters) {
        Objects.requireNonNull(parameters);
        this.parameters = parameters;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        Objects.requireNonNull(description);
        this.description = description;
    }

    public String getExemple() {
        return exemple;
    }

    public void setExemple(String exemple) {
        Objects.requireNonNull(exemple);
        this.exemple = exemple;
    }

    public int getOpcode() {
        return opcode;
    }

    public void setOpcode(int opcode) {
        Objects.requireNonNull(opcode);
        this.opcode = opcode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstructionDocumentation that = (InstructionDocumentation) o;
        return opcode == that.opcode && Objects.equals(name, that.name) && Objects.equals(parameters, that.parameters) && Objects.equals(description, that.description) && Objects.equals(exemple, that.exemple);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, parameters, description, exemple, opcode);
    }

    @Override
    public String toString() {
        return "InstructionDocumentation{" +
                "name='" + name + '\'' +
                ", parameters=" + parameters +
                ", description='" + description + '\'' +
                ", exemple='" + exemple + '\'' +
                ", opcode=" + opcode +
                '}';
    }
}
