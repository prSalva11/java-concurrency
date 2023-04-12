# Terminology

### Check-then-Act: 
This is a type of race condition where a potentially stale observation is used to make a decision on what to do next.

### Intrinsic Lock: 
Every java object can implicitly act as a lock for purposes of synchronization. These built-in locks are called intrinsic locks or monitor locks.

### synchronized block:
Synchronized block is a built-in locking mechanism for enforcing atomicity. It has two parts: one is a reference to the object which serves as a lock and other is the block of code to be guarded by this lock.

### Reentrancy:
Reentrancy means that locks are acquired on a per-thread basis rather than per-invocation basis. Reentrancy is implemented by associating with each lock an acquisition count and an owning thread.
