package com.nameless.zk.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;

public abstract class AbstractCurator {

	private String znoderoot = "/com/nameless/";
	private String connectString = null;
	private int baseSleepTimeMs = 0;
	private int maxRetries = 0;
	private CuratorFramework client = null;

	public abstract CuratorParams ready();
	public abstract ZNodeParams createPersistentNode(final CuratorFramework client);

	// 子类不可Overide该方法。
	public final void doit() {
		CuratorParams cp = ready();
		this.connectString = cp.getConnectString();
		this.baseSleepTimeMs = cp.getBaseSleepTimeMs();
		this.maxRetries = cp.getMaxRetries();
		if (this.connectString != null && this.baseSleepTimeMs != 0 && this.maxRetries != 0) {
			// 创建连接
			client = CuratorFrameworkFactory.newClient(connectString,
					new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries));
		}
		else {
			throw new CuratorNoParamsException();
		}
		try {
			client.start();
			//do something abstract
			ZNodeParams znp = createPersistentNode(client);
			System.out.println(String.format("znode=%s zdata=%s", znp.getZnode() , znp.getZdata()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (client != null)
				client.close();
		}
		
	}

	// 创建永久性节点 , 提供给子类和同包下的类使用。
	protected CuratorFramework createPersistent(ZNodeParams znp) throws Exception {
		// fluent 风格，方法返回client对象，可以继续调用。
		znp.getClient().create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT)
				.withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE).forPath(znp.getZnode(znoderoot), znp.getZdata());
		return znp.getClient();
	}

	// 创建临时节点
	protected CuratorFramework createEphemeral(ZNodeParams znp) throws Exception {
		znp.getClient().create().withMode(CreateMode.EPHEMERAL).forPath(znp.getZnode(znoderoot), znp.getZdata());
		return znp.getClient();
	}

	// 删除节点
	protected CuratorFramework delete(ZNodeParams znp) throws Exception {
		znp.getClient().delete().forPath(znp.getZnode(znoderoot));
		return client;
	}

	// 获取节点值
	protected ZNodeParams getDataForPath(ZNodeParams znp) throws Exception {
		byte[] res = znp.getClient().getData().forPath(znp.getZnode(znoderoot));
		znp.setZdata(res);
		return znp;
	}

	// 设置节点值
	protected void setDataForPath(ZNodeParams znp) throws Exception {
		znp.getClient().setData().inBackground().forPath(znp.getZnode(znoderoot), znp.getZdata());
	}

	// 判断节点是否已经存在
	protected boolean exists(ZNodeParams znp) throws Exception {
		Stat stat = znp.getClient().checkExists().forPath(znp.getZnode(znoderoot));
		if (stat == null) {
			return false;
		} else {
			return true;
		}
	}

}
