import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// ThreadLocal stores a separate value for each thread and does not share one value safely across threads
// pooled workers outlive individual tasks so remove request context in finally

public class M19_ThreadLocal {
    public static void main(String[] args) throws Exception {
        ThreadLocal<String> context = ThreadLocal.withInitial(() -> "unset");
        context.set("main");
        try (ExecutorService executor = Executors.newSingleThreadExecutor()) {
            System.out.println(executor.submit(context::get).get());	// unset
            executor.submit(() -> context.set("leaked request")).get();
            System.out.println(executor.submit(context::get).get());	// leaked request

            // the same worker now clears context before returning to the pool
            executor.submit(() -> {
                try {
                    context.set("request-2");
                } finally {
                    context.remove();
                }
            }).get();
            System.out.println(executor.submit(context::get).get());	// unset
            System.out.println(context.get());	// main
        } finally {
            context.remove();
        }

        // inheritance copies the value when the worker is created rather than on every submission
        InheritableThreadLocal<String> inherited = new InheritableThreadLocal<>();
        inherited.set("first");
        try (ExecutorService executor = Executors.newSingleThreadExecutor()) {
            System.out.println(executor.submit(inherited::get).get());	// first
            inherited.set("second");
            System.out.println(executor.submit(inherited::get).get());	// first
            executor.submit(inherited::remove).get();
        } finally {
            inherited.remove();
        }
    }

    // ThreadLocal keys are weakly held but values can remain retained by long-lived workers until cleanup
    // inherited mutable objects may still be shared because the default inheritance does not deep-copy values
}
