package com.nameless.zk.curator.locker.SharedLock;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 首先让我们创建一个模拟的共享资源， 这个资源期望只能单线程的访问，否则会有并发问题。
 * @author Administrator
 *
 */

public class FakeLimitedResource {
	
	// 初始值 = false
	private final AtomicBoolean inUse = new AtomicBoolean(false);

	// 模拟只能单线程操作的资源
	public void use() throws InterruptedException {
		// 如果isUse.初始值 = 输入的false 那么把 isUse.初始值 = true
		// return true 标识isUse.value=false ,并且isUse.value=true
		// return false 标识isUse.value=true
		// 如果inUse.value依然为false,即 inUse.compareAndSet(false, true) 返回 true,线程可以进入，程序可以继续运行。
		// 如果inUse.value=true,即inUse.compareAndSet(false, true) 返沪 false,那么锁已经被其他线程使用，程序抛出异常。
		if (!inUse.compareAndSet(false, true)) {
			// 在正确使用锁的情况下，此异常不可能抛出
			throw new IllegalStateException("Needs to be used by one client at a time");
		}
		try {
			Thread.sleep((long) (3 * Math.random()));
		} finally {
			// 线程运行完将 inUse.value = false;
			inUse.set(false);
		}
	}
}