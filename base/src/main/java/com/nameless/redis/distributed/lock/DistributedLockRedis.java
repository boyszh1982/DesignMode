package com.nameless.redis.distributed.lock;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import redis.clients.jedis.Jedis;

public class DistributedLockRedis {

	public static Map<String,Integer> store = new HashMap<String,Integer>();
	
	public static void main(String[] args) {
		
		store.put("cat", 100);
		
		String lockkey = "stanley.lock.";
		
		for(int i=0;i<30;i++) {
			new OperateThread(store).start();
		}
		
	}
	
	public static void keys(Jedis jedis , String lockkeyPrefix) {
		Set<String> keys = jedis.keys(lockkeyPrefix);
		Iterator<String> iter = keys.iterator();
		while(iter.hasNext()) {
			System.out.println(iter.next());
		}
	}
}


class OperateThread extends Thread {

	private Map<String,Integer> store ;
	private String lockkey = "stanley.lock.";
	private Jedis jedis ;
	
	public OperateThread(Map<String,Integer> store) {
		jedis = new Jedis("192.168.100.60", 6379, 200000);
		jedis.auth("123456");
		this.store = store;
	}
	
	@Override
	public void run() {
		
		//non lock 
		//store.put("cat", store.get("cat") -1);
		//System.out.println("store:"+store.get("cat"));
		
		//sync lock
//		synchronized (store) {
//			store.put("cat", store.get("cat") -1);
//			System.out.println("store:"+store.get("cat"));
//		}
		
		//get lock success .
		if(jedis.setnx(lockkey, String.valueOf(System.currentTimeMillis() + 200L)  ) == 1) {
			jedis.expire(lockkey, 1);
			
			jedis.del(lockkey);
		}
		else {
			
			System.out.println("Get lock failed !");
		}
	}
	
	public void getCat() {
		store.put("cat", store.get("cat") -1);
		System.out.println(System.currentTimeMillis() +" store:"+store.get("cat"));
	}
	
}

