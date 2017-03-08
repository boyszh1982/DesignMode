package com.nameless.base.proxy;

/**
 * 代理模式（静态）-真实客户
 * @author Administrator
 * 真实客户相当于spring-aop要拦截的类，proxyCustomer相当于拦截器。
 */
public class RealCustomer extends AbstractCustomer {

	/*
	 * 这里实现真实的业务逻辑
	 * (non-Javadoc)
	 * @see com.nameless.base.proxy.AbstractCustomer#buyTickets()
	 */
	@Override
	public void buyTickets() {
		System.out.println("I get a tickets !");
		
	}

}
