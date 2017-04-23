package com.nameless.base.concurrent;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Demo of {@code ForkJoinPool} behaviour in async and non-async mode.
 */
public class ForkJoinAsyncMode {
	
	public static void main(String[] args) {
		// Set the asyncMode argument below to true or false as desired:
		ForkJoinPool pool = new ForkJoinPool(4, ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, true);

		pool.invoke(new RecursiveRangeAction(0, 200));
		pool.awaitQuiescence(2L, TimeUnit.SECONDS);
	}

	/**
	 * A {@code ForkJoinTask} that prints a range if the range is smaller than a
	 * certain threshold; otherwise halves the range and proceeds recursively.
	 * Every recursive invocation also forks off a task that is never joined.
	 */
	private static class RecursiveRangeAction extends RecursiveAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = -1240641679962888165L;

		private static final AtomicInteger ASYNC_TASK_ID = new AtomicInteger();

		private final int start;
		private final int end;

		RecursiveRangeAction(int start, int end) {
			this.start = start;
			this.end = end;
		}

		@Override
		protected void compute() {
			if (end - start < 10) {
				System.out.format("%s range [%d-%d] done%n", Thread.currentThread().getName(), start, end);
			} else {
				int mid = (start + end) >>> 1;
				int id = ASYNC_TASK_ID.incrementAndGet();

				System.out.format("%1$s [%2$d-%3$d] -< [%3$d-%4$d], fork async task %5$d%n",
						Thread.currentThread().getName(), start, mid, end, id);

				// Fork off additional asynchronous task that is never joined.
				ForkJoinTask.adapt(() -> {
					System.out.format("%s async task %d done%n", Thread.currentThread().getName(), id);
				}).fork();

				invokeAll(new RecursiveRangeAction(start, mid), new RecursiveRangeAction(mid, end));
			}
		}
	}
}