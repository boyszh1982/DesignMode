package com.nameless.base.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * 采用ForkJoin实现停车场剩余车位显示。
 * 需求：
 * 100辆车入库，所有门都占用上。
 * 
 * Fork Join 适合分线程计算，然后又在父线程内汇总的业务场景。
 * 就像oracle的并行提示
 * 
 * Fork Join 是通过函数迭代创建线程。
 * Executor是通过多次submit创建线程。
 * @author Administrator
 *
 */
public class CarParkForkJoinImpl {

	public static void main(String[] args) throws InterruptedException {
		
		//初始化1000台车
		final List<String> carList = new ArrayList<String>();
		for(int i=0;i<10;i++) {
			carList.add("辽A:"+i);
		}
		
		CarParkDoorkeeper keeper = new CarParkDoorkeeper(carList);
		
		ForkJoinPool pool = new ForkJoinPool(
                8, ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, true);
		
		pool.submit(keeper);
		
		pool.awaitTermination(10, TimeUnit.SECONDS);
		
		pool.shutdown();
	}
}

class CarParkDoorkeeper extends RecursiveTask<Integer> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -261343401927250653L;

	private List<String> carList ; //park外剩余车辆
	
	public CarParkDoorkeeper(List<String> carList){
		this.carList = carList ;
	}
	
	@Override
	protected Integer compute() {
		//不加锁，ForkJoin会出现数据错误。与普通的线程池一样。
		//synchronized (carList) {
			
		
			//System.out.println(Thread.currentThread().getId() + " is running .");
			String tname = Thread.currentThread().getName();
			//当外边车辆全部进入广场
			if(carList.isEmpty()) {
				//System.out.println("["+tname+"]" + "no car outof park !");
			}
			else {
				//当park外的车还在排队，先进入排在最前边的车。
				System.out.println("["+tname+"]" + carList.get(0) + " into park !");
				carList.remove(0);
				
				/*
				 * 此处如果只有1个子任务，相当与串行。
				 * 若2个子任务会达到,1-2-4-8 ...效果
				 * 若3个子任务会达到,1-3-9-28 ...效果
				 */
				//进车的过程在启用大于1个子任务，不然没有意义。不会出现细胞分裂线程数增加效果
				CarParkDoorkeeper subkeeperA = new CarParkDoorkeeper(carList);
				CarParkDoorkeeper subkeeperB = new CarParkDoorkeeper(carList);
				subkeeperA.fork();
				subkeeperB.fork();
				//加上.join()处于无线等待状态 ? 
				System.out.println("A carList.size() = " + subkeeperA.join());
				System.out.println("B carList.size() = " + subkeeperB.join());
				//排队最前面的进入广场，在队列中移除。
			}
		//}
		return carList.size();
	}
	
}
