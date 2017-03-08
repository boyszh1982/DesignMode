package com.nameless.base.proxy;

public class ProxyConsole {

	/**
	 * 模仿spring 实现单例
	 * @param args
	 */
	private Object obj ;
	
	public synchronized Object getBean(String classname){
		if(obj == null){
			try {
				obj = Class.forName(classname).newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return obj;
	}
	
	public static void main(String[] args) {
		// 利用类的名字生成了一个事例，且单例。
		Object obj = new ProxyConsole().getBean("com.nameless.base.proxy.RealCustomer");
		// 将obj转换为抽象类，调用方法。ProxyConsole的启动相当于spring启动，整个容器内obj为单例。
		AbstractCustomer pc = (AbstractCustomer)obj;
		pc.buyTickets();
		
	}
}
