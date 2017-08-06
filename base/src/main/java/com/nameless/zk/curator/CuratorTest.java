package com.nameless.zk.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;

public class CuratorTest {

	public static void main(String[] args) {

		// 创建连接
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181",
				// 阻塞重试3次，间隔时间1秒。
				new ExponentialBackoffRetry(1000, 3));

		try {
			client.start();

			// 指定namespace
			// client.usingNamespace("com.nameless."); ?不起作用？
			
			String path = "/com.nameless.curator";
			
			if(exists(client, path)){
				delete(client, path);
			}
			
			createPersistent(client, path, "hello curator !"); 
			String res = new String(getDataForPath(client, path));
			System.out.println(res);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (client != null)
				client.close();
		}

	}

	// 创建永久性节点
	public static CuratorFramework createPersistent(CuratorFramework client, String path, String data)
			throws Exception {
		// fluent 风格，方法返回client对象，可以继续调用。
		client.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT)
				.withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE).forPath(path, data.getBytes());
		return client;
	}

	// 创建临时节点
	public static CuratorFramework createEphemeral(CuratorFramework client, String path, String data) throws Exception {
		client.create().withMode(CreateMode.EPHEMERAL).forPath(path, data.getBytes());
		return client;
	}

	// 删除节点
	public static CuratorFramework delete(CuratorFramework client, String path) throws Exception {
		client.delete().forPath(path);
		return client;
	}

	// 获取节点值
	public static byte[] getDataForPath(CuratorFramework client, String path) throws Exception {
		byte[] res = client.getData().forPath(path);
		return res;
	}

	// 设置节点值
	public static void setDataForPath(CuratorFramework client, String path, String data) throws Exception {
		client.setData().inBackground().forPath(path, data.getBytes());
	}

	// 判断节点是否已经存在
	public static boolean exists(CuratorFramework client, String path) throws Exception {
		Stat stat = client.checkExists().forPath(path);
		if (stat == null) {
			return false;
		} else {
			return true;
		}
	}
}
