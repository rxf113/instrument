package com.rxf113.instrument.traget;

import java.util.Scanner;
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
