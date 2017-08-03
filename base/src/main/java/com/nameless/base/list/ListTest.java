package com.nameless.base.list;

import java.util.ArrayList;
import java.util.List;

public class ListTest {

	public static void main(String[] args) {
		
		
		List<String> books = new ArrayList<String>();
		
		//向books添加3个元素,按插入顺序？
		System.out.println("------ 向books添加3个元素,按插入顺序？ ------");
		books.add(new String("0.轻量级Java EE企业应用实战"));
        books.add(new String("1.疯狂Java讲义"));
        books.add(new String("2.疯狂Android讲义"));
        System.out.println(books);
		
		//在第二个位置加一本书
        System.out.println("------ 在第二个位置加一本书 ------");
		books.add(1,new String("1.疯狂Ajax讲义"));
		for(String book : books){
			int i = books.indexOf(book);
			System.out.println(String.format("books=%s index=%s", book,i));
		}
		
		//删除第三本书
		books.remove(2);
		System.out.println(books);
		
		//判断元素在集合中的位置
		System.out.println(books.indexOf(new String("1.疯狂Ajax讲义")));
		
		//将第二个元素换成新的
		books.set(1, new String("1.疯狂Ajax讲义new"));
		System.out.println(books);
		
		//取子集合从2到4不包含4的子集合
		System.out.println(books.subList(1, 3));
		
	}
}
