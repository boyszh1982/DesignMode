package com.nameless.base.threadlocal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by boysz on 2018/12/4.
 */
public class Console {

    public static void main(String[] args) throws InterruptedException {


        List<Thread> threads = new ArrayList<>();
        for(int i=0;i<20;i++){
            threads.add(new MyThread("name-"+i));
        }

        for(Thread thread : threads){
            thread.start();
        }

        TimeUnit.MILLISECONDS.sleep(2000);

        for(Thread thread : threads){
            System.out.println(thread.getState());
        }

//        TimeUnit.MILLISECONDS.sleep(2000);
//        for(Thread thread : threads){
//            thread.start();
//        }

    }
}


