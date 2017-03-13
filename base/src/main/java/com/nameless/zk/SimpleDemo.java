package com.nameless.zk;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class SimpleDemo {

	public static void main(String[] args) throws IOException, KeeperException, InterruptedException {

		/*
		 * (1)、 OPEN_ACL_UNSAFE ：完全开放。
		 * 事实上这里是采用了world验证模式，由于每个zk连接都有world验证模式，所以znode在设置了 OPEN_ACL_UNSAFE
		 * 时，是对所有的连接开放。
		 * 
		 * (2)、 CREATOR_ALL_ACL ：给创建该znode连接所有权限。
		 * 事实上这里是采用了auth验证模式，使用sessionID做验证。所以设置了 CREATOR_ALL_ACL
		 * 时，创建该znode的连接可以对该znode做任何修改。 
		 * 
		 * (3)、 READ_ACL_UNSAFE ：所有的客户端都可读。
		 * 事实上这里是采用了world验证模式，由于每个zk连接都有world验证模式，所以znode在设置了READ_ACL_UNSAFE时，
		 * 所有的连接都可以读该znode。
		 */
		// watcher 只会工作一次，需要再次添加，若想监听请使用listener ,见SimpleListener
		SimpleWatcher sw = new SimpleWatcher();
		ZooKeeper zk = new ZooKeeper("127.0.0.1:2181", 3000, sw);
		sw.setZooKeeper(zk);
		// 查看节点是否存在
		Stat stat = zk.exists("/hello", true);
		if (stat == null) {
			// /hello 不存在，创建/hello
			// 不进行ACL权限控制，永久节点。
			zk.create("/hello", "hello".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			
		}
		// 不进行ACL权限控制，临时节点。
		// EPHEMERAL_SEQUENTIAL named like this 'temp0000000006'
		zk.create("/temp", "temp".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

		Thread.sleep(1000 * 1000L);
		zk.close();

	}
}

class SimpleWatcher implements Watcher {

	private ZooKeeper zk;

	public void setZooKeeper(ZooKeeper zk) {
		this.zk = zk ;
	}
	
	public void process(WatchedEvent event) {
		//System.out.println("事件状态:" + event.getState() + ",事件类型:" + event.getType() + ",事件涉及路径:" + event.getPath());
		if (event.getState() == KeeperState.SyncConnected) {
			if (event.getType() == EventType.None && null == event.getPath()) {
				try {
					Stat noneStat = zk.exists("/temp", true);
					System.out.println("None:"+noneStat);
				} catch (KeeperException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} else {
				try {
					if (event.getType() == EventType.NodeCreated) {
						System.out.println(event.getPath() + " created");
						Stat nodeCreatedStat = zk.exists(event.getPath(), true);
						System.out.println(nodeCreatedStat);
						System.out.println(zk.getData(event.getPath(), false, nodeCreatedStat));
					} else if (event.getType() == EventType.NodeDataChanged) {
						System.out.println(event.getPath() + " updated");
						// 如果 zk.exists(xxx,true) 那么会把 new ZooKeeper时依赖的Watcher 重新加载
						Stat nodeDataChangedStat = zk.exists(event.getPath(), true) ;
						System.out.println(nodeDataChangedStat );
						System.out.println(zk.getData(event.getPath(), false, nodeDataChangedStat ));
					} else if (event.getType() == EventType.NodeDeleted) {
						System.out.println(event.getPath() + " deleted");
						System.out.println(zk.exists(event.getPath(), true));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}
	}

}