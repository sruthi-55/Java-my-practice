import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

// CompletableFuture represents a value or failure and supports dependent completion stages
// exceptionally recovers failures, handle transforms either outcome and whenComplete observes either outcome

public class E10_CompletableFutureExceptions {
    public static void main(String[] args) throws InterruptedException {
        CompletableFuture<Integer> failed = CompletableFuture.failedFuture(new IllegalStateException("unavailable"));

        // get wraps failure in checked ExecutionException while join uses unchecked CompletionException
        try {
            failed.get();
        } catch (ExecutionException exception) {
            System.out.println(exception.getCause().getMessage());	// unavailable
        }
        try {
            failed.join();
        } catch (CompletionException exception) {
            System.out.println(exception.getCause().getMessage());	// unavailable
        }

        // a fallback is appropriate only when it represents an acceptable domain result
        System.out.println(failed.exceptionally(exception -> -1).join());	// -1
        System.out.println(failed.handle((value, exception) -> exception == null ? value : 0).join());	// 0
        System.out.println(CompletableFuture.completedFuture(21).handle((value, exception) -> value * 2).join());	// 42

        // observing a failure with whenComplete does not recover the dependent stage
        CompletableFuture<Integer> observed = failed.whenComplete((value, exception) -> {
            System.out.println(exception.getMessage());	// unavailable
        });
        System.out.println(observed.isCompletedExceptionally());	// true
        System.out.println(failed.isCompletedExceptionally());	// true
    }

    // recovery creates a new stage and does not change the failed original future
    // orTimeout completes the future exceptionally but does not guarantee interruption of underlying work
}
