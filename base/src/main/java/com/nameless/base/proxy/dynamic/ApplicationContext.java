package com.nameless.base.proxy.dynamic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 模拟spring通过xml把HelloServiceImpl实例化的过程
 * 包括单例模式，动态代理，实现aop功能。
 * @author Administrator
 *
 */
public class ApplicationContext {
	
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		
		IHelloService hs1 = (IHelloService) BeanFactory.getBean("helloService");
		String message1 = hs1.call("hs1.message");
		System.out.println(hs1.toString()+" - "+message1);
		
		
		IHelloService hs2 = (IHelloService) BeanFactory.getBean("helloService");
		String message2 = hs2.call("hs2.message");
		System.out.println(hs2.toString()+" - "+message2);
		
		
	}
}

/**
 * Bean工厂
 * @author Administrator
 *
 */
class BeanFactory {
	//beanId与bean类型的映射表	
	private static final Map<String,String> beanMap = new HashMap<String,String>();
	//beanId与构造函数参数的映射表
	private static final Map<String,List<Object>> beanConstructorParamsMap = new HashMap<String,List<Object>>();
	//Proxy.newProxyInstance生成对象的存储库
	private static final Map<String,Object> beanStoreMap = new HashMap<String,Object>();
	
	static {
		beanMap.put("helloService", "com.nameless.base.proxy.dynamic.HelloServiceImpl");
		List<Object> paramsList = new ArrayList<Object>();
		paramsList.add("Dick");
		paramsList.add(20);
		paramsList.add(new java.util.Date());
		beanConstructorParamsMap.put("helloService", paramsList);
	}
	
	private BeanFactory(){}
	
	public static Object getBean(String beanId) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		//如果beanStoreMap中已经有这个对象了就直接返回
		if( beanStoreMap.containsKey(beanId) ) {
			Object realObj = beanStoreMap.get(beanId);
			if(realObj != null){
				return realObj ;
			}
		}
		//没有beanId对象则生成
		else if ( beanMap.containsKey(beanId) ) {
			Class<?> clazz = Class.forName(beanMap.get(beanId));
			//判断该类型的类是否已经加载；
			Object realObj = null;
			//有参数构造函数
			if( beanConstructorParamsMap.containsKey(beanId) ){
				List<Object> paramList = beanConstructorParamsMap.get(beanId);
				Object[] params = new Object[paramList.size()];
				Class<?>[] paramsClass = new Class[paramList.size()];
				for(int i=0;i<paramList.size();i++ ){
					params[i] = paramList.get(i);
					paramsClass[i] = paramList.get(i).getClass();
				}
				realObj = clazz.getConstructor(paramsClass).newInstance(params);
			}
			else{
				//无参数构造函数
				realObj = clazz.getConstructor().newInstance();
			}
			
			InvocationHandler handler = new BeanInvocationHandler(realObj);
			Object proxyObj = Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), handler);
			beanStoreMap.put(beanId, proxyObj);
			
			//产生类文件,可以反编译$Proxy0 的内部结构
			byte[] b = sun.misc.ProxyGenerator.generateProxyClass("$Proxy0", new Class[]{clazz});
			try (
					java.io.FileOutputStream fout = new java.io.FileOutputStream("C:/temp/$Proxy0.class");
			) {
				fout.write(b);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//#产生类文件,可以反编译$Proxy0 的内部结构
			
			return proxyObj ;
		}
		return null;
	}
}

class BeanInvocationHandler implements InvocationHandler {
	
	/*
	 * 代理对真实属性的引用,关联
	 */
	private Object obj ;
	
	public BeanInvocationHandler(Object o){
		obj = o;
	}
	
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		//System.out.println("Before call method : " + method.getName() );
		Object result = method.invoke(obj, args);
		//System.out.println("After call method : " + method.getName() + " return : " + result );
		
		return result;
	}
	
}


