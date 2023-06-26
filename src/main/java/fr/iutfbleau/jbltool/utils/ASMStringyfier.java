package fr.iutfbleau.jbltool.utils;

import java.util.LinkedList;
import java.util.List;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InnerClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;
import org.objectweb.asm.tree.TryCatchBlockNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.util.Printer;

public class ASMStringyfier {
    protected static final String ACC_PUBLIC_STRING = "public";
    protected static final String ACC_PRIVATE_STRING = "private";
    protected static final String ACC_PROTECTED_STRING = "protected";
    protected static final String ACC_FINAL_STRING = "final";
    protected static final String ACC_SUPER_STRING = "super";
    protected static final String ACC_VOLATILE_STRING = "volatile";
    protected static final String ACC_INTERFACE_STRING = "interface";
    protected static final String ACC_ABSTRACT_STRING = "abstract";
    protected static final String ACC_SYNTHETIC_STRING = "synthetic";
    protected static final String ACC_ANNOTATION_STRING = "annotation";
    protected static final String ACC_STATIC = "static";
    protected static final String ACC_ENUM_STRING = "enum";

    protected static final String LABEL_PADDING = "\t ";
    protected static final String INFO_PADDING = "\t\t";
    protected static final String FIELD_PADDING = "\t";
    protected static final String METHOD_PADDING = "\t";
    protected static final String CODE_PADDING = "\t\t";

    protected ASMStringyfier() {}

    /**
     * Create a String out of the access values of a class.
     * @param access
     * Access of the class: <a href="https://asm.ow2.io/javadoc/org/objectweb/asm/tree/ClassNode.html#access">https://asm.ow2.io/javadoc/org/objectweb/asm/tree/ClassNode.html#access</a>.
     * @param sep
     * String that will be used to separate each access string.
     * @return
     * The string representing the access of the class.
     */
    protected static String classAccessModifier(int access, String sep) {
        // https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.1-200-E.1
        List<String> els = new LinkedList<String>();
        if ((access & Opcodes.ACC_PUBLIC) != 0) {
            els.add(ACC_PUBLIC_STRING);
        }
        if ((access & Opcodes.ACC_FINAL) != 0) {
            els.add(ACC_FINAL_STRING);
        }
        if ((access & Opcodes.ACC_SUPER) != 0) {
            els.add(ACC_SUPER_STRING);
        }
        if ((access & Opcodes.ACC_INTERFACE) != 0) {
            els.add(ACC_INTERFACE_STRING);
        }
        if ((access & Opcodes.ACC_ABSTRACT) != 0) {
            els.add(ACC_ABSTRACT_STRING);
        }
        if ((access & Opcodes.ACC_SYNTHETIC) != 0) {
            els.add(ACC_SYNTHETIC_STRING);
        }
        if ((access & Opcodes.ACC_ANNOTATION) != 0) {
            els.add(ACC_ANNOTATION_STRING);
        }
        if ((access & Opcodes.ACC_ENUM) != 0) {
            els.add(ACC_ENUM_STRING);
        }
        if( (access & Opcodes.ACC_STATIC) != 0) {
            els.add(ACC_STATIC);
        }
        return String.join(sep, els);
    }

    /**
     * Creates a String out of the access values of a class. Each access string will be separated with " - ".
     * @param access
     * Access of the class: <a href="https://asm.ow2.io/javadoc/org/objectweb/asm/tree/ClassNode.html#access">https://asm.ow2.io/javadoc/org/objectweb/asm/tree/ClassNode.html#access</a>.
     * @return
     * The string representing the access of the class. Each access will be separated with " - ".
     * @see classAccessModifier(int, String)
     */
    protected static String classAccessModifier(int access) {
        return classAccessModifier(access, " - ");
    }

