import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;

// interruption requests cooperative cancellation and never forcibly stops arbitrary code
// daemon threads do not keep the JVM alive after all non-daemon threads finish

public class M09_InterruptionAndDaemon {
    public static void main(String[] args) throws Exception {
        CountDownLatch entered = new CountDownLatch(1);
        Thread worker = new Thread(() -> {
            entered.countDown();
            try {
                new CountDownLatch(1).await();
            } catch (InterruptedException exception) {
                System.out.println(Thread.currentThread().isInterrupted());	// false
                Thread.currentThread().interrupt();
                System.out.println(Thread.currentThread().isInterrupted());	// true
            }
        });
        worker.start();
        entered.await();
        worker.interrupt();
        worker.join();

        // computation must check the flag explicitly because it has no interruptible blocking call
        CountDownLatch computing = new CountDownLatch(1);
        Thread computation = new Thread(() -> {
            computing.countDown();
            while (!Thread.currentThread().isInterrupted()) Thread.onSpinWait();
        });
        computation.start();
        try {
            computing.await();
        } finally {
            computation.interrupt();
            computation.join();
        }
        System.out.println(computation.isAlive());	// false

        // interrupted reads and clears the caller's flag while isInterrupted only reads it
        Thread.currentThread().interrupt();
        System.out.println(Thread.interrupted());	// true
        System.out.println(Thread.currentThread().isInterrupted());	// false

        Thread daemon = new Thread(() -> { throw new IllegalStateException("worker failed"); });
        daemon.setDaemon(true);
        daemon.setUncaughtExceptionHandler((thread, failure) -> {
            System.out.println(failure.getMessage());	// worker failed
        });
        daemon.start();
        daemon.join();
        System.out.println(daemon.isDaemon());	// true

        // an unstarted FutureTask makes timeout and cancellation behavior deterministic
        FutureTask<Integer> pending = new FutureTask<>(() -> 42);
        try {
            pending.get(0, TimeUnit.SECONDS);
        } catch (TimeoutException exception) {
            System.out.println(pending.isCancelled());	// false
        }
        System.out.println(pending.cancel(false));	// true
        try {
            pending.get();
        } catch (CancellationException exception) {
            System.out.println("cancelled");	// cancelled
        }

        FutureTask<Integer> failed = new FutureTask<>(() -> { throw new IllegalArgumentException("task failed"); });
        failed.run();
        try {
            failed.get();
        } catch (ExecutionException exception) {
            System.out.println(exception.getCause().getMessage());	// task failed
        }
    }

    // cancel(true) may interrupt running work while cancel(false) does not request interruption
    // propagate InterruptedException or restore the flag and stop work when propagation is impossible
    // CPU loops must check cancellation and blocking APIs have API-specific interruption behavior
    // an uncaught main failure ends main but other non-daemon threads can keep the JVM alive
    // daemon cleanup is not guaranteed and daemon status must be configured before starting
    // stop can expose inconsistent state and suspend can freeze a lock owner; never use these legacy APIs
}
