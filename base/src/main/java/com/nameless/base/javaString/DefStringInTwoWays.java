package com.nameless.base.javaString;

public class DefStringInTwoWays {

	/*
1、栈区（stack）— 由编译器自动分配释放 ，存放函数的参数值，局部变量的值等。其操作方式类似于数据结构中的栈。

2、堆区（heap）— 由程序员分配释放， 若程序员不释放，程序结束时可能由OS回收 。注意它与数据结构中的堆是两回事，分配方式倒是类似于链表。

3、全局区（静态区）（static）— 全局变量和静态变量的存储是放在一块的，初始化的全局变量和静态变量在一块区域， 未初始化的全局变量和未初始化的静态变量在相邻的另一块区域。程序结束后由系统释放。

4、文字常量区 — 常量字符串就是放在这里的，程序结束后由系统释放 。

5、程序代码区 — 存放函数体的二进制代码。

由于String类的immutable性质，当String变量需要经常变换其值时，应 该考虑使用StringBuffer类，以提高程序效率。
	 */
	
	
	public static void main(String[] args) {
		
		String s1 = "abc";
		String s2 = "abc";
		/*
		 * 下面两个结果返回都是true
		 * 内部实现流程
		 * 每次 = "abc" 先检查stack中是否有"abc",如果没有创建并将地址给s1,
		 * 如果有则把"abc"的地址给s2
		 */
		System.out.println(s1 == s2);
		System.out.println(s1.equals(s2));

		String ns1 = new String("abc");
		String ns2 = new String("abc");
		
		/*
		 * ns1 == ns2 -> false, 因为new String 后放入堆内存，生成了两个不同的地址，
		 * 在指向ns1 和 ns2
		 */
		System.out.println(ns1 == ns2); 
		System.out.println(ns1.equals(ns2));
		
		//不正确的做法
		String msg = "A";
		msg += "B";
		msg += "C";
		//正确做法
		StringBuffer buff = new StringBuffer();
		buff.append("A");
		buff.append("B");
		buff.append("C");
			
	}
}
