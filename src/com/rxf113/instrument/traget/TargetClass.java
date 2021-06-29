package com.rxf113.instrument.traget;

import com.rxf113.instrument.traget.GetVal;

import java.util.concurrent.TimeUnit;

/**
 * 目标类
 *
 * @author rxf113
 */
public class TargetClass {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("targetClass is running !!!");
        System.out.println("waiting to be operated");

        while (true) {
            System.out.printf("getValClass val is : %d\n", GetVal.getVal());
            TimeUnit.SECONDS.sleep(5);
            if (GetVal.getVal() == 0) {
                break;
            }
        }
    }
}
