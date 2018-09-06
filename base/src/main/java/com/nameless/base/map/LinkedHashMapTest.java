package com.nameless.base.map;

import java.util.*;

public class LinkedHashMapTest {

	public static void main(String[] args) {

		/*
		LinkedHashMap<String,Integer> scores = new LinkedHashMap<>();
		scores.put("语文", 80);
		scores.put("英文", 82);
		scores.put("数学", 76);
		// 遍历scores里的所有的key-value对
		for (Object key : scores.keySet()) {
			System.out.println(key + "------>" + scores.get(key));
		}
		*/


		LinkedHashMap<String,Integer> scores = new LinkedHashMap<String,Integer>(16,0.75f,true);

		scores.put("语文", 80);
		scores.put("英文", 82);
		scores.put("数学", 76);

		scores.get("语文");
		System.out.println(scores.toString());

		scores.get("数学");
		scores.get("数学");
		System.out.println(scores.toString());

		// 遍历scores里的所有的key-value对
		Iterator<Map.Entry<String,Integer>> iter = scores.entrySet().iterator();
		while (iter.hasNext()){
			Map.Entry<String,Integer> entry = iter.next();
			System.out.println(entry.toString());
		}

		// speed test
		LinkedHashMap<String,Integer> speedmap = new LinkedHashMap<String,Integer>(16,0.75f,true);
		//HashMap<String,Integer> speedmap = new HashMap<String,Integer>();
		for(int i=0;i<10000000;i++){
			speedmap.put("S_"+i,i);
		}

		System.out.println(System.currentTimeMillis());
		speedmap.get("S_1");
		System.out.println(System.currentTimeMillis());
		speedmap.get("S_9000000");
		System.out.println(System.currentTimeMillis());
	}
}