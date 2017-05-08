package com.nameless.zk.distributed.lock;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.EventType;

public class LockWatcher implements Watcher {

	private ZooKeeper zk ;
	
	public void setZooKeeper(ZooKeeper zk) {
		this.zk = zk ;
	}
	
	@Override
	public void process(WatchedEvent event) {
		//监听锁的建立
		if ( event.getType() == EventType.NodeCreated ) {
			System.out.println(event.getPath() + " created");
		} 
		//监听锁的删除
		else if (event.getType() == EventType.NodeDeleted) {
			System.out.println(event.getPath() + " deleted");
		}
	}

}
