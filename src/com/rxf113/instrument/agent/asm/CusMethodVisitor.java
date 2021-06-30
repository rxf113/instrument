package com.rxf113.instrument.agent.asm;

import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;


public class CusMethodVisitor extends MethodVisitor {

    private String printContent;

    public CusMethodVisitor(MethodVisitor mv, String printContent) {
        super(ASM5, mv);
        this.printContent = printContent;
    }


    //此方法在目标方法调用之前调用，所以前置操作可以在这处理
    @Override
    public void visitCode() {
        mv.visitCode();
        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        //打印内容
        mv.visitLdcInsn(printContent);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
    }


//    @Override
//    public void visitInsn(int opcode) {
//        //此方法可以获取方法中每一条指令的操作类型，被访问多次
//        //如应在方法结尾处添加新指令，则应判断：
//        if (opcode == Opcodes.IRETURN) {
//            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//            mv.visitLdcInsn("执行方法后。。。");
//            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
//        }
//        super.visitInsn(opcode);
//    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        super.visitMaxs(maxStack + 1, maxLocals);

    }

}