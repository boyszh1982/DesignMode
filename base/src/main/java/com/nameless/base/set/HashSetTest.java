package com.nameless.base.set;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.TreeSet;

public class HashSetTest {
	
	
	/*
	1) HashSet的性能总是比TreeSet好(特别是最常用的添加、查询元素等操作)，因为TreeSet需要额外的红黑树算法来维护集合元素的次序。只有当需要一个保持排序的Set时，才应该使用TreeSet，否则都应该使用HashSet
	2) 对于普通的插入、删除操作，LinkedHashSet比HashSet要略慢一点，这是由维护链表所带来的开销造成的。不过，因为有了链表的存在，遍历LinkedHashSet会更快
	3) EnumSet是所有Set实现类中性能最好的，但它只能保存同一个枚举类的枚举值作为集合元素
	4) HashSet、TreeSet、EnumSet都是"线程不安全"的，通常可以通过Collections工具类的synchronizedSortedSet方法来"包装"该Set集合。
	SortedSet s = Collections.synchronizedSortedSet(new TreeSet(...));
	
	理解:
	TreeSet 有红黑树判断，插入、遍历速度最慢，不过可以保证想要的顺序
	HashSet 用hashCode确定元素位置，插入和查询最快
	LinkedHashSet 是链表结构，遍历最快，不用看顺序直接找下一个。
	
	*/
	public static void main(String[] args) {
		//HashSet
		System.out.println("------ HashSet ------");
		HashSet set = new HashSet();
		set.add(new A());
		set.add(new A());
		set.add(new B());
		set.add(new B());
		set.add(new C());
		set.add(new C());
		System.out.println(set);
		System.out.println(String.format("A=A ? %s", new A().equals(new A())));
		System.out.println(String.format("B=B ? %s", new B().equals(new B())));
		System.out.println(String.format("C=C ? %s", new C().equals(new C())));
		
		//LinkedHashSet
		System.out.println("------ LinkedHashSet ------");
		LinkedHashSet<? extends Object> lset = null;
		LinkedHashSet<Object> t = new LinkedHashSet<Object>();
		t.add(new A());
		t.add(new A());
		t.add(new B());
		t.add(new B());
		t.add(new C());
		t.add(new C());
		lset = t;
		System.out.println(lset);
		
		//TreeSet 红黑树存储
		System.out.println("------ TreeSet.自然排序 ------");
		String[] months = new String[]{"201701","201702","201703","201704","201705","201706",
				"201707","201708","201709","201710","201711","201712"};
		TreeSet<String> tset = new TreeSet<String>();
		for(String month : months ){
			tset.add(month);
		}
		System.out.println(tset);
		System.out.println(tset.first());
		System.out.println(tset.last());
		//返回小于201709的集合,不包含201709
		System.out.println(tset.headSet("201709"));
		//返回大于201709的集合,包含201709
		System.out.println(tset.tailSet("201709"));
		//返回大于201709,小于等于201711的集合
		System.out.println(tset.subSet("201709", "201711"));
		
		System.out.println("------ TreeSet.定制排序 ------");
		TreeSet<String> treeOrderSet = new TreeSet<String>(new Comparator<String>(){
			@Override
			public int compare(String s1, String s2) {
				return Integer.valueOf(s2) - Integer.valueOf(s1);
			}
		});
		for(String month : months ){
			treeOrderSet.add(month);
		}
		System.out.println(treeOrderSet);
		
		
		//EnumSet
		System.out.println("------ EnumSet ------");
		EnumSet<Season> eset1 = EnumSet.allOf(Season.class);
		System.out.println(eset1);
		EnumSet<Season> eset2 = EnumSet.noneOf(Season.class);
		System.out.println(eset2);
		eset2.add(Season.SPRING);
		System.out.println(eset2);
		EnumSet<Season> eset3 = EnumSet.of(Season.SPRING,Season.WINTER);
		System.out.println(eset3);
		//print [SUMMER, FAIL, WINTER]
		EnumSet<Season> eset4 = EnumSet.range(Season.SUMMER, Season.WINTER);
		System.out.println(eset4);
		//创建一个从Season全集中排除EnumSet.range(Season.SPRING, Season.WINTER)的集合
		EnumSet<Season> eset5 = EnumSet.complementOf(EnumSet.range(Season.SPRING, Season.WINTER));
		System.out.println(eset5);
	}
}

enum Season {
	SPRING,SUMMER,FAIL,WINTER
}

class A {
	
	@Override
	public boolean equals(Object obj) {
		System.out.println(String.format("this.hashCode=%s obj.hashCode=%s", this.hashCode(),obj.hashCode()));
		//super.equals(obj);
		return true;
	}
	/*
		1、hashCode的存在主要是用于查找的快捷性，如Hashtable，HashMap等，hashCode是用来在散列存储结构中确定对象的存储地址的；
		2、如果两个对象相同，就是适用于equals(Java.lang.Object) 方法，那么这两个对象的hashCode一定要相同；
		3、如果对象的equals方法被重写，那么对象的hashCode也尽量重写，并且产生hashCode使用的对象，一定要和equals方法中使用的一致，否则就会违反上面提到的第2点；
		4、两个对象的hashCode相同，并不一定表示两个对象就相同，也就是不一定适用于equals(java.lang.Object) 方法，只能够说明这两个对象在散列存储结构中，如Hashtable，他们“存放在同一个篮子里”。
	 	如果重写了equals方法使两个对象相等， 如果hashCode不同，则进入hashMap则可以保存多个。
	 */
	@Override
	public int hashCode() {
		//return super.hashCode();
		return 1;
	}
}

class B {
	@Override
	public boolean equals(Object obj) {
		System.out.println(String.format("this.hashCode=%s obj.hashCode=%s", this.hashCode(),obj.hashCode()));
		return true;
	}
}

class C {
	@Override
	public int hashCode() {
		return 1;
	}
}