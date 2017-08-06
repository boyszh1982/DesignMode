package com.nameless.zk.curator.locker.SharedReentrantLock;

import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

/**
 * 然后创建一个ExampleClientThatLocks类，它负责请求锁，使用资源，释放锁这样一个完整的访问过程。
 * @author Administrator
 *
 */

public class ExampleClientThatLocks {
	
	private final InterProcessMutex lock;
	private final FakeLimitedResource resource;
	private final String clientName;

	public ExampleClientThatLocks(CuratorFramework client, String lockPath, FakeLimitedResource resource,
			String clientName) {
		this.resource = resource;
		this.clientName = clientName;
		lock = new InterProcessMutex(client, lockPath);
	}

	public void doWork(long time, TimeUnit unit) throws Exception {
		// 获得互斥锁
		if (!lock.acquire(time, unit)) {
			throw new IllegalStateException(clientName + " 不能得到互斥锁");
		}
		
		// 测试重入锁 Reentrant
		//if (!lock.acquire(time, unit)) {
		//	throw new IllegalStateException(clientName + " 不能得到互斥锁");
		//}
		//else {
		//	System.out.println("同一线程又执行了 lock.acquire(time,unit)方法");
		//}
		
		try {
			System.out.println(clientName + " 已获取到互斥锁");
			resource.use(); // 使用资源
			Thread.sleep(1000 * 1);
		} finally {
			System.out.println(clientName + " 释放互斥锁");
			lock.release(); // 总是在finally中释放
		}
	}
}