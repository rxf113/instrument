package com.rxf113.instrument.agent.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.ASM5;


public class CusClassVisitor extends ClassVisitor {

    private String methodName;

    private String printContent;

    public CusClassVisitor(ClassVisitor cv, String methodName, String printContent) {
        super(ASM5, cv);
        this.methodName = methodName;
        this.printContent = printContent;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
        if (name.equals(methodName)) {
            return new CusMethodVisitor(methodVisitor,printContent);
        } else {
            return methodVisitor;
        }
    }

//    @Override
//    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
//        if (name.equals(fieldName)) {
//            return super.visitField(access, name, descriptor, signature, val);
//        }
//        return super.visitField(access, name, descriptor, signature, value);
//    }
}
