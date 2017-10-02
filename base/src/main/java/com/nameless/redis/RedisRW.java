package com.nameless.redis;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import redis.clients.jedis.Jedis;

public class RedisRW {

	public static Jedis getJedisConnection() {
		Jedis jedis = new Jedis("124.126.15.61", 6379, 200000);
		jedis.auth("123456");
		return jedis ;
	}
	
	public static void main(String[] args) {
		//ExecutorService pool = Executors.newFixedThreadPool(20);
		
		String k = new SimpleDateFormat("yyyy-MM-dd#HH:mm").format(new Date());
		
		int poolsize = 20;
		List<Jedis> jedisList = new ArrayList<>();
		for(int i=0;i<poolsize;i++) {
			jedisList.add(getJedisConnection());
		}
		CountDownLatch latch = new CountDownLatch(poolsize);
		
		List<RedisInputDto> dtoList = new ArrayList<>();
		for(int i=0;i<10;i++) {
			dtoList.add(new RedisInputDto("dto"+i,UUID.randomUUID().toString()) );
		}
		
		RedisInputDto dto = null;
		for(Jedis jedis:jedisList) {
			dto = new RedisInputDto();
			dto.setName(k+"."+jedisList.indexOf(jedis));
			dto.setDtos(dtoList);
			Thread t1 = new Thread(new RWThread(latch,jedis,dto));
			t1.start();
		}
		
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}


class RWThread implements Runnable {
	private CountDownLatch latch;
	private Jedis jedis;
	private RedisInputDto dto;
	
	public RWThread(CountDownLatch latch,Jedis jedis,RedisInputDto dto) {
		this.latch = latch;
		this.jedis = jedis;
		this.dto = dto;
	}
	
	@Override
	public void run() {
		try {
			long t1 = System.currentTimeMillis();
			byte[] bytes = SerializeUtil.serialize(dto);
			long t2 = System.currentTimeMillis();
			System.out.println(String.format("%s trans to byte[] cost %s", dto.getName(), t2-t1));
			jedis.set(dto.getName().getBytes(),bytes);
			long t3 = System.currentTimeMillis();
			System.out.println(String.format("%s set to redis cost %s", dto.getName(), t3-t2));
			byte[] result = jedis.get(dto.getName().getBytes());
			long t4 = System.currentTimeMillis();
			System.out.println(String.format("%s get from redis cost %s", dto.getName(), t4-t3));
			RedisInputDto resultDto = SerializeUtil.unserialize(result, RedisInputDto.class);
			long t5 = System.currentTimeMillis();
			System.out.println(String.format("%s trnas to Object cost %s", dto.getName(), t5-t4));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			latch.countDown();
		}
	}
	
}
