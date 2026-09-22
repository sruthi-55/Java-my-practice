import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

// Executor accepts tasks and ExecutorService adds results and lifecycle management
// ThreadPoolExecutor combines worker limits, a task queue, a thread factory and a rejection policy

public class M13_ThreadPools {
    public static void main(String[] args) throws Exception {
        for (RejectedExecutionHandler policy : List.of(new ThreadPoolExecutor.AbortPolicy(),
                new ThreadPoolExecutor.CallerRunsPolicy(), new ThreadPoolExecutor.DiscardPolicy(),
                new ThreadPoolExecutor.DiscardOldestPolicy())) {
            rejection(policy);
        }

        // single-thread pools serialize tasks and cached pools create or reuse idle workers
        try (ExecutorService single = Executors.newSingleThreadExecutor();
             ExecutorService cached = Executors.newCachedThreadPool()) {
            System.out.println(single.submit(() -> 1).get());	// 1
            System.out.println(cached.submit(() -> 2).get());	// 2

            // a task cannot wait indefinitely for a child queued behind it in the same single-worker pool
            Future<String> outer = single.submit(() -> {
                Future<Integer> child = single.submit(() -> 42);
                try {
                    child.get(0, TimeUnit.MILLISECONDS);
                    return "unexpected";
                } catch (TimeoutException exception) {
                    child.cancel(false);
                    return "starvation avoided";
                }
            });
            System.out.println(outer.get());	// starvation avoided
        }
        immediateShutdown();
    }

    // occupy the worker and its queue before submitting a third task to exercise saturation policies
    static void rejection(RejectedExecutionHandler policy) throws InterruptedException {
        CountDownLatch release = new CountDownLatch(1);
        AtomicInteger total = new AtomicInteger();
        ThreadPoolExecutor pool = new ThreadPoolExecutor(1, 1, 10, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1), task -> new Thread(task, "bounded-worker"), policy);
        try {
            pool.execute(() -> {
                try {
                    release.await();
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                }
            });
            pool.execute(() -> total.addAndGet(1));
            try {
                pool.execute(() -> total.addAndGet(10));
            } catch (RejectedExecutionException exception) {
                System.out.println("rejected");	// rejected for AbortPolicy
            }
        } finally {
            release.countDown();
            pool.shutdown();
            if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                pool.shutdownNow();
                throw new AssertionError("pool did not stop");
            }
        }
        System.out.println(policy.getClass().getSimpleName() + " " + total.get());	// AbortPolicy 1, CallerRunsPolicy 11, DiscardPolicy 1, DiscardOldestPolicy 10
    }

    // shutdownNow interrupts active work and returns queued tasks that never started
    static void immediateShutdown() throws InterruptedException {
        ExecutorService pool = Executors.newSingleThreadExecutor();
        CountDownLatch started = new CountDownLatch(1);
        pool.execute(() -> {
            started.countDown();
            try {
                new CountDownLatch(1).await();
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            }
        });
        try {
            if (!started.await(5, TimeUnit.SECONDS)) throw new AssertionError("worker did not start");
            pool.execute(() -> { });
        } finally {
            System.out.println(pool.shutdownNow().size());	// 1
            System.out.println(pool.awaitTermination(5, TimeUnit.SECONDS));	// true
        }
    }

    // workers grow to core size, then queue tasks, then grow to maximum when the queue refuses insertion
    // an unbounded queue usually prevents growth beyond core size and can exhaust memory under overload
    // fixed pools use unbounded queues while cached pools can create many workers under sustained blocking
    // shutdown rejects new tasks but drains queued work; close waits and shutdownNow cannot force termination
    // CallerRuns applies backpressure while discard policies risk silently losing work or stranding futures
    // size CPU pools around available processing capacity and measure I/O pools against wait time and resource limits
}
