package com.nameless.zk.distributed.lock.client;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import com.nameless.zk.distributed.lock.LockWatcher;
import com.nameless.zk.distributed.lock.service.ISaleTicket;

public class Client {
	/**
	 * 通过建立临时节点方法实现分布式锁，每次都需要重新连接ZooKeeper ,需要考虑用创建永久节点方法实现见
	 * https://my.oschina.net/xianggao/blog/532010
	 * 
	 * 异步RMI如何实现，利用队列？
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		//多线程模拟多客户端调用
		for(int i=0;i<10;i++){
			new Thread(new SaleThread()).start();
			//Thread.sleep(3000);
		}
	}
}

class SaleThread implements Runnable {
		
	@Override
	public void run() {
		try {
			//RPC调用
			Registry registry = LocateRegistry.getRegistry("127.0.0.1", 7000);
			ISaleTicket st = (ISaleTicket) registry.lookup("sale");
			
			//分布式锁
			String lockName = Thread.currentThread().getName();
			LockWatcher lw = new LockWatcher();

			while(true) {
				//抢任务。关闭连接后释放锁。
				ZooKeeper zk = null;
				try {
					zk = new ZooKeeper("127.0.0.1:2181", 3000, lw);
					lw.setZooKeeper(zk);
					//能创建/ticketLock节点的客户端(不报节点已存在异常，可以判定已抢到锁。)，可以继续执行任务。不然报异常。
					zk.create("/ticketLock", lockName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
					Integer ticket = st.sale("shenyang->shenzhen");
					System.out.println("获得票号:"+ticket);
					if(ticket == 0){
						break;
					}
					//Thread.sleep(10);
				} catch (KeeperException e) {
					//没有抢到任务
					//System.out.println(lockName + " 没有抢到任务 ！");
				} finally {
					if(zk!=null)
						zk.close();
				}
			}
			
			/*
			Stat stat = zk.exists("/ticketLock", true);
			if (stat == null) {
				zk.create("/ticketLock", lockName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				Integer ticket = st.sale("shenyang->shenzhen");
				System.out.println("获得票号:"+ticket);
				Thread.sleep(3000);
			}
			*/
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}