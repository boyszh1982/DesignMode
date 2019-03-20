package com.nameless.base.concurrent;

import org.jboss.netty.util.internal.NonReentrantLock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by ThinkPad on 2019/3/20.
 */
public class ReentrantLockTest2 {

    public static void main(String[] args) {
        final ReentrantLock lock = new ReentrantLock(true);
        for(int i=0;i<10;i++){
            new Thread(() -> {
                try {
                    lock.lockInterruptibly();
                    System.out.println(Thread.currentThread().getName());
                    Thread.sleep(1*1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }).start();
        }
    }
}
