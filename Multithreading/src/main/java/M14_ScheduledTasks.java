import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

// fixed rate targets scheduled start times while fixed delay waits after each completed execution
// a periodic task never overlaps with itself and an uncaught failure suppresses future executions

public class M14_ScheduledTasks {
    public static void main(String[] args) throws Exception {
        try (ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2)) {
            System.out.println(scheduler.schedule(() -> 42, 1, TimeUnit.MILLISECONDS).get());	// 42
            periodic(scheduler, true);
            periodic(scheduler, false);
            ScheduledFuture<?> future = scheduler.schedule(() -> { }, 1, TimeUnit.DAYS);
            System.out.println(future.cancel(false));	// true
        }
    }

    // both scheduling modes stop after this deliberate failure on the third execution
    static void periodic(ScheduledExecutorService scheduler, boolean fixedRate) throws Exception {
        AtomicInteger executions = new AtomicInteger();
        Runnable task = () -> {
            if (executions.incrementAndGet() == 3) throw new IllegalStateException("periodic failure");
        };
        ScheduledFuture<?> future = fixedRate
                ? scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.MILLISECONDS)
                : scheduler.scheduleWithFixedDelay(task, 0, 1, TimeUnit.MILLISECONDS);
        try {
            future.get(5, TimeUnit.SECONDS);
        } catch (ExecutionException exception) {
            System.out.println(executions.get());	// 3
            System.out.println(exception.getCause().getMessage());	// periodic failure
        } finally {
            future.cancel(true);
        }
    }

    // an overlong fixed-rate execution makes subsequent runs late rather than concurrent
    // Timer uses one execution thread and an unchecked task failure can terminate that timer
    // scheduled executors support multiple workers, futures and finer lifecycle control
}
