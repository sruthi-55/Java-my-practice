// synchronized block locks only a critical section while synchronized method locks the complete method body
// static synchronization locks the Class object and protects state shared across every instance
// deadlock occurs when threads wait cyclically for locks and consistent lock ordering helps prevent it

public class M06_LocksAndDeadlock {
    public static void main(String[] args) {
        LockedCounter counter = new LockedCounter();
        counter.incrementBlock();
        counter.incrementMethod();
        LockedCounter.incrementStatic();
        System.out.println(counter.value());	// 2

        // every caller takes the same locks in the same order to prevent a lock-order cycle
        Object first = new Object();
        Object second = new Object();
        synchronized (first) {
            synchronized (second) {
                System.out.println(Thread.holdsLock(first) && Thread.holdsLock(second));	// true
            }
        }

        // throwing from a synchronized block releases that monitor automatically
        try {
            synchronized (first) {
                throw new IllegalStateException("failed");
            }
        } catch (IllegalStateException exception) {
            System.out.println(Thread.holdsLock(first));	// false
        }
    }

    // a synchronized instance method locks this while a static synchronized method locks the Class object
    // synchronization provides mutual exclusion and happens-before visibility for the same monitor
    // thread synchronization coordinates shared memory while process synchronization coordinates separate processes
    // deadlock requires mutual exclusion, hold-and-wait, no forced resource preemption and circular waiting
    // livelock repeatedly changes state without progress while starvation denies a task access indefinitely
    // contention means competing for a resource and does not by itself imply deadlock
    // different lock objects cannot protect one shared invariant and long critical sections increase contention
    // avoid callbacks and blocking external operations while holding a lock
    // M22 demonstrates a real detectable deadlock with interruptible recovery
}

class LockedCounter {
    private static int total;
    private int value;

    void incrementBlock() {
        synchronized (this) {
            value++;
        }
    }

    synchronized void incrementMethod() {
        value++;
    }

    static synchronized void incrementStatic() {
        total++;
    }

    int value() {
        return value;
    }
}
