import java.util.concurrent.CountDownLatch;

// thread scheduler chooses execution order and priority is only a scheduling hint
// priorities range from Thread.MIN_PRIORITY to Thread.MAX_PRIORITY with NORM_PRIORITY as default
// states are NEW, RUNNABLE, BLOCKED, WAITING, TIMED_WAITING and TERMINATED

public class M05_ThreadLifecycleAndScheduling {
    public static void main(String[] args) throws InterruptedException {
        states();
        // run executes normally on the caller while start executes run on a new thread
        Thread execution = new Thread(() -> System.out.println(Thread.currentThread().getName()), "worker");	// main, then worker
        execution.run();
        execution.start();
        execution.join();

        // priority values can be assigned but do not guarantee execution order
        Thread lowPriority = new Thread(() -> { }, "low-priority");
        Thread highPriority = new Thread(() -> { }, "high-priority");
        lowPriority.setPriority(Thread.MIN_PRIORITY);
        highPriority.setPriority(Thread.MAX_PRIORITY);
        lowPriority.start();
        highPriority.start();
        lowPriority.join();
        highPriority.join();
        System.out.println(lowPriority.getPriority());	// 1
        System.out.println(highPriority.getPriority());	// 10
        System.out.println(Thread.currentThread().getName() + " " + Thread.NORM_PRIORITY);	// main 5

        // starting the same Thread instance twice is illegal
        try {
            execution.start();
        } catch (IllegalThreadStateException exception) {
            System.out.println(exception.getClass().getSimpleName());	// IllegalThreadStateException
        }

        // child thread inherits daemon status and priority from its creating thread
        Thread parent = new Thread(() -> {
            Thread child = new Thread(() -> { });
            System.out.println(child.isDaemon() + " " + child.getPriority());	// true 7
        });
        parent.setDaemon(true);
        parent.setPriority(7);
        parent.start();
        parent.join();
    }

    // state polling is used only for this bounded diagnostic demonstration and not for task coordination
    static void states() throws InterruptedException {
        Thread waiting = new Thread(() -> {
            System.out.println(Thread.currentThread().getState());	// RUNNABLE
            try {
                new CountDownLatch(1).await();
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            }
        }, "state-worker");
        System.out.println(waiting.getState());	// NEW
        System.out.println(waiting.threadId() > 0);	// true
        waiting.start();
        try {
            awaitState(waiting, Thread.State.WAITING);
            waiting.join(1);
            System.out.println(waiting.isAlive());	// true
        } finally {
            waiting.interrupt();
            waiting.join();
        }
        System.out.println(waiting.getState());	// TERMINATED

        Object monitor = new Object();
        Thread blocked = new Thread(() -> { synchronized (monitor) { } });
        synchronized (monitor) {
            blocked.start();
            awaitState(blocked, Thread.State.BLOCKED);
        }
        blocked.join();

        Thread sleeping = new Thread(() -> {
            try {
                Thread.sleep(60_000);
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            }
        });
        sleeping.start();
        try {
            awaitState(sleeping, Thread.State.TIMED_WAITING);
        } finally {
            sleeping.interrupt();
            sleeping.join();
        }

        // sleep retains owned monitors and yield is only a scheduler hint
        synchronized (monitor) {
            Thread.sleep(1);
            Thread.yield();
            System.out.println(Thread.holdsLock(monitor));	// true
        }
    }

    static void awaitState(Thread thread, Thread.State expected) throws InterruptedException {
        long deadline = System.nanoTime() + 5_000_000_000L;
        while (thread.getState() != expected && System.nanoTime() < deadline) Thread.sleep(1);
        if (thread.getState() != expected) throw new AssertionError("state not observed: " + expected);
        System.out.println(expected);	// WAITING, BLOCKED or TIMED_WAITING
    }

    // the JVM starts the main thread and runtime daemon threads; child threads inherit their parent's priority
    // priorities range from 1 to 10 but scheduling order remains JVM and operating-system dependent
    // threads share process memory while each thread has its own execution stack
    // RUNNABLE includes execution and readiness for CPU time because Java has no separate RUNNING state
    // timed join can return while its target is still alive and sleep never proves another task completed
}
