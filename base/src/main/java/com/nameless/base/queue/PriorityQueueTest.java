package com.nameless.base.queue;

import java.util.Comparator;
import java.util.PriorityQueue;

public class PriorityQueueTest {

	public static void main(String[] args) {

		PriorityQueue<Integer> pq = new PriorityQueue<Integer>();
		// 下面代码依次向pq中加入四个元素
		pq.offer(6);
		pq.offer(-3);
		pq.offer(9);
		pq.offer(0);
		
		//[-3, 0, 9, 6] 从小到大排列
		System.out.println(pq);
		
		//从队列中取出,不删除
		System.out.println(pq.peek());
		//从队列中取出，删除。
		System.out.println(pq.poll());
		
		System.out.println(pq);
		
		PriorityQueue<Integer> pq2 = new PriorityQueue<Integer>(new Comparator<Integer>(){
			@Override
			public int compare(Integer o1, Integer o2) {
				// TODO Auto-generated method stub
				return o2 - o1;
			}
		});
		
		pq2.addAll(pq);
		//从大到小排列
		System.out.println(pq2);
		
		PriorityQueue<String> pqs = new PriorityQueue<>();
		String e = null;
		pqs.offer(e);
		System.out.println(pqs);

	}
}
