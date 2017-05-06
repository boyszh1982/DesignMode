package com.nameless.base.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
/*
ForkJoinPool使用的工作窃取的方式能够在最大方式上充分利用CPU的资源，一般流程是fork分解，join结合。本质是将一个任务分解成多个子任务，每个子任务用单独的线程去处理，主要几个常用方法有 
fork( ForkJoinTask)	异步执行一个线程
join( ForkJoinTask)	等待任务完成并返回执行结果
execute( ForkJoinTask)	执行不带返回值的任务
submit( ForkJoinTask)	执行带返回值的任务
invoke( ForkJoinTask)	执行指定的任务，等待完成，返回结果。
invokeAll(ForkJoinTask)	执行指定的任务，等待完成，返回结果。
shutdown()	执行此方法之后，ForkJoinPool 不再接受新的任务，但是已经提交的任务可以继续执行。如果希望立刻停止所有的任务，可以尝试 shutdownNow() 方法。
awaitTermination(int, TimeUnit.SECONDS)	阻塞当前线程直到 ForkJoinPool 中所有的任务都执行结束。
compute（）	执行任务的具体方法
Runtime.getRuntime().availableProcessors()	获取CPU个数的方法
*/
/**
 * 计算 1-1000 (start - end)所有数的加和
 * 1+2+3+...+1000 = ? 
 * @author Administrator
 *
 */
public class ForkJoinHello {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		
		ForkJoinPool pool = new ForkJoinPool();
		//pool.invoke(new SumTask(1,1000));
		//pool.awaitQuiescence(2, TimeUnit.MINUTES);
		pool.submit(new SumTask(1,10) );
		pool.awaitTermination(2, TimeUnit.MINUTES);
		pool.shutdown();

	}
	
	private static class SumTask extends RecursiveTask<Long> {
		private static final long serialVersionUID = -7133776197236925256L;
		private static AtomicInteger ASYNC_TASK_ID = new AtomicInteger();
		
		private static long start , end ;
		SumTask(long start, long end) {
			this.start = start ;
			this.end = end ;
		}
		
		@Override
		protected Long compute() {
			
			String tname = Thread.currentThread().getName();
			if( end - start == 0 ) {
				// 两数相等
				System.out.println("["+tname+"]" + start);
				return start ;
			}
			else if( end - start == 1 ) {
				// 两数临近
				System.out.println("["+tname+"]" + ( start + end ) );
				return start + end ;
			}
			else {
				SumTask st1 = new SumTask(start+1 , end-1);
				SumTask st2 = new SumTask(start+2 , end-2);
				
				st1.fork();
				st2.fork();
				
				Long a = st1.join();
				Long b = st2.join();
				Long s = a + b;
				
				//System.out.println("["+tname+"]" + (start+1) + " + " + (end-1) + " = " + ( a ) );
				//System.out.println("["+tname+"]" + (start+2) + " + " + (end-2) + " = " + ( b ) );
				//System.out.println("["+tname+"]" + a + " + " + b + " = " + ( s ) );
				return s;
			}
			
		}
		
	}
	
}



