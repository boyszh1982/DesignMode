package com.nameless.base.threadlocal;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by boysz on 2018/12/4.
 */
public class SerialIntegerContext {

    /**
     * 每一个线程都有自己的ThreadLocal序列，自己维护自己被调用的次数。线程之间互不干扰。
     */
        static final ThreadLocal<AtomicInteger> serialThreadLocal = new ThreadLocal<AtomicInteger>(){
            @Override
            protected synchronized AtomicInteger initialValue(){
                return new AtomicInteger(1);
            }

        };

        public static int get() {
            return serialThreadLocal.get().getAndAdd(1);
        }


}
