###  **<center>Arthas 等 Java 诊断工具核心原理浅析 </center>**
- - - - - -
[项目地址](#address)

看了会 arthas 源码，结合资料 。用一个小 demo 展示下 arthas 的核心实现

java instrument  agent  +  ASM

**新建一个普通的java项目，包结构如下**

![image-20210630104844318](https://rxf113.xyz/static/image-20210630104844318.png)

##### 三个包 ：

>  target 目标类   模拟平时要维护的服务

>  client 客户端    模拟arthas客户端

> agent 代理程序    客户端挂载target 再执行 target 的jar包 ，提供修改字节码等功能

##### 主要执行流程 ： 

> 1 将 agent  写好打成jar包

> 2 启动 target 一直保持运行

> 3 在 client 中指定 target 的进程id，挂载到 traget 进程，然后加载 agent  jar包,  启动，然后触发执行 agent 里的方法，如修改字节码等

##### 实现目标：

在 target  运行期间，通过client 传入类名-方法名-打印内容 ，达到在原方法前打印传入的内容

##### 开发步骤 ：

#### 1 新建 Target 类 和  WillBeModified类

![image-20210630113354680](https://rxf113.xyz/static/image-20210630113354680.png)

```java
package com.rxf113.instrument.traget;

import java.util.concurrent.TimeUnit;

/**
 * 目标类
 * 用来模拟平时要维护的服务
 *
 * @author rxf113
 */
public class Target {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("targetClass is running !!!");
        System.out.println("waiting to be operated");

        while (true) {
            //循环调用 WillBeModified 中的方法
            WillBeModified.printVal();
            TimeUnit.SECONDS.sleep(5);
        }
    }
}

/**
 * 等会被修改的类
 *
 * @author rxf113
 */
public class WillBeModified {

    public static int val = 9527;

    public static void printVal() {
        System.out.println(val);
    }
}

```

#### 2  创建Agent类![image-20210630133702810](https://rxf113.xyz/static/image-20210630133702810.png)

```java
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
```

ASM 模块忽略 ，我也是现炒热卖，具体查看代码

#### 3  将agent包下所有类打成jar包 

> 1  编译所有类 ，由于有多个类且依赖jar包，采用如下方式编译
>
> ![](https://rxf113.xyz/static/image-20210630140630762.png)
>
> ```cmd
> # javac命令
> javac -cp C:\Users\****\.m2\repository\org\ow2\asm\asm\8.0\asm-8.0.jar;C:\Users\****\.m2\reposi
> tory\org\ow2\asm\asm-analysis\8.0\asm-analysis-8.0.jar;C:\Users\****\.m2\repository\org\ow2\asm\asm-commons\8.0\asm-commons-8.0.jar -encoding utf8 @all-java.txt -Xlint:unchecked
> ```
>
> 2 填写jar包相关class及MANIFEST.MF
>
> ![image-20210630114735768](https://rxf113.xyz/static/image-20210630114735768.png)
>
> 3 修改 MANIFEST.MF 文件
>
> ![image-20210630114842985](https://rxf113.xyz/static/image-20210630114842985.png)
>
> ```java
> Manifest-Version: 1.0
> Agent-Class: com.rxf113.instrument.agent.CusAgent
> Can-Redefine-Classes: true
> Can-Retransform-Classes: true
> ```
>
> 4 Build 打jar包

#### 4 启动Traget

![image-20210630115447305](https://rxf113.xyz/static/image-20210630115447305.png)

记录进程id

![](https://rxf113.xyz/static/image-20210630134003184.png)

#### 5 创建client 类

```java
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
```

#### 6 启动client ，查看 Target输出变化

![image-20210630115652262](https://rxf113.xyz/static/image-20210630115652262.png)

打印发生了变化，中文乱码了，无伤大雅！！！

### 项目地址：

- [instrument](https://github.com/rxf113/instrument.git)

