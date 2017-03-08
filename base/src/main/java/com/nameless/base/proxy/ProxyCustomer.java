package com.nameless.base.proxy;

import com.nameless.base.proxy.AbstractCustomer;

/**
 * 代理模式（静态）-客户雇佣的代理
 * 1,客户要买机票，代理帮她买。（代理需要买票方法）
 * 2,客户代理 帮哪个客户买票？（需要有对真实客户的引用,成员变量。）
 * 3,代理买票是帮真实客户买票，所以代理买票方法中要有客户买票的动作。
 * 4,客户代理与真实客户都有买票动作。可以抽取出接口。
 * @author Administrator
 *
 */
public class ProxyCustomer extends AbstractCustomer {

	private AbstractCustomer customer ;
	
	public ProxyCustomer(AbstractCustomer customer){
		this.customer = customer;
	}
	
	public void buyTickets(){
		System.out.println("想想SPRING-AOP.before");
		customer.buyTickets();
		System.out.println("想想SPRING-AOP.after");
	}
}
