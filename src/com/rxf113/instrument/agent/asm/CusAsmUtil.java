package com.rxf113.instrument.agent.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

/**
 * 自定义工具类
 *
 * @author rxf113
 */
public class CusAsmUtil {
    public static byte[] changeMethodByClassBufferMethodVal(byte[] classfileBuffer, String methodName,String printContent) {
        ClassReader classReader = new ClassReader(classfileBuffer);
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = new CusClassVisitor(classWriter, methodName,printContent);
        classReader.accept(cv, 0);
        return classWriter.toByteArray();
    }
}
