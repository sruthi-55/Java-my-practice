import java.util.concurrent.CountDownLatch;

// a race condition makes correctness depend on timing and may exist even with individually atomic operations
// a data race has conflicting accesses without a happens-before order and at least one write
// atomicity prevents partial operations, visibility exposes writes and ordering constrains observable execution
// happens-before is a visibility and ordering guarantee rather than a claim about wall-clock execution order
// actions before task submission happen-before task actions and task actions happen-before successful Future.get
// queue handoff and latch countdown-to-await completion also publish prior writes

public class M08_RacesAndMemoryModel {
    public static void main(String[] args) throws InterruptedException {
        // both threads read before either writes so even this volatile counter loses an increment
        Shared shared = new Shared();
        CountDownLatch read = new CountDownLatch(2);
        Runnable increment = () -> {
            int observed = shared.count;
            read.countDown();
            try {
                read.await();
                shared.count = observed + 1;
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            }
        };
        Thread first = new Thread(increment);
        Thread second = new Thread(increment);
        first.start();
        second.start();
        first.join();
        second.join();
        System.out.println(shared.count);	// 1

        // writes before start reach the child and child writes reach the caller after successful join
        int[] value = {21};
        Thread worker = new Thread(() -> value[0] *= 2);
        worker.start();
        worker.join();
        System.out.println(value[0]);	// 42

        // reading a volatile flag written after the payload safely publishes the earlier payload write
        Thread publisher = new Thread(() -> { shared.payload = 42; shared.ready = true; });
        publisher.start();
        long deadline = System.nanoTime() + 5_000_000_000L;
        while (!shared.ready && System.nanoTime() < deadline) Thread.onSpinWait();
        if (!shared.ready) throw new AssertionError("publication timed out");
        System.out.println(shared.payload);	// 42
        publisher.join();
    }

    static class Shared {
        volatile int count;
        int payload;
        volatile boolean ready;
    }

    // monitor unlock happens-before a later lock of that same monitor
    // program order and transitivity combine with start, join and volatile edges to publish data
    // without these guarantees the JVM may reorder operations or reuse stale values
    // sleep and yield do not publish shared writes and a passing run does not prove race freedom
    // protect a check and its resulting update together rather than locking each separately
}
