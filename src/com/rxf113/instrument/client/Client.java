package com.rxf113.instrument.client;

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
public class Client {

    public static void main(String[] args) throws IOException, AttachNotSupportedException, AgentLoadException, AgentInitializationException {
        //targetClass 的进程号
        VirtualMachine attach = VirtualMachine.attach("5808");
        //agent jar包的位置 参数
        attach.loadAgent("D:\\gitee_mine\\instrument\\out\\production\\instrumentDemo\\com\\rxf113\\instrument\\agent\\cusAgent.jar",
                //[className-methodName-printContent] 格式的参数
                "com.rxf113.instrument.traget.WillBeModified-printVal-i am printContent,我已经被打了");
        attach.detach();
    }

}
