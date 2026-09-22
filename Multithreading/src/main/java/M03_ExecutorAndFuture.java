import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.CountDownLatch;
import java.util.List;
import java.util.concurrent.TimeUnit;

// ExecutorService separates task submission from thread creation and reuses worker threads
// Future represents a pending result and get waits for completion

public class M03_ExecutorAndFuture {
    public static void main(String[] args) throws Exception {
        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            // Callable returns a value and may throw checked exceptions while Runnable returns nothing
            Callable<Integer> task = () -> 20 + 22;
            Future<Integer> result = executor.submit(task);
            System.out.println(result.get());	// 42
            System.out.println(result.isDone());	// true
            System.out.println(executor.submit(() -> { }).get());	// null

            // invokeAll returns futures in submission order and invokeAny returns one successful result
            List<Callable<Integer>> tasks = List.of(() -> 1, () -> 2);
            for (Future<Integer> future : executor.invokeAll(tasks)) {
                System.out.println(future.get());	// 1, then 2
            }
            System.out.println(executor.invokeAny(List.<Callable<Integer>>of(() -> 42, () -> 42)));	// 42

            // CompletionService retrieves finished tasks rather than preserving submission order
            ExecutorCompletionService<Integer> completed = new ExecutorCompletionService<>(executor);
            CountDownLatch release = new CountDownLatch(1);
            completed.submit(() -> { release.await(); return 1; });
            completed.submit(() -> 2);
            try {
                Future<Integer> first = completed.poll(5, TimeUnit.SECONDS);
                if (first == null) throw new AssertionError("completion timed out");
                System.out.println(first.get());	// 2
            } finally {
                release.countDown();
            }
            System.out.println(completed.take().get());	// 1
        }
    }

    // Future.get publishes completed task writes to the waiting caller
    // get timeout limits the wait and does not automatically cancel work
    // cancellation and failure examples are in M09 and Exceptions/E09_ThreadAndFutureExceptions
}
