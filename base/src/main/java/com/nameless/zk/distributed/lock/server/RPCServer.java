package com.nameless.zk.distributed.lock.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedHashMap;
import java.util.Map;

import com.nameless.zk.distributed.lock.service.ISaleTicket;

public class RPCServer {

	/**
	 * 在Client.java中用多线程模拟多各户买票，
	 * Server中有个票库，
	 * Client通过访问ZooKeeper 创建临时节点实现抢锁。
	 * 运行环境，启动ZooKeeper , Server , Client
	 * @param args
	 * @throws RemoteException
	 */
	public static void main(String[] args) throws RemoteException {
		String ticketKey = "shenyang->shenzhen";
		Map<String,Integer> ticketMap = new LinkedHashMap<String,Integer>();
		ticketMap.put(ticketKey, 100);

		ISaleTicket st = (ISaleTicket) UnicastRemoteObject.exportObject(
				(ISaleTicket)( ticketKeyParam -> {
					Integer tickets = ticketMap.get(ticketKeyParam);
					if(tickets <= 0 ){
						tickets = 0;
						return tickets;
					}
					else{
						tickets = tickets - 1;
						ticketMap.put(ticketKeyParam, tickets);
						System.out.println("剩余票数:"+ticketMap.get(ticketKeyParam));
						return tickets ;
					}
				} )
				, 7001);
				

		Registry registry = LocateRegistry.createRegistry(7000);
		registry.rebind("sale", st);
	}
}
