package com.nameless.base.classloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;


/**
 * 已经实现了热加在类
 * 如何实现读取文本数据类呢？
 * 可以把文件写成文件，在调用某些方法使JAVA文件编译成.class在用热加载调用方法？一定行。
 * @author Administrator
 *
 */
public class CustomCL extends ClassLoader {

	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, InterruptedException {
		while(true){
			String dir = CustomCL.getSystemResource("").getPath();
			System.out.println(dir);
			CustomCL cl = new CustomCL(dir,new String[]{"com.nameless.base.classloader.Foo"});
			Class cls = cl.loadClass("com.nameless.base.classloader.Foo"); 
	        Object foo = cls.newInstance(); 
	        Method m = foo.getClass().getMethod("sayHello", new Class[]{}); 
	        m.invoke(foo, new Object[]{});
	        Thread.sleep(5*1000L);
		}
	}
	
	private String basedir; // 需要该类加载器直接加载的类文件的基目录
	private HashSet dynaclazns; // 需要由该类加载器直接加载的类名

	public CustomCL(String basedir, String[] clazns) throws FileNotFoundException, IOException {
		super(null); // 指定父类加载器为 null
		this.basedir = basedir;
		dynaclazns = new HashSet();
		loadClassByMe(clazns);
	}

	private void loadClassByMe(String[] clazns) throws FileNotFoundException, IOException {
		for (int i = 0; i < clazns.length; i++) {
			loadDirectly(clazns[i]);
			dynaclazns.add(clazns[i]);
		}
	}

	private Class loadDirectly(String name) throws FileNotFoundException, IOException {
		Class cls = null;
		StringBuffer sb = new StringBuffer(basedir);
		String classname = name.replace('.', '/') + ".class";
		sb.append(classname);
		File classF = new File(sb.toString());
		System.out.println(sb.toString());
		cls = instantiateClass(name, new FileInputStream(classF), classF.length());
		return cls;
	}

	private Class instantiateClass(String name, InputStream fin, long len) throws IOException {
		byte[] raw = new byte[(int) len];
		fin.read(raw);
		fin.close();
		return defineClass(name, raw, 0, raw.length);
	}

	protected Class loadClass(String name, boolean resolve) throws ClassNotFoundException {
		Class cls = null;
		cls = findLoadedClass(name);
		if (!this.dynaclazns.contains(name) && cls == null)
			cls = getSystemClassLoader().loadClass(name);
		if (cls == null)
			throw new ClassNotFoundException(name);
		if (resolve)
			resolveClass(cls);
		return cls;
	}

}