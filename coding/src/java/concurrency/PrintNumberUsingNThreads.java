package java.concurrency;
/******************************************************************************

                            Online Java Compiler.
                Code, Compile, Run and Debug java program online.
Write your code in this editor and press "Run" button to execute it.

*******************************************************************************/

public class PrintNumberUsingNThreads
{
    private static int index = 1;
    private int max;
    Object lock = new Object();
    
    
    private void setMax(int max){
        this.max = max;
    }
    
    private class Task implements Runnable{
        int id;
        int n;
        
        String name;
        
        Task(int id, int n){
            this.id = id;
            this.n = n;
            this.name = "Thread-" + id;
            
        }
        
        @Override
        public void run(){
            System.out.println("Thread id running=" + this.id);
            
                while(index <= max){
                    
                   synchronized(lock){
                   
                  //  int mod = index % n;
                    while(this.id != index % n){
                        try{
                           lock.wait();
                        }catch(InterruptedException ex){
                            ex.printStackTrace();
                        }
                    }
                
                    System.out.println(Thread.currentThread().getName() + " = " + index);
                    index++;
                    lock.notifyAll();
                    
                   } 
                    
                }
            
        }
        
    }
    
    public void execute(int n, int max){
        
		setMax(max);
		Thread threads[] = new Thread[n];
		
		for(int i = 1; i <= n; ++i){
		    Task task = new Task(i % n, n);
		    threads[i - 1] = new Thread(task, "T-" + i);
		}
	
		
		for(int i = 0; i < n; ++i){
		    threads[i].start();
		}
		
    }
    
	public static void main(String[] args) {
		int n = 3;
		int max = 10;
		
		PrintNumberUsingNThreads m = new PrintNumberUsingNThreads();
		m.execute(n, max);
		
		
	}
}


