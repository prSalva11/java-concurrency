package java.concurrency;
/******************************************************************************

                            Online Java Compiler.
                Code, Compile, Run and Debug java program online.
Write your code in this editor and press "Run" button to execute it.

*******************************************************************************/

public class BlockingQueueImpl
{
    private static void log(String s){
        System.out.println(s);
    }
    
    
    
    private class BlockingQueue<T>{
        
        private class Node<Z>{
            Z data;
            Node<Z> next;
            Node(Z d){
                this.data = d;
            }
        }
        
        Node<T> head, trail;
        int N=16;
        int count = 0;
        
        
        BlockingQueue(int n){
            this.N = n;
        }
        
        boolean isEmpty(){
            return this.count <= 0;
        }
        
        int size(){
            return this.count;
        }
        
        int capacity(){
            return this.N;
        }
        
        
        synchronized void put(T data){
            while(this.capacity() == this.size()){
                try{
                    wait();
                }catch(InterruptedException ex){
                    ex.printStackTrace();
                }
            }
            //log("Adding node");
            if(trail == null){
                head = new Node<T>(data);
                trail = head;
            }else{
                Node<T> node = new Node<T>(data);
                trail.next = node;
                trail = node;
            }
            ++this.count;
            notifyAll();
        }
        
        synchronized T take(){
            while(this.isEmpty()){
                 try{
                    wait();
                }catch(InterruptedException ex){
                    ex.printStackTrace();
                }
            }
           // log("Take");
            Node<T> node = head;
            head = head.next;
            --this.count;
            notifyAll();
            return node.data;
        }
    }
    
    public void test(){
        BlockingQueue<Integer> q = new BlockingQueue<Integer>(2);
        new Thread(() ->{
            
           Integer x = q.take();
           log("Taking out element =" + x);
           x = q.take();
           log("Taking out element =" + x);
           x = q.take();
           log("Taking out element =" + x);
           x = q.take();
           log("Taking out element =" + x);
        }).start();
        
        new Thread(() ->{
            q.put(10);
            q.put(20);
            q.put(30);
        }).start();
    }
    
	public static void main(String[] args) {
		
		BlockingQueueImpl m = new BlockingQueueImpl();
		m.test();
	}
}
