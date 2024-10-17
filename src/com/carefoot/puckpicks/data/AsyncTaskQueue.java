package com.carefoot.puckpicks.data;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Allows you to queue asynchronous tasks for execution by a threadpool,
 * which can then be flushed and shutdown
 * 
 * @author jeremycarefoot
 *
 */
public class AsyncTaskQueue {
	
	private int threads;
	private Queue<Runnable> tasks;
	
	public AsyncTaskQueue(int threads) {
		this.threads = threads;
		tasks = new LinkedList<>();
	}
	
	/**
	 * Add a runnable to the queue
	 * @param r Runnable object
	 */
	public void add(Runnable r) {
		tasks.add(r);
	}
	
	/**
	 * Runs all tasks in the current queue in thread pool and clears the queue
	 */
	public void flush() {
		ExecutorService threadPool = Executors.newFixedThreadPool(threads);
		
		Runnable r;
		while ((r = tasks.poll()) != null) {
			threadPool.submit(r);
		}
		
		threadPool.shutdown(); // shuts down threadpool when submitted tasks are complete
	}

}
