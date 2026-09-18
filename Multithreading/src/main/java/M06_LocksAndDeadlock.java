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
    }

    // a synchronized instance method locks this while a static synchronized method locks the Class object
    // synchronization provides mutual exclusion and happens-before visibility for the same monitor
    // thread synchronization coordinates shared memory while process synchronization coordinates separate processes
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
