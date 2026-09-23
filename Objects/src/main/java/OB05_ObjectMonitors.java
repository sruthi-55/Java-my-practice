// each object can act as a monitor used by synchronized, wait, notify and notifyAll
// monitor methods are final and require ownership of the exact receiving object's monitor
// wait releases that monitor while waiting and reacquires it before returning or throwing InterruptedException

public class OB05_ObjectMonitors {
    public static void main(String[] args) throws InterruptedException {
        Object monitor = new Object();

        // ownership is required even when there are no waiters to notify
        try {
            monitor.notify();
        } catch (IllegalMonitorStateException exception) {
            System.out.println(exception.getClass().getSimpleName());	// IllegalMonitorStateException
        }

        // timed waits need no worker thread and may wake spuriously so exact elapsed time is not asserted
        synchronized (monitor) {
            System.out.println(Thread.holdsLock(monitor));	// true
            monitor.wait(1);
            monitor.wait(1, 1);
            monitor.notify();
            monitor.notifyAll();
            System.out.println(Thread.holdsLock(monitor));	// true
        }
        System.out.println(Thread.holdsLock(monitor));	// false

        // an already interrupted thread throws from wait and clears its interrupt status
        synchronized (monitor) {
            Thread.currentThread().interrupt();
            try {
                monitor.wait();
            } catch (InterruptedException exception) {
                System.out.println(Thread.currentThread().isInterrupted());	// false
                System.out.println(Thread.holdsLock(monitor));	// true
            }
        }
    }

    // wait() and wait(0) have no timeout and a condition must be rechecked in a while loop
    // notify chooses one arbitrary waiter while notifyAll lets all waiters compete after lock release
    // notification is not stored for future waiters so guard a persistent condition with the same monitor
    // wait releases only the receiver's monitor while Thread.sleep releases no held monitors
    // static synchronized methods lock the Class object while instance synchronized methods lock this
    // use a private final lock object rather than pooled strings, boxed values or publicly accessible objects
    // see Multithreading/M07_WaitNotify for guarded conditions and actual producer-consumer coordination
}
