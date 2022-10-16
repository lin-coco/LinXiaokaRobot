package com.lincoco.xiaokarobot.common;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * @author ：xys
 * @description：TODO
 * @date ：2022/10/16
 */
public class TaskCounter {

    private static CountDownLatch counter;

    public static void countDown() {
        counter.countDown();
    }

    public static void await(){
        try {
            counter.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void resetCounter(Integer taskCount){
        counter = new CountDownLatch(taskCount);
    }
}
