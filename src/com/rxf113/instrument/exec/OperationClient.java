package com.rxf113.instrument.exec;

import java.io.IOException;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

/**
 * 启动类 相当于arthas客户端的意思
 *
 * @author rxf113
 */
public class OperationClient {

    public static void main(String[] args) throws IOException, InterruptedException, AttachNotSupportedException, AgentLoadException, AgentInitializationException {
        //targetClass 是进程号
        VirtualMachine attach = VirtualMachine.attach("15924");
        //agent jar包的位置 参数
        attach.loadAgent("F:\\githubs\\instrumentDemo\\out\\production\\instrumentDemo\\com\\rxf113\\instrument\\agent\\cusAgent.jar",
                //[className-methodName-fieldName-value]
                "com.rxf113.instrument.traget.GetVal-getVal-val-250");
        attach.detach();
    }

}
