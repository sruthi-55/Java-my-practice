import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// CompletableFuture composes asynchronous results through dependent stages
// thenCompose flattens a dependent future while thenCombine joins two independent results

public class M15_CompletableFuture {
    public static void main(String[] args) throws Exception {
        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            CompletableFuture.runAsync(() -> System.out.println("started"), executor).join();	// started
            CompletableFuture<Integer> first = CompletableFuture.supplyAsync(() -> 21, executor);
            System.out.println(first.thenApply(value -> value * 2).join());	// 42
            first.thenAccept(value -> System.out.println(value)).join();	// 21
            first.thenRun(() -> System.out.println("done")).join();	// done
            System.out.println(first.thenCompose(value -> CompletableFuture.supplyAsync(() -> value + 1, executor)).join());	// 22
            CompletableFuture<Integer> second = CompletableFuture.supplyAsync(() -> 10, executor);
            System.out.println(first.thenCombine(second, Integer::sum).join());	// 31
            CompletableFuture.allOf(first, second).join();
            System.out.println(first.join() + second.join());	// 31
            System.out.println(CompletableFuture.anyOf(CompletableFuture.completedFuture(7), new CompletableFuture<>()).join());	// 7
            System.out.println(first.thenApplyAsync(value -> value + 2, executor).join());	// 23
        }

        CompletableFuture<Integer> failed = CompletableFuture.failedFuture(new IllegalArgumentException("bad input"));
        try {
            failed.get();
        } catch (ExecutionException exception) {
            System.out.println(exception.getCause().getMessage());	// bad input
        }
        try {
            failed.join();
        } catch (CompletionException exception) {
            System.out.println(exception.getCause().getMessage());	// bad input
        }
        System.out.println(failed.exceptionally(exception -> 0).join());	// 0
        System.out.println(failed.handle((value, exception) -> exception == null ? value : -1).join());	// -1
        CompletableFuture<Integer> observed = failed.whenComplete((value, exception) -> {
            System.out.println(exception.getMessage());	// bad input
        });
        System.out.println(observed.isCompletedExceptionally());	// true

        // timeout completes this future exceptionally but does not stop arbitrary underlying work
        try {
            new CompletableFuture<Integer>().orTimeout(1, TimeUnit.MILLISECONDS).join();
        } catch (CompletionException exception) {
            System.out.println(exception.getCause().getClass().getSimpleName());	// TimeoutException
        }
        System.out.println(new CompletableFuture<Integer>().completeOnTimeout(5, 1, TimeUnit.MILLISECONDS).join());	// 5
        System.out.println(new CompletableFuture<Integer>().cancel(true));	// true
        System.out.println(CompletableFuture.supplyAsync(() -> 9).join());	// 9
    }

    // non-Async stages may run on a completing or calling thread while Async stages use an executor
    // default async execution normally uses the common pool so isolate blocking work with a suitable executor
    // CompletableFuture.cancel does not use interruption to control its underlying computation
    // allOf waits for all stages without collecting values and anyOf may finish with a failure
}
