package com.nameless.rpc.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

@FunctionalInterface
public interface IShowService extends Remote {

	String show(String message) throws RemoteException ;
}
