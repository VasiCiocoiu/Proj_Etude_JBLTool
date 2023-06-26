package fr.iutfbleau.jbltool;

import fr.iutfbleau.jbltool.utils.ASMStringyfier;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.objectweb.asm.Opcodes;

public class ASMStringyfierTest extends ASMStringyfier {
    @Test
    public void classAccessModifierSimple() {
        assertEquals(ASMStringyfier.ACC_PUBLIC_STRING, ASMStringyfier.classAccessModifier(Opcodes.ACC_PUBLIC));
        assertEquals(ASMStringyfier.ACC_FINAL_STRING, ASMStringyfier.classAccessModifier(Opcodes.ACC_FINAL));
        assertEquals(ASMStringyfier.ACC_SUPER_STRING, ASMStringyfier.classAccessModifier(Opcodes.ACC_SUPER));
        assertEquals(ASMStringyfier.ACC_INTERFACE_STRING, ASMStringyfier.classAccessModifier(Opcodes.ACC_INTERFACE));
        assertEquals(ASMStringyfier.ACC_ABSTRACT_STRING, ASMStringyfier.classAccessModifier(Opcodes.ACC_ABSTRACT));
        assertEquals(ASMStringyfier.ACC_SYNTHETIC_STRING, ASMStringyfier.classAccessModifier(Opcodes.ACC_SYNTHETIC));
        assertEquals(ASMStringyfier.ACC_ANNOTATION_STRING, ASMStringyfier.classAccessModifier(Opcodes.ACC_ANNOTATION));
        assertEquals(ASMStringyfier.ACC_ENUM_STRING, ASMStringyfier.classAccessModifier(Opcodes.ACC_ENUM));
    }

    @Test
    public void classAccessModifierSimpleNoSepLeft() {
        assertEquals(ASMStringyfier.ACC_PUBLIC_STRING,
                ASMStringyfier.classAccessModifier(Opcodes.ACC_PUBLIC, "this is a sep"));
    }
    
    @Test
    public void classAccessModifierMultiple() {
        assertEquals(ASMStringyfier.ACC_PUBLIC_STRING + " - " + ASMStringyfier.ACC_FINAL_STRING, ASMStringyfier.classAccessModifier(Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL, " - "));
    }
}
