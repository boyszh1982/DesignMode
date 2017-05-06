package com.nameless.rpc.export;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import com.nameless.rpc.service.IShowService;

public class RPCExport {

	public static void main(String[] args) throws RemoteException, AlreadyBoundException {
		// 将iShow2注册到端口8002上。与RPCServer.java中的iShow1不同（iShow1注册到端口8001上）
		IShowService iShow2 = (IShowService)UnicastRemoteObject.exportObject(new ShowServiceImpl(),8002);
		// 先启动 com.nameless.rpc.server.RPCServer.java ,之后就可以获得registry
		Registry registry = LocateRegistry.getRegistry("127.0.0.1",9000);
		registry.bind("show.port.2", iShow2 );
	}
	
	private static class ShowServiceImpl implements IShowService {
		@Override
		public String show(String message) throws RemoteException {
			return message + " 2 ....... ";
		}
	}
}
