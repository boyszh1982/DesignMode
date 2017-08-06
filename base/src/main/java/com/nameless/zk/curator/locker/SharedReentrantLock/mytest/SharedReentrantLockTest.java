package com.nameless.zk.curator.locker.SharedReentrantLock.mytest;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class SharedReentrantLockTest {

	public static void main(String[] args) throws InterruptedException {

		int poolsize = 20;
		// 初始化10000库存
		GoodsStoreResource resource = new GoodsStoreResource(10);
		
		ArrayList<CuratorFramework> clients = new ArrayList<>();
		for(int i=0;i<poolsize;i++){
			CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1", new ExponentialBackoffRetry(1000, 3));
			client.start();
			clients.add(client);
		}

		ExecutorService service = Executors.newFixedThreadPool(poolsize);
		for (int i = 0; i < poolsize; i++) {
			service.submit(new CallBusiness(clients.get(i), resource, true));
		}
		// shutdown 放在awaitTermination,当线程在await时间内完成才会结束主线程。
		service.shutdown();
		service.awaitTermination(30 * 1000L, TimeUnit.MILLISECONDS);
		// System.out.println(resource.getStore());

	}
}

class CallBusiness implements Callable<String> {

	// 默认设置isRun = false
	private AtomicBoolean isRun = new AtomicBoolean(false);

	private CuratorFramework client;

	private GoodsStoreResource resource;

	private boolean reentrantAble = true; // 默认使用重入锁

	// 重入锁,reentrant,会不停的循环等待运行条件满足在执行，不会退出
	private InterProcessMutex reentrantLock;

	// 非重入锁, ?????? 重入，非重入 究竟有什么区别？没有测试出来。
	private InterProcessSemaphoreMutex noneReentrantLock;

	public CallBusiness(CuratorFramework c, GoodsStoreResource r, boolean reentrantAble) {
		client = c;
		resource = r;
		this.reentrantAble = reentrantAble;
		if (reentrantAble) {
			reentrantLock = new InterProcessMutex(client, "/reentrantLock");
		} else {
			noneReentrantLock = new InterProcessSemaphoreMutex(client, "/noneReentrantLock");
		}
	}

	private InterProcessLock getLock() {
		if (reentrantAble) {
			return reentrantLock;
		} else {
			return noneReentrantLock;
		}
	}

	@Override
	public String call() throws Exception {
		// 取锁失败
		if (!getLock().acquire(100 * 1000, TimeUnit.MILLISECONDS)) {
			throw new Exception(Thread.currentThread().getName()+" get lock fail !");
		}
		
		// 测试入锁，？什么场景会出现重入？，好像不简单是乐观锁的问题。
		/*if (!getLock().acquire(100 * 1000, TimeUnit.MILLISECONDS)) {
			throw new Exception(Thread.currentThread().getName()+" get lock fail !");
		}*/
		
		try {
			
			// 如果预期值(expect=false) == isRun.value(当前值)，则修改
			// isRun.value=${update}(新值update=true)
			// 并且如果except=isRun.value 返回true;
			boolean expect = false;
			boolean update = true;
			if (isRun.compareAndSet(expect, update)) {
				if (resource.getStore() > 0) {
					int x = (int) (Math.random() * 900 + 100);
					resource.saleone();
					System.out.println(Thread.currentThread().getName() + ":" + resource.getStore() + " - sleep("+x+")");
					Thread.sleep(x);
				}
			}

		} finally {
			isRun.set(false);
			getLock().release();
		}

		return null;
	}

}
