package myjava.custom.myconcurrency;

/******************************************************************************

                            Online Java Compiler.
                Code, Compile, Run and Debug java program online.
Write your code in this editor and press "Run" button to execute it.

*******************************************************************************/

public class CustomMapImpl
{
    private static void log(String s){
        System.out.println(s);
    }
    
    private class Node<K, V>{
        K key;
        V value;
        Node<K,V> next;
        Node(K key, V value){
            this.key = key;
            this.value = value;
        }
    }
    
    private class CustomMap<K, V>{
        Node<K, V> table[];
        int capacity;
        
        CustomMap(int n){
            this.capacity = n;
            table = new Node[this.capacity];
        }
        
        private int getIndex(K key){
            int hash = key.hashCode();
            return hash % this.capacity;
        }
        
        public void put(K key, V value){
            int index = getIndex(key);
            log("Putting " + key + " at buckedIndex = " + index);
            if(table[index] == null){
                Node<K, V> node = new Node<K, V>(key, value);
                table[index] = node;
            }else{
                Node<K, V> cur = table[index];
                Node<K, V> last = null;
                while(cur != null){
                    if(cur.key.equals(key))
                    {
                        cur.value = value;
                        return;
                    }
                    last = cur;
                    cur = cur.next;
                }
                last.next = new Node<>(key, value);
            }
        }
        
        public V get(K key){
            int index = getIndex(key);
            if(table[index] == null){
                return null;
            }
            else{
                Node<K, V> cur = table[index];
                V value = null;
                while(cur != null){
                    if(cur.key.equals(key)){
                        value = cur.value;
                        break;
                    }
                    cur = cur.next;
                }
                return value;
            }
        }
    }
    
    
    public void test(){
        CustomMap<Integer, String> map = new CustomMap<Integer, String>(2);
        map.put(1, "Hello");
        map.put(2, "Wello");
        map.put(3, "Zello");
        map.put(4, "tello");
        map.put(5, "kello");
        
        log(map.get(1));
        log(map.get(2));
        log(map.get(3));
        log(map.get(4));
        log(map.get(5));
    }
    
	public static void main(String[] args) {
		
		CustomMapImpl m = new CustomMapImpl();
		m.test();
	}
}
