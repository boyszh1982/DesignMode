package com.nameless.base.list;

import java.util.LinkedList;

public class LinkedListTest {

	public static void main(String[] args) {
		
		LinkedList<String> books = new LinkedList<String>();
		books.add("A");
		books.add("B");
		books.add("C");
		//1.将字符串元素加入队列的尾部(双端队列)
		books.offer("END");
		System.out.println(books);
		//2.将一个字符串元素加入栈的顶部(双端队列)
		books.push("BEGIN");
		System.out.println(books);
		//3.将字符串元素添加到队列的头(相当于栈的顶部) 与 2有什么区别 ?
        books.offerFirst("疯狂Android讲义");
        System.out.println(books);
        
        //4.访问不删除
        System.out.println(books.peek());
        
        //5.访问删除
        System.out.println(books.pop());
        System.out.println(books);
        
        //6.访问并删除最后一个
        System.out.println(books.pollLast());
        System.out.println(books);
        
	}
}
