import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.concurrent.atomic.LongAdder;

// volatile provides visibility and ordering but does not make compound actions atomic
// atomic classes perform thread-safe lock-free updates to individual values

public class M04_AtomicAndVolatile {
    public static void main(String[] args) throws InterruptedException {
        Status status = new Status();
        Thread worker = new Thread(() -> {
            status.increment();
            status.stop();
        });
        worker.start();

        long deadline = System.nanoTime() + 5_000_000_000L;
        while (status.isRunning() && System.nanoTime() < deadline) Thread.onSpinWait();
        if (status.isRunning()) throw new AssertionError("worker did not publish completion");
        worker.join();
        System.out.println(status.count());	// 1
        atomicMethods();
    }

    // atomic operations protect one update and cannot automatically preserve multi-field invariants
    static void atomicMethods() {
        AtomicInteger count = new AtomicInteger(1);
        System.out.println(count.getAndIncrement());	// 1
        System.out.println(count.incrementAndGet());	// 3
        System.out.println(count.compareAndSet(3, 10));	// true
        System.out.println(count.updateAndGet(value -> value * 2));	// 20
        System.out.println(count.accumulateAndGet(2, Integer::sum));	// 22

        // CAS loops retry when another thread updates the observed value first
        int previous;
        do {
            previous = count.get();
        } while (!count.compareAndSet(previous, previous + 1));
        System.out.println(count.get());	// 23
        System.out.println(new AtomicLong(10).addAndGet(5));	// 15
        System.out.println(new AtomicBoolean(false).compareAndSet(false, true));	// true

        // plain reference CAS cannot detect an A-to-B-to-A change
        AtomicReference<String> reference = new AtomicReference<>("A");
        reference.set("B");
        reference.set("A");
        System.out.println(reference.compareAndSet("A", "C"));	// true

        // a version stamp detects the same reference returning after intervening changes
        AtomicStampedReference<String> stamped = new AtomicStampedReference<>("A", 0);
        stamped.set("B", 1);
        stamped.set("A", 2);
        System.out.println(stamped.compareAndSet("A", "C", 0, 3));	// false

        LongAdder total = new LongAdder();
        total.increment();
        total.add(4);
        System.out.println(total.sum());	// 5
    }

    // update functions may be retried so they must not perform external side effects
    // LongAdder spreads contended updates but sum is not an atomic snapshot during concurrent writes
    // AtomicLong is preferable when each update must yield one exact globally ordered value
    // a volatile reference does not make mutations inside its object volatile
}

class Status {
    private final AtomicInteger count = new AtomicInteger();
    private volatile boolean running = true;

    void increment() {
        count.incrementAndGet();
    }

    void stop() {
        running = false;
    }

    boolean isRunning() {
        return running;
    }

    int count() {
        return count.get();
    }
}
