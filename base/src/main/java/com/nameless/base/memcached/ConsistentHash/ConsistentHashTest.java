package com.nameless.base.memcached.ConsistentHash;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ConsistentHashTest {

	/**
	 * http://www.cnblogs.com/hupengcool/p/3659016.html
	 * https://github.com/julycoding/The-Art-Of-Programming-By-July/blob/master/ebook/zh/06.09.md
	 * @param args
	 */
	public static void main2(String[] args) {

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


	public static void main(String[] args) {

		Set<String> nodes = new HashSet<>();

		for(int i=0;i<256 ; i++){
			nodes.add(String.valueOf(i));
		}

		Map<String,Integer> bucketCnt = new HashMap<>();

		ConsistentHash<String> consistentHash = new ConsistentHash<String>(new HashFunction(), 1, nodes);

		for(int i=0; i< 10000000 ; i++ ){
			// System.out.println( i + " >>> " + jch.calculate( String.valueOf(i) ));
			String bucket = consistentHash.get(String.valueOf(i));

			if(bucketCnt.containsKey(bucket)) {
				bucketCnt.put(bucket, bucketCnt.get(bucket) + 1);
			}
			else{
				bucketCnt.put(bucket, 1);
			}
		}

		System.out.println(bucketCnt);



	}

}