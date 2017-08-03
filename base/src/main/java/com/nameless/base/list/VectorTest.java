package com.nameless.base.list;

import java.util.Stack;

public class VectorTest {

	public static void main(String[] args) {
		
		Stack<String> v = new Stack<String>();
		//依次三次入栈
		v.push("A");
		v.push("B");
		v.push("C");
		
		System.out.println(v);
		
		//访问第一个元素(按出栈顺序排列的第一个。)，不做出栈操作。
		System.out.println(v.peek());
		System.out.println(v);
		
		//从栈中调出第一个元素
		System.out.println(v.pop());
		System.out.println(v);
		
	}
}