    /**
     * Returns a string representing a field.
     * @param fn
     * The field itself.
     * @return
     * A string representing the given field.
     */
    protected static String getFieldAsString(FieldNode fn) {
        StringBuilder sb = new StringBuilder(30);

        int a = fn.access;
        if((a & Opcodes.ACC_PUBLIC) != 0) {
            sb.append(ACC_PUBLIC_STRING).append(" ");
        } else if((a & Opcodes.ACC_PRIVATE) != 0) {
            sb.append(ACC_PRIVATE_STRING).append(" ");
        } else if((a & Opcodes.ACC_PROTECTED) != 0) {
            sb.append(ACC_PROTECTED_STRING).append(" ");
        }

        if((a & Opcodes.ACC_STATIC) != 0) {
            sb.append(ACC_STATIC).append(" ");
        }
        if((a & Opcodes.ACC_FINAL) != 0) {
            sb.append(ACC_FINAL_STRING).append(" ");
        } else if((a & Opcodes.ACC_VOLATILE) != 0) {
            sb.append(ACC_VOLATILE_STRING).append(" ");
        }

        sb.append(fn.desc);
        sb.append(" ");
        sb.append(fn.name);
        sb.append(";");

        if((a & Opcodes.ACC_ENUM) != 0) { // is an enum field
            sb.append(" // enum field");
        }

        sb.append("\n");
        return sb.toString();
    }

    /**
     * Returns a string containing infos from a given class.
     * @param cn
     * The ClassNode to get infos from.
     * @return
     * A string containing the infos.
     */
    public static String getClassInfo(ClassNode cn) {
        StringBuilder sb = new StringBuilder(50);

        int version = cn.version;
        sb.append("Major version: ");
        sb.append(String.valueOf(version & 0xFF));
        sb.append("\nMinor version: ");
        sb.append(String.valueOf(version & 0xFF00));

        if(cn.sourceFile != null && !cn.sourceFile.equals("")) {
            sb.append("\nCompiled from \"");
            sb.append(cn.sourceFile);
            sb.append("\"");
        }

        // classes defined in the class' scope
        List<InnerClassNode> innerClasses = cn.innerClasses;
        if(innerClasses != null) {
            sb.append("\n\nInner classes:\n");
            for(InnerClassNode icn : innerClasses) {
                sb.append("\t");
                sb.append(icn.name);
                sb.append("\n\t\tInner name: ");
                sb.append(icn.innerName);
                sb.append("\n\t\tOuter name: ");
                sb.append(icn.outerName);
                sb.append("\n\t\tAccess modifiers: ");
                sb.append(ASMStringyfier.classAccessModifier(icn.access));
                sb.append("\n\n");
            }
        }

        return sb.toString();
    }

    /**
     * Returns if the native access flag is set in an access bitflag.
     * @param access
     * The bitflag to check into.
     * @return
     * If the native access flag is set in the access parameter.
     */
    protected static boolean isNative(int access) {
        return (access & Opcodes.ACC_NATIVE) != 0;
    }

    /**
     * Returns if the var args (method can have an unknown number of arguments) flag is set in an access bitflag.
     * @param access
     * The bitflag to check into.
     * @return
     * If the var args flag is set in the access parameter.
     */
    protected static boolean hasVarargs(int access) {
        return (access & Opcodes.ACC_VARARGS) != 0;
    }

    /**
     * Returns the method access as a string.
     * @param access
     * The access bitflag to check into.
     * @return
     * A string representing the access of the method.
     */
    protected static String getMethodAccess(int access) {
        StringBuilder sb = new StringBuilder(50);

        if ((access & Opcodes.ACC_PUBLIC) != 0) {
            sb.append("public ");
        } else if((access & Opcodes.ACC_PRIVATE) != 0) {
            sb.append("private ");
        } else if((access & Opcodes.ACC_PROTECTED) != 0) {
            sb.append("protected ");
        }

        if ((access & Opcodes.ACC_STATIC) != 0) {
            sb.append("static ");
        }
        if ((access & Opcodes.ACC_FINAL) != 0) {
            sb.append("final ");
        }
        if ((access & Opcodes.ACC_SYNCHRONIZED) != 0) {
            sb.append("synchronized ");
        }
        if ((access & Opcodes.ACC_NATIVE) != 0) {
            sb.append("native ");
        }
        if ((access & Opcodes.ACC_ABSTRACT) != 0) {
            sb.append("abstract ");
        }

        return sb.toString();
    }

