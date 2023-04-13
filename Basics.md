# Basic Information


 Stateless objects are always thread-safe. 
    Example: A method computes based on all local variables is thread-safe
    
 Thread-safe classes encapsulate any needed synchronization so that clients need not provide their own.

 Update related state variables in a single atomic operation to preserve state consistency. (Check out my Video for examples)

 When using locks to coordinate access to a variable, the same lock must be used wherever that variable is accessed.
 
 Synchronization has another significant and subtle , aspect: memory visibility
 we want not only to prevent one thread from modifying the state of an object when another is using it, but also to ensure that when a thread modifies the state of the object, other threads can actually see the changes that were made.
 
 
