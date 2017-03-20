package com.nameless.base.classloader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Foo {
	
	public void sayHello() {
		List list = new ArrayList();
		Collections.sort(list,new Comparator(){

			public int compare(Object o1, Object o2) {
				// TODO Auto-generated method stub
				return 0;
			}
			
		});
		
		//System.out.println("hello world! (version one)");
		System.out.println("hello world! (version two)");
	}
}
