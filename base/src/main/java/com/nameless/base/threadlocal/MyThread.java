package com.nameless.base.threadlocal;

/**
 * Created by boysz on 2018/12/4.
 */
public class MyThread extends Thread {

    public MyThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        System.out.println( Thread.currentThread() + ":" +SerialIntegerContext.get());
        //System.out.println(ThreadContext.myThreadLocal.get());
        //System.out.println(Thread.currentThread().getName() + " is running .");
    }
}
