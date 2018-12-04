package com.nameless.base.map;

import java.util.*;

public class LinkedHashMapTest {

	public static void main(String[] args) {
		LinkedHashMap<String,Integer> scores = new LinkedHashMap<String,Integer>(16,0.75f,false);
		scores.put("语文", 80);
		scores.put("英文", 82);
		scores.put("数学", 76);
		System.out.println(scores.get("英文"));
	}

	public static void main2(String[] args) {

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


		HashMap<Integer,Integer> cntmap = new HashMap<Integer,Integer>();
		// speed test
		LinkedHashMap<String,Integer> speedmap = new LinkedHashMap<String,Integer>(16,0.75f,false);
		//HashMap<String,Integer> speedmap = new HashMap<String,Integer>();
		String key = null;
		for(int i=0;i<10;i++){
			key = "S_"+i;
			speedmap.put("S_"+i,i);

			if( cntmap.containsKey(hash(key)) ) {
				cntmap.put(hash(key),cntmap.get(hash(key))+1);
			}
			else{
				cntmap.put(hash(key),1);
			}
			//System.out.println(hash(hash(key)));
		}

		System.out.println(System.currentTimeMillis());
		speedmap.get("S_1");
		System.out.println(System.currentTimeMillis());
		speedmap.get("S_10");

		//System.out.println(cntmap.toString());
		/*
		Iterator<Map.Entry<Integer,Integer>> itercntmap = cntmap.entrySet().iterator();
		while (iter.hasNext()){
			Map.Entry<Integer,Integer> entry = itercntmap.next();
			if(entry.getValue() > 1)
				System.out.println(entry.toString());
		}
		*/

	}

	static final int hash(Object key) {
		int h;
		return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
	}
}