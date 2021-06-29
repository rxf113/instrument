package com.rxf113.instrument.agent.asm;


import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AnalyzerAdapter;
import org.objectweb.asm.commons.LocalVariablesSorter;

import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.ASM5;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.LSTORE;
import static org.objectweb.asm.Opcodes.LSUB;
import static org.objectweb.asm.Opcodes.LLOAD;


public class CusMethodVisitor extends MethodVisitor {
    public CusMethodVisitor(MethodVisitor mv) {
        super(ASM5, mv);
    }

    private int time;

    public LocalVariablesSorter lvs;

    private int maxStack;

    public AnalyzerAdapter aa;


    //此方法在目标方法调用之前调用，所以前置操作可以在这处理
    @Override
    public void visitCode() {
        mv.visitCode();

        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitLdcInsn("方法已被修改哦!");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);


        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false);
        time = lvs.newLocal(Type.LONG_TYPE);
        mv.visitVarInsn(LSTORE, time);
        maxStack = 4;
    }

    @Override
    public void visitInsn(int opcode) {
        //此方法可以获取方法中每一条指令的操作类型，被访问多次
        //如应在方法结尾处添加新指令，则应判断：
        if (opcode == Opcodes.IRETURN) {

            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false);

            mv.visitVarInsn(LLOAD, time);

            mv.visitInsn(LSUB);

            mv.visitVarInsn(LSTORE, time);

            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");

            mv.visitVarInsn(LLOAD, time);

            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(J)V", false);

            maxStack = Math.max(aa.stack.size() + 4, maxStack);

//            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//            mv.visitLdcInsn("method end");
//            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        }
        super.visitInsn(opcode);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        super.visitMaxs(Math.max(maxStack, this.maxStack), maxLocals);

    }

}