package com.nameless.base.proxy.dynamic;

import java.util.Date;

public class HelloServiceImpl implements IHelloService {
	
	private String name ;
	private Integer age ;
	private Date date ;

	public HelloServiceImpl(){}
	
	public HelloServiceImpl(String name , Integer age , Date date ){
		this.name = name ;
		this.age = age ;
		this.date = date ;
	}
	
	public String call(String message) {
		String result = String.format("Somebody call message : %s", message);
		if(name != null) {
			result += "\n name="+name;
		}
		if(age != null) {
			result += "\n age="+age;
		}
		if(date != null) {
			result += "\n date="+date.toString();
		}
		return result ;
	}

}
