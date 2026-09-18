// thread scheduler chooses execution order and priority is only a scheduling hint
// priorities range from Thread.MIN_PRIORITY to Thread.MAX_PRIORITY with NORM_PRIORITY as default

public class M05_ThreadLifecycleAndScheduling {
    public static void main(String[] args) throws InterruptedException {
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

    // the JVM starts the main thread and runtime daemon threads; child threads inherit their parent's priority
    // priorities range from 1 to 10 but scheduling order remains JVM and operating-system dependent
    // threads share process memory while each thread has its own execution stack
}
