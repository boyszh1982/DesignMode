package com.nameless.base.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CarParkExecutorServiceImpl {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		//初始化1000台车
		final List<String> carList = new ArrayList<String>();
		for(int i=0;i<50000;i++) {
			carList.add("辽A:"+i);
		}

		int threadCnt = carList.size();

		CountDownLatch latch = new CountDownLatch(threadCnt);
		ExecutorService tpool = Executors.newFixedThreadPool(threadCnt);
		List<Future<String>> list = new ArrayList<Future<String>>();
		for(int i=0;i<threadCnt;i++){
			Future<String> f = (Future<String>) tpool.submit(new CarParkDoorkeeper2(latch,carList));
			list.add(f);
		}
		latch.await(10, TimeUnit.SECONDS);
		//tpool.awaitTermination(10, TimeUnit.SECONDS);
		tpool.shutdown();
		for(Future<String> f : list){
			try {
				System.out.println(f.get(1,TimeUnit.MILLISECONDS));
			} catch (TimeoutException e) {
				e.printStackTrace();
			}
		}
	}
}

class CarParkDoorkeeper2 implements Runnable {

	private List<String> carList ; //park外剩余车辆
	private CountDownLatch latch ; //

	public CarParkDoorkeeper2(CountDownLatch latch , List<String> carList){
		this.latch = latch ;
		this.carList = carList ;
	}

	public void run() {
		synchronized (carList) {
			String tname = Thread.currentThread().getName();
			if(carList.isEmpty()) {
				;
			}
			else{
				try {
					Thread.sleep(2L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//当park外的车还在排队，先进入排在最前边的车。
				System.out.println("["+tname+"]" + carList.get(0) + " into park !");
				carList.remove(0);

			}
			latch.countDown();
		}
	}

}
