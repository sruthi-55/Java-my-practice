import java.util.concurrent.CancellationException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

// InterruptedException signals cooperative cancellation of an interruptible wait
// ExecutionException wraps a submitted task failure while timeout and cancellation describe waiting or task state

public class E09_ThreadAndFutureExceptions {
    public static void main(String[] args) throws InterruptedException, ExecutionException, BrokenBarrierException {
        interruption();
        uncaughtFailure();
        executedFailure();
        submittedFailure();
        timeoutAndCancellation();
        brokenBarrier();
        daemonFailure();
    }

    // propagate InterruptedException when possible or restore the flag and stop work when propagation is impossible
    static void interruption() throws InterruptedException {
        Thread worker = new Thread(() -> {
            Thread.currentThread().interrupt();
            try {
                new CountDownLatch(1).await();
            } catch (InterruptedException exception) {
                System.out.println(Thread.currentThread().isInterrupted());	// false
                Thread.currentThread().interrupt();
                System.out.println(Thread.currentThread().isInterrupted());	// true
                return;
            }
        });
        worker.start();
        worker.join();
    }

    // an uncaught exception terminates its thread and reaches that thread's uncaught exception handler
    static void uncaughtFailure() throws InterruptedException {
        Thread worker = new Thread(() -> { throw new IllegalStateException("worker failed"); });
        worker.setUncaughtExceptionHandler((thread, exception) -> {
            System.out.println(exception.getMessage());	// worker failed
        });
        worker.start();
        worker.join();
        System.out.println("caller continues");	// caller continues
    }

    // submit captures the failure in Future so callers must inspect the result
    static void submittedFailure() throws InterruptedException {
        try (ExecutorService executor = Executors.newSingleThreadExecutor()) {
            Future<Integer> result = executor.submit(() -> { throw new IllegalArgumentException("task failed"); });
            try {
                result.get();
            } catch (ExecutionException exception) {
                System.out.println(exception.getCause().getMessage());	// task failed
            }
            executor.shutdown();
            try {
                executor.submit(() -> 1);
            } catch (RejectedExecutionException exception) {
                System.out.println(exception.getClass().getSimpleName());	// RejectedExecutionException
            }
        }
    }

    // execute reports an uncaught task failure through the worker's handler rather than a Future
    static void executedFailure() throws InterruptedException {
        CountDownLatch handled = new CountDownLatch(1);
        try (ExecutorService executor = Executors.newSingleThreadExecutor(task -> {
            Thread worker = new Thread(task);
            worker.setUncaughtExceptionHandler((thread, exception) -> {
                System.out.println(exception.getMessage());	// execute failed
                handled.countDown();
            });
            return worker;
        })) {
            executor.execute(() -> { throw new IllegalStateException("execute failed"); });
            if (!handled.await(5, TimeUnit.SECONDS)) throw new AssertionError("handler was not called");
        }
    }

    // timing out a wait does not cancel the task; this unstarted task makes the example deterministic
    static void timeoutAndCancellation() throws InterruptedException, ExecutionException {
        FutureTask<Integer> pending = new FutureTask<>(() -> 42);
        try {
            pending.get(0, TimeUnit.MILLISECONDS);
        } catch (TimeoutException exception) {
            System.out.println(exception.getClass().getSimpleName());	// TimeoutException
        }
        System.out.println(pending.isCancelled());	// false
        System.out.println(pending.cancel(true));	// true
        try {
            pending.get();
        } catch (CancellationException exception) {
            System.out.println(exception.getClass().getSimpleName());	// CancellationException
        }
    }

    // cancel(true) requests interruption for a running FutureTask but does not force a task to stop
    // execute does not return a Future and an uncaught Runnable failure reaches the worker's handler
    // Thread.interrupted reads and clears the current flag while isInterrupted does not clear it

    // a timeout breaks the barrier and subsequent await calls fail until it is reset
    static void brokenBarrier() throws InterruptedException, BrokenBarrierException {
        CyclicBarrier barrier = new CyclicBarrier(2);
        try {
            barrier.await(0, TimeUnit.MILLISECONDS);
        } catch (TimeoutException exception) {
            System.out.println(exception.getClass().getSimpleName());	// TimeoutException
        }
        try {
            barrier.await();
        } catch (BrokenBarrierException exception) {
            System.out.println(exception.getClass().getSimpleName());	// BrokenBarrierException
        }
        barrier.reset();
        System.out.println(barrier.isBroken());	// false
    }

    // daemon threads report uncaught failures too; joining here lets the example finish deterministically
    static void daemonFailure() throws InterruptedException {
        Thread daemon = new Thread(() -> { throw new IllegalStateException("daemon failed"); });
        daemon.setDaemon(true);
        daemon.setUncaughtExceptionHandler((thread, exception) -> {
            System.out.println(exception.getMessage());	// daemon failed
        });
        daemon.start();
        daemon.join();
    }

    // an uncaught main-thread failure ends that thread but other non-daemon threads may keep the JVM alive
    // normal JVM shutdown begins when no live non-daemon threads remain
    // daemon status affects JVM lifetime rather than whether uncaught exceptions reach a handler
}
