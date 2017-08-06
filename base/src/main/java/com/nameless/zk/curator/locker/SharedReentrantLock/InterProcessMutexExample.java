package com.nameless.zk.curator.locker.SharedReentrantLock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

/**
 * 可重入锁，什么场景客户端需要多次获取锁？
 * @author Administrator
 *
 */
public class InterProcessMutexExample {
	private static final int QTY = 5;
	private static final int REPETITIONS = QTY * 10;
	private static final String PATH = "/examples/locks";

	public static void main(String[] args) throws Exception {
		final FakeLimitedResource resource = new FakeLimitedResource();
		final List<CuratorFramework> clientList = new ArrayList<CuratorFramework>();
		// 初始化zk连接
		for (int i = 0; i < QTY; i++) {
			CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181",
					new ExponentialBackoffRetry(1000, 3));
			client.start();
			clientList.add(client);
		}
		System.out.println("连接初始化完成！(并未开始连接zk)");
		
		// 线程池
		ExecutorService service = Executors.newFixedThreadPool(QTY);
		for (int i = 0; i < QTY; ++i) {
			final int index = i;
			// 带返回值的Runnable
			Callable<Void> task = new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					try {
						final ExampleClientThatLocks example = new ExampleClientThatLocks(clientList.get(index), PATH,
								resource, "Client " + index);
						for (int j = 0; j < REPETITIONS; ++j) {
							example.doWork(10, TimeUnit.SECONDS);
						}
					} catch (Throwable e) {
						e.printStackTrace();
					} finally {
						CloseableUtils.closeQuietly(clientList.get(index));
					}
					return null;
				}
			};
			service.submit(task);
		}
		//service.shutdown(); // ? shutdown() 应该放在最后变吧？
		service.awaitTermination(10, TimeUnit.MINUTES);
		service.shutdown();
		System.out.println("OK!");
	}
}