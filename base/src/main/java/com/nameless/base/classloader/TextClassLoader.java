package com.nameless.base.classloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TextClassLoader extends ClassLoader {

	/**
	 * 热加在
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		TextClassLoader tcl = new TextClassLoader();
		String classpath = CustomCL.getSystemResource("com/nameless/base/classloader/Foo.class").getPath();
		File classF = new File(classpath);
		FileInputStream fis = new FileInputStream(classF);
		byte[] bytes = new byte[(int) classF.length()];
		fis.read(bytes);
		Class<?> clazz = tcl.defineClass("com.nameless.base.classloader.Foo", bytes, 0, bytes.length);
		Object obj = clazz.newInstance();
		Method method = clazz.getMethod("sayHello");
		method.invoke(obj);
	}
	
	//直接读取文字是不可以使用的
	public static void main2(String[] args) throws UnsupportedEncodingException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		TextClassLoader tcl = new TextClassLoader();
		String classname = "Foo";
		String classbody = "public class Foo {	public void sayHello() { 		System.out.println(\"hello world! (version one)\");		    } }";
		Class clazz = tcl.loadTextClass("Foo",classbody.getBytes());
		Object obj = clazz.newInstance();
		Method method = clazz.getMethod("sayHello", null);
		method.invoke(obj, null);
	}

	private Class loadTextClass(String name,byte[] bytes) throws ClassNotFoundException {
		defineClass(name, bytes, 0, bytes.length);
		return null;
		//return loadClass(name);
	}
	
	
	
}
