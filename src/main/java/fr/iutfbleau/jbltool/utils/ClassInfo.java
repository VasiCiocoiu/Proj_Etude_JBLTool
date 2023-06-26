package fr.iutfbleau.jbltool.utils;

import org.objectweb.asm.Opcodes;

public class ClassInfo {
    /**
     * Check if the class is a record (https://howtodoinjava.com/java14/java-14-record-type/).
     * @param access
     * Access of the class.
     * @see <a href="https://asm.ow2.io/javadoc/org/objectweb/asm/tree/ClassNode.html#access">https://asm.ow2.io/javadoc/org/objectweb/asm/tree/ClassNode.html#access</a>
     */
    public static boolean isRecord(int access) {
        return (Opcodes.ACC_RECORD & access) != 0;
    }

    /**
     * Check if the class is deprecated.
     * @param access
     * Access of the class.
     * @see <a href="https://asm.ow2.io/javadoc/org/objectweb/asm/tree/ClassNode.html#access">https://asm.ow2.io/javadoc/org/objectweb/asm/tree/ClassNode.html#access</a>
     */
    public static boolean isDeprecated(int access) {
        return (Opcodes.ACC_DEPRECATED & access) != 0;
    }
}
