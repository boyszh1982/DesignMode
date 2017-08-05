package com.nameless.base.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ArrayListVsLinkedList {

	public static void main(String[] args) {
		Integer[] arrayValue = new Integer[20000];
		Random random = new Random();
		for(int i=0;i<20000;i++){
			arrayValue[i] = random.nextInt(100) + 1;
		}
		List<Integer> values = Arrays.asList(arrayValue);
		
		/*
		ArrayList search :10
		LinkedList search :1746
		ArrayList add :161
		LinkedList add :0
		 */
		
		System.out.println("ArrayList search :"+Before.searchListTime(new ArrayList<Integer>(values)));
		System.out.println("LinkedList search :"+Before.searchListTime(new LinkedList<Integer>(values)));
		System.out.println("ArrayList add :"+Before.addListFromHeaderTime(new ArrayList<Object>(values)));
		System.out.println("LinkedList add :"+Before.addListFromHeaderTime(new LinkedList<Object>(values)));
	}
}

class Before {
	
	//《JAVA代码与架构之完美优化-实战经典》
	//测试List插入时间
	public static long addListFromHeaderTime(List<Object> list){
		long startTime = System.currentTimeMillis();
		Object obj = new Object();
		for(int i=0;i<20000;i++){
			list.add(0,obj);
		}
		long endTime = System.currentTimeMillis();
		return endTime - startTime;
	}
	
	//测试List查询时间
	public static long searchListTime(List<Integer> list){
		long startTime = System.currentTimeMillis();
		for(int i=0;i<20000;i++){
			//二分法查询,查询Integer列表
			Collections.binarySearch(list, list.get(i));
		}
		long endTime = System.currentTimeMillis();
		return endTime - startTime;
	}
	
	
	
}
