package com.nameless.base.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {

	public static int cnt = 0;
	
	public static void main(String[] args) throws InterruptedException {
		//true=fair 公平锁，所有新进入的线程都放在等待队列的尾部。
		//false=nonfair 非公平锁，新进来的线程限制性，后执行等待队列的线程。
		ReentrantLock lock = new ReentrantLock(true);
		
		MyResource r = new MyResource();
		
		ExecutorService service = Executors.newFixedThreadPool(1);
		for(int i=0;i<10000;i++){
			Thread t = new Thread(new MyRunnable(r,lock));
			service.execute(t);
		}
		System.out.println(service.getClass());
		service.shutdown();
		service.awaitTermination(1, TimeUnit.MINUTES);
		
		System.out.println(r.getCnt());
		
	}
}

class MyRunnable implements Runnable{
	private ReentrantLock lock = null;
	private MyResource r = null;
	public MyRunnable(MyResource r,ReentrantLock lock){
		this.r = r;
		this.lock = lock;
	}
	
	@Override
	public void run() {
		try {
			
			/*
			 * lock.lock() 调用了 sync.lock()
			 * Sync 继承了 AbstractQueuedSynchronizer(一个阻塞队列)
			 */
			lock.lock();
			r.addone();
			/*
			if(lock.tryLock()){
				r.addone();
			}
			else{
				
			}
			*/
		} catch (IllegalMonitorStateException e) {
			System.out.println("没拿到锁");
		} finally {
			lock.unlock();
		}
		
		
	}
}

class MyResource {
	private int cnt = 0;
	
	public void addone() {
		
		try {
			//模拟业务时间
			Thread.sleep(1000);
			this.cnt++;
			System.out.println(Thread.currentThread().getName()+","+this.cnt);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public int getCnt() {
		return cnt;
	}
	
	
}