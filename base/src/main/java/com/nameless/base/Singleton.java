package com.nameless.base;

/**
 * Design mode
 * 单例模式
 *     懒汉（线程安全几种方式，本例介绍最简单的一种。）
 *     饿汉（本身线程安全）
 * 工厂模式（SPRING-IOC）
 * 
 * 代理模式（SPRING-AOP）
 * 
 * 包装模式
 */
public class Singleton 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }
}

/**
 * 单例模式
 * 线程安全
 * 懒汉式
 * @author Administrator
 */
class SyncSingletonLazyInit {
	private static SyncSingletonLazyInit _instance ;
	private SyncSingletonLazyInit(){}
	public static synchronized SyncSingletonLazyInit getInstance(){
		if(_instance != null){
			_instance = new SyncSingletonLazyInit();
		}
		return _instance;
	}
}

/**
 * 单例模式
 * 饿汉式
 * 这本身就不存在线程安全不安全的问题
 * @author Administrator
 *
 */
class SingletonInit {
	private static final SingletonInit _instance = 
			new SingletonInit();
	private SingletonInit(){}
	public static SingletonInit getInstance(){
		return _instance;
	}
}
