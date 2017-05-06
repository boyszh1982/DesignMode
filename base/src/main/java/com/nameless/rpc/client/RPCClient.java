package com.nameless.rpc.client;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.nameless.rpc.service.IShowService;

public class RPCClient {

	public static void main(String[] args) {
		
		Registry registry;
		try {
			registry = LocateRegistry.getRegistry("127.0.0.1", 9000);
			Remote lookup = registry.lookup("show.port.1");
			IShowService iShow = (IShowService) lookup;
			System.out.println(iShow.show("hello ! "));
			
			lookup = registry.lookup("show.port.3");
			iShow = (IShowService) lookup;
			System.out.println(iShow.show("hello ! "));
			
			//需要启动 com.nameless.rpc.export.RPCExport.java 
			lookup = registry.lookup("show.port.2");
			iShow = (IShowService) lookup;
			System.out.println(iShow.show("hello ! "));
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
