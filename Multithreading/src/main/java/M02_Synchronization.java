import java.util.stream.IntStream;

// synchronization provides mutual exclusion and visibility when threads share mutable state
// synchronized instance method locks the current object monitor

public class M02_Synchronization {
    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();
        Thread first = new Thread(() -> IntStream.range(0, 10_000).forEach(value -> counter.increment()));
        Thread second = new Thread(() -> IntStream.range(0, 10_000).forEach(value -> counter.increment()));
        first.start();
        second.start();
        first.join();
        second.join();
        System.out.println(counter.value());	// 20000
    }
}

class Counter {
    private int value;

    synchronized void increment() {
        value++;
    }

    synchronized int value() {
        return value;
    }
}
