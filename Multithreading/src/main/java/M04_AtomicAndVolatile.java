import java.util.concurrent.atomic.AtomicInteger;

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

        while (status.isRunning()) Thread.onSpinWait();
        worker.join();
        System.out.println(status.count());	// 1
    }
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
