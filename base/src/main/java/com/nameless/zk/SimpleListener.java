package com.nameless.zk;

import java.io.IOException;
import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.Watcher.Event.KeeperState;

public class SimpleListener {

	/*
	 * http://blog.csdn.net/done58/article/details/51014846 ZkClient 介绍
先看看zookeeper本身自带的客户端的问题。
1) ZooKeeper的Watcher是一次性的，用过了需要再注册；
2) session的超时后没有自动重连，生产环境中如果网络出现不稳定情况，那么这种情况出现的更加明显；
3) 没有领导选举机制，集群情况下可能需要实现stand by，一个服务挂了，另一个需要接替的效果；
4) 客户端只提供了存储byte数组的接口，而项目中一般都会使用对象。
5) 客户端接口需要处理的异常太多，并且通常，我们也不知道如何处理这些异常。

zkClient
zkClient主要做了两件事情。一件是在session loss和session expire时自动创建新的ZooKeeper实例进行重连。另一件是将一次性watcher包装为持久watcher。后者的具体做法是简单的在watcher回调中，重新读取数据的同时再注册相同的watcher实例。

I0Itec这个zookeeper客户端基本上解决了上面的所有问题，主要有以下特性：
1) 提供了zookeeper重连的特性------能够在断链的时候,重新建立连接,无论session失效与否.
2) 持久的event监听器机制------ ZKClient框架将事件重新定义分为了stateChanged、znodeChanged、dataChanged三种情况，用户可以注册这三种情况下的监听器（znodeChanged和dataChanged和路径有关），而不是注册Watcher。
3) zookeeper异常处理-------zookeeper中繁多的Exception,以及每个Exception所需要关注的事情各有不同，I0Itec简单的做了封装。
4) data序列化------简单的data序列化.(Serialzer/Deserialzer)
5）有默认的领导选举机制
请注意使用I0Itect-zkClient暂时有几个方法仍需要重写:
1) create方法 : 创建节点时,如果节点已经存在,仍然抛出NodeExistException,可是我期望它不在抛出此异常。
2) retryUtilConnected :   如果向zookeeper请求数据时(create,delete,setData等),此时链接不可用,那么调用者将会被阻塞直到链接建立成功;不过我仍然需要一些方法是非阻塞的,如果链接不可用,则抛出异常,或者直接返回。
3) create方法 :  创建节点时,如果节点的父节点不存在,我期望同时也要创建父节点,而不是抛出异常。
4) data监测:  我需要提供一个额外的功能来补充watch的不足,开启一个线程,间歇性的去zk server获取指定的path的data,并缓存起来。归因与watch可能丢失,以及它不能持续的反应znode数据的每一次变化,所以只能手动去同步获取。
	 */
	private String znode = "/listen";

	public static void main(String[] args) throws IOException, InterruptedException {

		ZkClient zc = new ZkClient("127.0.0.1:2181");
		// 子节点变化 , 创建子节点好用
		zc.subscribeChildChanges("/listen", new IZkChildListener() {
			public void handleChildChange(String parentPath, List currentChilds) throws Exception {
				System.out.println("clildren of path " + parentPath + ":" + currentChilds);
			}
		});
		
		// 数据变化 , 为什么不好用？ 
		zc.subscribeDataChanges("/hello", new IZkDataListener() {
			public void handleDataChange(String dataPath, Object data) throws Exception {
				System.out.println("Data of " + dataPath + " has changed");
			}

			public void handleDataDeleted(String dataPath) throws Exception {
				System.out.println(dataPath + " has deleted");
			}
		});
		// 连接状态变化 , 为什么不好用？
		zc.subscribeStateChanges(new IZkStateListener() {
			public void handleNewSession() throws Exception {
				System.out.println("handleNewSession()");
			}

			public void handleStateChanged(KeeperState stat) throws Exception {
				System.out.println("handleStateChanged,stat:" + stat);
			}

			public void handleSessionEstablishmentError(Throwable error) throws Exception {
				System.out.println("handleSessionEstablishmentError,stat:" + error.getMessage());

			}
		});
		
		Thread.sleep(1000 * 1000L);

	}

}