    /**
     * Returns a iconst instruction as a string with it's correct instruction name.
     * @param ins
     * The iconst instruction to stringify.
     * @return
     * The instruction as a string.
     */
    protected static String getIntInsnValue(IntInsnNode ins) {
        int opcode = ins.getOpcode();
		switch(opcode) {
            case Opcodes.ICONST_0:
                return "iconst_0";
            case Opcodes.ICONST_1:
                return "iconst_1";
            case Opcodes.ICONST_2:
                return "iconst_2";
            case Opcodes.ICONST_3:
                return "iconst_3";
            case Opcodes.ICONST_4:
                return "iconst_4";
            case Opcodes.ICONST_5:
                return "iconst_5";

            case Opcodes.LCONST_0:
                return "lconst_0";
            case Opcodes.LCONST_1:
                return "lconst_1";

            case Opcodes.DCONST_0:
                return "dconst_0";
            case Opcodes.DCONST_1:
                return "dconst_1";

            case Opcodes.FCONST_0:
                return "fconst_0";
            case Opcodes.FCONST_1:
                return "fconst_1";
            case Opcodes.FCONST_2:
                return "fconst_2";

            default:
                return "iconst " + ins.operand;
		}
    }

    /**
     * Returns a string containing a disassembled version of the class.
     * @param cn
     * The ClassNode to represent.
     * @return
     * A string representing the class.
     */
    public static String getClassCode(ClassNode cn) {
        StringBuilder sb = new StringBuilder(500);

        sb.append("class ");
        sb.append(cn.name);

        sb.append(" extends ");
        sb.append(cn.superName);

        List<String> interfaces = cn.interfaces;
        if(interfaces != null && !interfaces.isEmpty()) {
            sb.append(" implements ");
            sb.append(String.join(", ", interfaces));
        }

        sb.append(" {\n// Access: ");
        sb.append(ASMStringyfier.classAccessModifier(cn.access));
        sb.append("\n\n");

        if(cn.attrs != null && !cn.attrs.isEmpty()) {
            for(Attribute attr : cn.attrs) {
                sb.append(FIELD_PADDING).append(attr.toString());
            }
        }

        List<FieldNode> fields = cn.fields;
        if(fields != null && !fields.isEmpty()) {
            for(FieldNode field : fields) {
                sb.append(FIELD_PADDING).append(ASMStringyfier.getFieldAsString(field));
            }
            sb.append("\n");
        }

        for (MethodNode method : cn.methods) {
            String name = method.name;
            Type returnType = Type.getReturnType(method.desc);
            Type[] argumentsType = Type.getArgumentTypes(method.desc);
            LabelNameGenerator lng = new LabelNameGenerator();

            sb.append(METHOD_PADDING).append(ASMStringyfier.getMethodAccess(method.access)); // access value
            sb.append(returnType.getClassName()).append(" "); // return value
            sb.append(name);
            // parameters
            sb.append("(");

            for(int i = 0; i < argumentsType.length; i++) {
                sb.append(argumentsType[i].getClassName());
                if(i != argumentsType.length - 1) { // last elements
                    sb.append(", ");
                }
            }
            if(hasVarargs(method.access)) {
                sb.append(" ...");
            }
            sb.append(")");

            List<String> exceptions = method.exceptions;
            if(exceptions != null && !exceptions.isEmpty()) {
                sb.append(" throws ");
                sb.append(String.join(", ", exceptions));
            }

            if(ASMStringyfier.isNative(cn.access)) {
                sb.append(";\n");
                continue; // they don't have any code as they are native
            }
            sb.append(" {\n");

            if(method.desc != null && !method.desc.equals("")) {
                sb.append(INFO_PADDING).append("// Description: ");
                sb.append(method.desc);
            }

            sb.append("\n").append(INFO_PADDING).append("// Access flags: ").append(ASMStringyfier.classAccessModifier(method.access));

            String signature = method.signature;
            if(signature != null && !signature.equals("")) {
                sb.append("\n").append(INFO_PADDING).append("// Signature: ");
                sb.append(signature);
            }

            sb.append("\n").append(INFO_PADDING).append("// Max stack size: ");
            sb.append(method.maxStack);
            sb.append("\n").append(INFO_PADDING).append("// Max locals: ");
            sb.append(method.maxLocals);

            InsnList instructions = method.instructions;

            sb.append("\n").append(INFO_PADDING).append("// Locals: ");
            int countLocals = 0;
            for(AbstractInsnNode instruction : instructions) {
                if(instruction instanceof FrameNode) {
                    FrameNode ins = (FrameNode) instruction;
                    sb.append(getFrameNodeLocalsAsString(ins));
                    countLocals++;
                }
            }
            if (countLocals == 0) {
                sb.append("<none>");
            }
            sb.append("\n");

            if(!method.tryCatchBlocks.isEmpty()) {
                sb.append(INFO_PADDING).append("// Try catch: (start, end, handler):").append("\n");
                for(TryCatchBlockNode tcbn : method.tryCatchBlocks) {
                    sb.append(INFO_PADDING)
                        .append("// \t")
                        .append(lng.generateLabelName(tcbn.start))
                        .append(", ")
                        .append(lng.generateLabelName(tcbn.end))
                        .append(", ")
                        .append(lng.generateLabelName(tcbn.handler))
                        .append("\n");
                }
            }

            // instructions
            for(AbstractInsnNode instruction : instructions) {
                if(instruction instanceof LineNumberNode) {
                } else if(instruction instanceof FieldInsnNode) {
                    FieldInsnNode ins = (FieldInsnNode) instruction;
                    sb.append(CODE_PADDING).
                        append(Printer.OPCODES[ins.getOpcode()]).
                        append(" ").
                        append(ins.owner).
                        append("\n");
                } else if(instruction instanceof IincInsnNode) {
                    IincInsnNode ins = (IincInsnNode) instruction;
                    sb.append(CODE_PADDING).
                        append(Printer.OPCODES[ins.getOpcode()]).
                        append(" ").
                        append(ins.var).
                        append(" +").
                        append(ins.incr).
                        append("\n");
                } else if(instruction instanceof InsnNode) {
                    InsnNode ins = (InsnNode) instruction;
                    sb.append(CODE_PADDING).
                        append(Printer.OPCODES[ins.getOpcode()]).
                        append("\n");
                } else if(instruction instanceof IntInsnNode) {
                    IntInsnNode ins = (IntInsnNode) instruction;
                    sb.append(CODE_PADDING).
                        append(getIntInsnValue(ins)).
                        append("\n");
                } else if(instruction instanceof LdcInsnNode) {
                    LdcInsnNode ins = (LdcInsnNode) instruction;
                    sb.append(CODE_PADDING).
                        append(Printer.OPCODES[ins.getOpcode()]).
                        append(" ").
                        append(getConstantAsString(ins.cst)).
                        append("\n");
                } else if(instruction instanceof InvokeDynamicInsnNode) {
                    InvokeDynamicInsnNode ins = (InvokeDynamicInsnNode) instruction;
                    sb.append(CODE_PADDING).
                        append(Printer.OPCODES[ins.getOpcode()]).
                        append(" ").
                        append(ins.name).
                        append(" // desc: ").
                        append(ins.desc).
                        append("\n");
                } else if(instruction instanceof JumpInsnNode) {
                    JumpInsnNode ins = (JumpInsnNode) instruction;
                    sb.append(CODE_PADDING).
                        append(Printer.OPCODES[ins.getOpcode()]).
                        append(" ").
                        append(lng.generateLabelName(ins.label.getLabel())).
                        append("\n");
                } else if(instruction instanceof LabelNode) {
                    LabelNode ins = (LabelNode) instruction;
                    sb.append(LABEL_PADDING).
                        append(lng.generateLabelName(ins.getLabel())).
                        append(":\n");
                } else if(instruction instanceof LookupSwitchInsnNode) {
                    LookupSwitchInsnNode ins = (LookupSwitchInsnNode) instruction;
                    sb.append(CODE_PADDING).
                        append(Printer.OPCODES[ins.getOpcode()]).
                        append("\n");
                } else if(instruction instanceof MethodInsnNode) {
                    MethodInsnNode ins = (MethodInsnNode) instruction;
                    sb.append(CODE_PADDING).
                        append(Printer.OPCODES[ins.getOpcode()]).
                        append(" ").append(ins.owner).append("#").append(ins.name).
                        append(" // desc: ").append(ins.desc).append("\n");
                } else if(instruction instanceof MultiANewArrayInsnNode) {
                    MultiANewArrayInsnNode ins = (MultiANewArrayInsnNode) instruction;
                    sb.append(CODE_PADDING).
                        append(Printer.OPCODES[ins.getOpcode()]).
                        append(" ").
                        append(ins.dims).
                        append(" ").
                        append(ins.desc).
                        append("\n");
                } else if(instruction instanceof TableSwitchInsnNode) {
                    TableSwitchInsnNode ins = (TableSwitchInsnNode) instruction;
                    sb.append(CODE_PADDING).
                        append(Printer.OPCODES[ins.getOpcode()]).
                        append("\n");
                } else if(instruction instanceof TypeInsnNode) {
                    TypeInsnNode ins = (TypeInsnNode) instruction;
                    sb.append(CODE_PADDING).
                        append(Printer.OPCODES[ins.getOpcode()]).
                        append(" // desc: ").
                        append(ins.desc).
                        append("\n");
                } else if(instruction instanceof VarInsnNode) {
                    VarInsnNode ins = (VarInsnNode) instruction;
                    sb.append(CODE_PADDING).
                        append(ASMStringyfier.varInstructionAsString(ins)).
                        append("\n");
                }
            }

            sb.append("\n\n");
        }

        sb.append("}");

        return sb.toString();
    }

