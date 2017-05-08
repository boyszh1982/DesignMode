package com.nameless.zk.distributed.lock.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

@FunctionalInterface
public interface ISaleTicket extends Remote {
	
	/**
	 * 卖票方法
	 * @param ticketMap 包含多种票，如 key="shenyang->shenzhen" value=100张
	 * @param ticketKey shenyang->shenzhen
	 * @return 返回票号
	 */
	public Integer sale(String ticketKey) throws RemoteException ;
}
