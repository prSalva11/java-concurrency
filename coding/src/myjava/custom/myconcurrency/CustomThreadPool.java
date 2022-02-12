package myjava.custom.myconcurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CustomThreadPool {
	
	private static void log(String t) {
		System.out.println(t);
	}

	
	private class ThreadPool{
		private List<Runnable> workers;
		private BlockingQueue<Runnable> blockingQueue;
		private int threadsCount;
		
		ThreadPool(int n){
			this.threadsCount = n;
			blockingQueue = new LinkedBlockingQueue<Runnable>(10);
			workers = new ArrayList<>(this.threadsCount);
			for(int i = 0 ; i < this.threadsCount; ++i) {
				workers.add(new ThreadPoolWorker(this.blockingQueue));
			}
			for(int i = 0; i < this.threadsCount; ++i) {
				Runnable worker = workers.get(i);
				new Thread(worker).start();
			}
		}
		
		public boolean submit(Runnable task) {
			try {
				this.blockingQueue.put(task);
			} catch (InterruptedException e) {
				return false;
			}
			return true;
		}
		
		public void shutDown() {
			for(int i = 0; i < this.threadsCount; ++i) {
				ThreadPoolWorker worker = (ThreadPoolWorker)workers.get(i);
				worker.cancel();
			}
		}
	}
	
	private class ThreadPoolWorker implements Runnable{
		
		private boolean isRunning;
		private BlockingQueue<Runnable> blockingQueue;
		
		ThreadPoolWorker(BlockingQueue<Runnable> q){
			this.isRunning = true;
			this.blockingQueue = q;
		}

		public void cancel() {
			this.isRunning = false;
		}
		
		@Override
		public void run() {
			while(isRunning) {
				try {
					Runnable task = blockingQueue.take();
					log("Executing worker thread: " + Thread.currentThread().getName());
				    task.run();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	
	/*
	 * Task which i want to submit to the threadpool
	 */
	
	private class Task implements Runnable{
		
		int taskId;
		Task(int taskId){
			this.taskId = taskId;
		}

		@Override
		public void run() {
			log("Task executed: " + this.taskId);
			
		}
	}
	
	public void test() {
		ThreadPool pool = new ThreadPool(5);
		
		/*
		 * Submitting tasks to the thread pool
		 */
		
		for(int i = 0; i < 20; ++i) {
			Runnable task = new Task(i);
			pool.submit(task);
		}
		
		pool.shutDown();
	}
	
	
	public static void main(String[] args) {
		CustomThreadPool driver = new CustomThreadPool();
		driver.test();
        
	}

}
