package myjava.custom.myconcurrency;

public class CustomSemaphoreDriver {
	
	private static void log(String s) {
		System.out.println(s);
	}
	
	private static class SharedData{
		static int count = 0;
	}
	
	/*
	 * This interface is there because we are implementing Semaphore via 2 different implementations
	 * i. PrasenCustomSemaphore (This is developed by @Prasenjit Salva
	 * ii. AbhishekCustomSemaphore (This is the copyright of Abhishek Mittal
	 *     check this link: https://www.javamadesoeasy.com/2015/03/implementation-of-customown-semaphore.html
	 *     
	 * Here we have provided both the implementation just for the understanding & learning purpose.
	 * We don't impose any right on the implementation given by Abhishek
	 */
	interface ISemaphore{
		public void acquire() throws InterruptedException;
		public void release() ;
	}
	
	private class PrasenCustomSemaphore implements ISemaphore{
		int permits;
		PrasenCustomSemaphore(final int permits){
			this.permits = permits;
		}
		
		public synchronized void acquire() throws InterruptedException{
			//wait unitl permits is non zero
			while(this.permits == 0) {
				this.wait();
			}
			--this.permits;
			
		}
		
		public synchronized void release(){
			++this.permits;
			
			//notify() will notify single thread in the queue not all hence don't use notifyAll here
			this.notify();
		}
	}
	
private class AbhishekCustomSemaphore implements ISemaphore{
	    
	    private int permits;
	    
	    /** permits is the initial number of permits available.
	           This value can be negative, in which case releases must occur
	           before any acquires will be granted, permits is number of threads
	           that can access shared resource at a time.
	           If permits is 1, then only one threads that can access shared
	           resource at a time.
	    */
	    public AbhishekCustomSemaphore(int permits) {
	           this.permits=permits;
	    }
	 
	    /**Acquires a permit if one is available and decrements the
	       number of available permits by 1.
	           If no permit is available then the current thread waits
	           until one of the following things happen >
	            >some other thread calls release() method on this semaphore or,
	            >some other thread interrupts the current thread.
	    */
	    public synchronized void acquire() throws InterruptedException {
	           //Acquires a permit, if permits is greater than 0 decrements
	           //the number of available permits by 1.
	           if(permits > 0){
	                  permits--;
	           }
	           //permit is not available wait, when thread
	           //is notified it decrements the permits by 1
	           else{
	                  this.wait();
	                  permits--;
	           }
	    }
	 
	    /** Releases a permit and increases the number of available permits by 1.
	           For releasing lock by calling release() method it’s not mandatory
	           that thread must have acquired permit by calling acquire() method.
	    */
	    public synchronized void release() {
	           //increases the number of available permits by 1.
	           permits++;
	           
	           //If permits are greater than 0, notify waiting threads.
	           if(permits > 0)
	                  this.notify();
	    }
	}
	
	private class Task implements Runnable{
		ISemaphore sem;
        Task(ISemaphore sem){
        	this.sem = sem;
        }
        
		@Override
		public void run() {
			
			try {
				sem.acquire();
				for(int i = 0; i < 5; ++i) {
					SharedData.count++;
					System.out.println(Thread.currentThread().getName() + " " + SharedData.count);
				}
				log("");
				
				Thread.sleep(100);
				
				sem.release();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	
	public void test() {
		/*
		 * Best way to understand semaphore is to give counter for the semaphore as 1
		 * In this case we can see only one thread (since semaphore window is 1) acquiring the shared resource at a time
		 */
		ISemaphore sem = new PrasenCustomSemaphore(1);
		//Sema sem = new SemaphoreCustom(1);
		for(int i = 0 ; i < 10; ++i) {
			Task task = new Task(sem);
			new Thread(task, ("Thread-" + (i + 1))).start();
		}
		
	}
	
	
	public static void main(String args[]) {
		CustomSemaphoreDriver driver = new CustomSemaphoreDriver();
		driver.test();
	}
	
	

	
	
	
}