    /**
     * Returns a var instruction formated (exemple: "aload 1" -> "aload_1", "aload 6" -> "alod 6")
     * @param ins
     * {@link VarInsnNode} representing the instruction to stringify.
     * @return
     * The instruction given as parameter formated as a string.
     */
    protected static String varInstructionAsString(VarInsnNode ins) {
        if(ins.var <= 3) {
            return Printer.OPCODES[ins.getOpcode()] + "_" + ins.var;
        }
        return Printer.OPCODES[ins.getOpcode()] + " " + ins.var;
    }

    /**
     * Returns the locals from a frame as a string.
     * @param ins
     * The frame to get the locals from.
     * @return
     * A string representing the locals from the FrameNode.
     */
    protected static String getFrameNodeLocalsAsString(FrameNode ins) {
        StringBuilder sb = new StringBuilder();
        if(ins.local != null && !ins.local.isEmpty()) {
            for(Object o : ins.local) {
                if(o instanceof Integer) {
                    sb.append((Integer) o);
                    sb.append(", ");
                } else if(o instanceof String) {
                    sb.append('"' + (String) o + '"');
                    sb.append(", ");
                } else if(o instanceof LabelNode) {
                    LabelNode ln = (LabelNode) o;
                    sb.append(ln.getLabel().info.toString());
                    sb.append(", ");
                }
            }
        }
        return sb.toString();
    }

    /**
     * Returns a string representing a constant to it's original type.
     * @param cst
     * The constant to cast.
     * @return
     * The constant as string.
     */
    protected static String getConstantAsString(Object cst) {
        // The constant to be loaded on the stack. This parameter must be a non null
        // Integer, a Float, a Long, a Double, a String or a org.objectweb.asm.Type.
        if(cst instanceof Integer) {
            return String.valueOf((int) cst);
        } else if(cst instanceof Float) {
            return String.valueOf((float) cst);
        } else if(cst instanceof Long) {
            return String.valueOf((long) cst);
        } else if(cst instanceof Double) {
            return String.valueOf((double) cst);
        } else if(cst instanceof String) {
            return '"' + ((String) cst) + '"';
        } else {
            return ((Type)cst).getClassName();
        }
    }
}
