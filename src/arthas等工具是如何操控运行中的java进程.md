arthas等工具是如何操控运行中的java进程

- 1  新建一个普通的java项目 ，框架、maven 啥的都不用了

![image-20210629201840110](C:\Users\11313\AppData\Roaming\Typora\typora-user-images\image-20210629201840110.png)

![](C:\Users\11313\AppData\Roaming\Typora\typora-user-images\image-20210629202320804.png)

- 2 编写一个目标类，当做平时的服务端程序。运行起来后， 循环从GetVal类获取属性并打印

  ![](C:\Users\11313\AppData\Roaming\Typora\typora-user-images\image-20210629205252338.png)

![image-20210629205429999](C:\Users\11313\AppData\Roaming\Typora\typora-user-images\image-20210629205429999.png)


- 3 编写 agent类 

- 4 编译agent 包下所有类，并打成jar包

  ![](C:\Users\11313\AppData\Roaming\Typora\typora-user-images\image-20210629220706782.png)

​      打jar包

​	![image-20210629221049635](C:\Users\11313\AppData\Roaming\Typora\typora-user-images\image-20210629221049635.png)

修改 MANIFEST.MF 文件

![image-20210629222618328](C:\Users\11313\AppData\Roaming\Typora\typora-user-images\image-20210629222618328.png)

build

![image-20210629222933877](C:\Users\11313\AppData\Roaming\Typora\typora-user-images\image-20210629222933877.png)

- 5 编写一个agent织入的 类，相当于一个arthas客户端

  ![image-20210629231007598](C:\Users\11313\AppData\Roaming\Typora\typora-user-images\image-20210629231007598.png)

6 启动 TargetClass  

![image-20210629231025244](C:\Users\11313\AppData\Roaming\Typora\typora-user-images\image-20210629231025244.png)

7 查看TargetClass 进程号

![](C:\Users\11313\AppData\Roaming\Typora\typora-user-images\image-20210629231723215.png)



8 输入进程号 ， 启动Client ， 

![](C:\Users\11313\AppData\Roaming\Typora\typora-user-images\image-20210629231738246.png)