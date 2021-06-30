package com.rxf113.instrument.agent;


import com.rxf113.instrument.agent.asm.CusAsmUtil;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.security.ProtectionDomain;

/**
 * agent类
 *
 * @author rxf113
 */
public class CusAgent {


    public static void agentmain(String agentArgs, Instrumentation inst) throws UnmodifiableClassException, ClassNotFoundException {
        System.out.println("method agentmain invoked");
        //默认 [className-methodName-printContent] 格式
        String[] args = agentArgs.split("-");
        inst.addTransformer(new CusClassFileTransformer(args[0], args[1],args[2]), true);
        //触发transform执行
        inst.retransformClasses(Class.forName(args[0]));
    }

    static class CusClassFileTransformer implements ClassFileTransformer {

        public CusClassFileTransformer(String className, String methodName,String printContent) {
            this.className = className;
            this.methodName = methodName;
            this.printContent = printContent;
        }

        private String className;

        private String methodName;

        private String printContent;

        @Override
        public byte[] transform(ClassLoader loader, String clsName, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
            //判断是不是要处理的类
            if (clsName.matches(".*" + className)) {
                //修改此方法
                //使用ASM框架 修改方法使其添加一条打印
                return CusAsmUtil.changeMethodByClassBufferMethodVal(classfileBuffer, methodName,printContent);
            }
            return new byte[0];
        }
    }
}
