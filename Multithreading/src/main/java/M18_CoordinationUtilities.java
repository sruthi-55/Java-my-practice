import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Phaser;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

// CountDownLatch is a one-shot completion gate and CyclicBarrier is a reusable rendezvous
// Phaser supports dynamic participants, Semaphore limits permits and Exchanger pairs two handoffs

public class M18_CoordinationUtilities {
    public static void main(String[] args) throws Exception {
        try (ExecutorService executor = Executors.newFixedThreadPool(4)) {
            CountDownLatch done = new CountDownLatch(2);
            executor.execute(done::countDown);
            executor.execute(done::countDown);
            System.out.println(done.await(5, TimeUnit.SECONDS));	// true

            // each generation opens only after both parties reach the barrier
            CyclicBarrier barrier = new CyclicBarrier(2, () -> System.out.println("phase complete"));	// phase complete twice
            Future<?> first = executor.submit(() -> { rendezvous(barrier); rendezvous(barrier); });
            Future<?> second = executor.submit(() -> { rendezvous(barrier); rendezvous(barrier); });
            first.get();
            second.get();

            // timeout breaks a generation and later arrivals fail until reset
            try {
                barrier.await(0, TimeUnit.SECONDS);
            } catch (TimeoutException exception) {
                System.out.println(barrier.isBroken());	// true
            }
            try {
                barrier.await();
            } catch (BrokenBarrierException exception) {
                System.out.println("broken barrier");	// broken barrier
            }
            barrier.reset();

            Phaser phaser = new Phaser(1);
            phaser.register();
            Future<?> participant = executor.submit(() -> phaser.arriveAndDeregister());
            phaser.arriveAndAwaitAdvance();
            participant.get();
            System.out.println(phaser.getPhase());	// 1
            System.out.println(phaser.getRegisteredParties());	// 1
            phaser.arriveAndDeregister();

            Exchanger<String> exchanger = new Exchanger<>();
            Future<String> exchanged = executor.submit(() -> exchanger.exchange("worker", 5, TimeUnit.SECONDS));
            System.out.println(exchanger.exchange("main", 5, TimeUnit.SECONDS));	// worker
            System.out.println(exchanged.get());	// main

            // a semaphore permits at most two tasks inside this resource boundary
            Semaphore permits = new Semaphore(2, true);
            AtomicInteger active = new AtomicInteger();
            AtomicInteger maximum = new AtomicInteger();
            java.util.concurrent.Callable<Void> limited = () -> {
                permits.acquire();
                try {
                    maximum.accumulateAndGet(active.incrementAndGet(), Math::max);
                } finally {
                    active.decrementAndGet();
                    permits.release();
                }
                return null;
            };
            for (Future<Void> result : executor.invokeAll(List.of(limited, limited, limited, limited))) result.get();
            System.out.println(maximum.get() <= 2);	// true
            System.out.println(permits.availablePermits());	// 2
        }
    }

    static void rendezvous(CyclicBarrier barrier) {
        try {
            barrier.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("rendezvous interrupted", exception);
        } catch (BrokenBarrierException | TimeoutException exception) {
            throw new IllegalStateException("rendezvous failed", exception);
        }
    }

    // release permits only after successful acquisition and never over-release
    // a semaphore is not an ownership lock and a different thread can release a permit
    // use a latch for completion, a barrier for fixed teams and a phaser for changing membership
}
