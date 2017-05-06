package com.nameless.rpc.server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import com.nameless.rpc.service.IShowService;



public class RPCServer {

	public static void main(String[] args) {
		Registry registry;
		try {
			//将iShow1注册到端口8001上。
			IShowService iShow1 = (IShowService)UnicastRemoteObject.exportObject(new ShowServiceImpl(),8001);
			//将iShow3注册到端口8003上。
			IShowService iShow3 = (IShowService)UnicastRemoteObject.exportObject((IShowService)(message->{return "xxxxx";}),8003);
			//创建访问registry的端口
			registry = LocateRegistry.createRegistry(9000);
			registry.bind("show.port.1", iShow1 );
			registry.bind("show.port.3", iShow3 );
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static class ShowServiceImpl implements IShowService {
		@Override
		public String show(String message) throws RemoteException {
			return message + " ....... ";
		}
	}
}
