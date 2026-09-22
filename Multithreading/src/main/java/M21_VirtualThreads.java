import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;

// virtual threads are lightweight JVM-managed threads scheduled on platform carrier threads
// Java 21 virtual threads improve throughput for many blocking tasks rather than accelerating CPU computation

public class M21_VirtualThreads {
    public static void main(String[] args) throws Exception {
        Thread virtual = Thread.ofVirtual().name("virtual-worker").unstarted(() -> {
            System.out.println(Thread.currentThread().isVirtual());	// true
        });
        virtual.start();
        virtual.join();
        System.out.println(virtual.isDaemon());	// true
        Thread direct = Thread.startVirtualThread(() -> { });
        direct.join();

        // create one virtual thread per task but separately bound access to a scarce resource
        Semaphore connections = new Semaphore(2);
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            Callable<Integer> request = () -> {
                connections.acquire();
                try {
                    Thread.sleep(1);
                    return 42;
                } finally {
                    connections.release();
                }
            };
            for (Future<Integer> result : executor.invokeAll(List.of(request, request, request))) {
                System.out.println(result.get());	// 42 three times
            }
        }
        System.out.println(connections.availablePermits());	// 2
    }

    // sleeping here simulates blocking work and is not used as proof that another task completed
    // do not pool virtual threads; use semaphores or resource pools to limit scarce downstream capacity
    // in Java 21 blocking inside synchronized or native code can pin a virtual thread to its carrier
    // avoid long blocking operations under synchronized on Java 21 when scalability matters
    // many virtual threads each retaining large ThreadLocal values can consume substantial memory
    // virtual threads obey the same memory model and synchronization rules as platform threads
}
