package com.nameless.base.memcached.ConsistentHash;

import java.util.HashSet;
import java.util.Set;

public class ConsistentHashTest {

	/**
	 * http://www.cnblogs.com/hupengcool/p/3659016.html
	 * https://github.com/julycoding/The-Art-Of-Programming-By-July/blob/master/ebook/zh/06.09.md
	 * @param args
	 */
	public static void main(String[] args) {

		//服务器格数
		Set<String> nodes = new HashSet<String>();
		nodes.add("A");
		nodes.add("B");
		nodes.add("C");
		ConsistentHash<String> consistentHash = new ConsistentHash<String>(new HashFunction(), 160, nodes);

		consistentHash.add("D");
		System.out.println(consistentHash.getSize()); // 640

		for(int i=0;i<1000;i++){
			System.out.println(consistentHash.get(i+""));
		}
		System.out.println(consistentHash.get("test0"));
		
		
	}

}