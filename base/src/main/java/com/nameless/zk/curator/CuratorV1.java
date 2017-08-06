package com.nameless.zk.curator;

import org.apache.curator.framework.CuratorFramework;

public class CuratorV1 extends AbstractCurator {

	private static CuratorParams cp = new CuratorParams();

	{
		cp.setConnectString("127.0.0.1:2181");
		cp.setBaseSleepTimeMs(1000);
		cp.setMaxRetries(3);
	}

	@Override
	public CuratorParams ready() {
		return cp;
	}

	@Override
	public ZNodeParams createPersistentNode(CuratorFramework client) {
		ZNodeParams znp = new ZNodeParams();
		znp.setZnode("absznode");
		znp.setZdata("封装了个抽象");
		znp.setClient(client);
		try {
			createPersistent(znp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return znp;
	}

	// 测试
	public static void main(String[] args) {
		CuratorV1 cv1 = new CuratorV1();
		cv1.doit();
	}

}
