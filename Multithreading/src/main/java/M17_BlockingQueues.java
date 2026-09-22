import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

// BlockingQueue supports waiting producers and consumers and bounded capacity supplies backpressure
// a poison pill is an agreed message that tells a consumer to stop after preceding work is consumed

public class M17_BlockingQueues {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(2);
        List<Integer> consumed = new ArrayList<>();
        Thread consumer = new Thread(() -> {
            try {
                for (int value; (value = queue.take()) != -1;) consumed.add(value);
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            }
        });
        consumer.start();
        try {
            queue.put(1);
            queue.put(2);
            queue.put(-1);
            consumer.join();
        } finally {
            consumer.interrupt();
            consumer.join();
        }
        System.out.println(consumed);	// [1, 2]

        BlockingQueue<Integer> bounded = new LinkedBlockingQueue<>(1);
        bounded.put(1);
        System.out.println(bounded.offer(2, 1, TimeUnit.MILLISECONDS));	// false
        System.out.println(bounded.take());	// 1
        System.out.println(bounded.poll(1, TimeUnit.MILLISECONDS));	// null

        // SynchronousQueue has zero storage capacity and hands each item directly to a waiting peer
        BlockingQueue<Integer> handoff = new SynchronousQueue<>();
        Thread sender = new Thread(() -> {
            try {
                handoff.put(42);
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            }
        });
        sender.start();
        try {
            System.out.println(handoff.poll(5, TimeUnit.SECONDS));	// 42
        } finally {
            sender.interrupt();
            sender.join();
        }
    }

    // ArrayBlockingQueue uses fixed array storage while LinkedBlockingQueue uses linked nodes
    // always choose a deliberate capacity because an effectively unbounded queue can exhaust memory
    // multiple consumers need a termination protocol such as one poison pill per consumer
    // queues reject null and have no built-in close operation; cancellation must release blocked participants
}
