package com.nameless.rpc.export;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import com.nameless.rpc.service.IShowService;

public class RPCExportOtherHost {
	
	public static void main(String[] args) throws RemoteException, NotBoundException, AlreadyBoundException {
		//测试调用远程服务器注册的API
		callRemoteRegistory(args);
		
		//注册API到远程服务器
		/**
		 * 测试结果，不能注册到远程服务的Registry中
		 */
		reg2remote(args);
		
		//调用刚注册的API
		callMyRegistory(args);
	}
	
	public static void reg2remote(String[] args) throws RemoteException, AlreadyBoundException {
		// 将iShow2注册到端口8002上。与RPCServer.java中的iShow1不同（iShow1注册到端口8001上）
		IShowService iShow2 = (IShowService)UnicastRemoteObject.exportObject((IShowService)(message->{return "abcdefg";}),8009);
		// 先启动 com.nameless.rpc.server.RPCServer.java ,之后就可以获得registry
		Registry registry = LocateRegistry.getRegistry("192.168.128.141",9000);
		registry.bind("show.port.9", iShow2 );
	}
	
	public static void callMyRegistory(String[] args) throws RemoteException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry("192.168.128.141", 9000);
		IShowService iShow = (IShowService)registry.lookup("show.port.9");
		System.out.println(iShow.show("1111"));
	}

	public static void callRemoteRegistory(String[] args) throws RemoteException, NotBoundException {
		
		Registry registry = LocateRegistry.getRegistry("192.168.128.141", 9000);
		String[] lookups = registry.list();
		for(String l : lookups){
			System.out.println(l);
		}
		IShowService iShow = (IShowService)registry.lookup("show.port.1");
		System.out.println(iShow.show("1111"));
	}
}
