import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

// ExecutorService separates task submission from thread creation and reuses worker threads
// Future represents a pending result and get waits for completion

public class M03_ExecutorAndFuture {
    public static void main(String[] args) throws Exception {
        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            Future<Integer> result = executor.submit(() -> 20 + 22);
            System.out.println(result.get());	// 42
        }
    }
}
