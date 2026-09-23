import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.LockSupport;

// ReentrantLock supports explicit acquisition, timed attempts and interruptible waiting
// Condition supplies a separate wait queue associated with a lock
// LockSupport parks a thread using a permit without requiring ownership of an object monitor
// AQS is a synchronizer-building framework used by locks and coordination classes rather than an application lock API

public class M10_LockAndCondition {
    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock(true);
        lock.lock();
        try {
            lock.lock();
            try {
                System.out.println(lock.getHoldCount());	// 2
                System.out.println(lock.isFair());	// true
            } finally {
                lock.unlock();
            }

            // another thread cannot acquire the held lock with either immediate or timed attempts
            Thread contender = new Thread(() -> {
                boolean acquired = lock.tryLock();
                try {
                    System.out.println(acquired);	// false
                } finally {
                    if (acquired) lock.unlock();
                }
                try {
                    acquired = lock.tryLock(1, TimeUnit.MILLISECONDS);
                    try {
                        System.out.println(acquired);	// false
                    } finally {
                        if (acquired) lock.unlock();
                    }
                    Thread.currentThread().interrupt();
                    lock.lockInterruptibly();
                    try {
                        throw new AssertionError("interrupt ignored");
                    } finally {
                        lock.unlock();
                    }
                } catch (InterruptedException exception) {
                    System.out.println("acquisition interrupted");	// acquisition interrupted
                    Thread.currentThread().interrupt();
                }
            });
            contender.start();
            contender.join();
        } finally {
            lock.unlock();
        }

        // different conditions separate consumers waiting for data from producers waiting for capacity
        Slot slot = new Slot();
        Thread producer = new Thread(() -> {
            try {
                slot.put(42);
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            }
        });
        producer.start();
        System.out.println(slot.take());	// 42
        producer.join();
        parkingRules();
    }

    static void parkingRules() {
        // unpark can supply one permit before park but permits never accumulate beyond one
        LockSupport.unpark(Thread.currentThread());
        LockSupport.parkNanos(1_000_000);
        System.out.println(Thread.currentThread().isInterrupted());	// false

        // park returns on interruption without throwing or clearing the interrupt flag
        Thread.currentThread().interrupt();
        LockSupport.park();
        System.out.println(Thread.currentThread().isInterrupted());	// true
        Thread.interrupted();
    }

    static class Slot {
        private final ReentrantLock lock = new ReentrantLock();
        private final Condition notEmpty = lock.newCondition();
        private final Condition notFull = lock.newCondition();
        private Integer value;

        void put(int item) throws InterruptedException {
            lock.lockInterruptibly();
            try {
                while (value != null) notFull.await();
                value = item;
                notEmpty.signal();
            } finally {
                lock.unlock();
            }
        }

        int take() throws InterruptedException {
            lock.lockInterruptibly();
            try {
                while (value == null) notEmpty.await();
                int item = value;
                value = null;
                notFull.signalAll();
                return item;
            } finally {
                lock.unlock();
            }
        }
    }

    // await releases and reacquires its lock and conditions must be tested in loops
    // fair locks may reduce starvation at a throughput cost and untimed tryLock may barge
    // synchronized is simpler when timed, interruptible or multiple-condition acquisition is unnecessary
    // park may return spuriously so real coordination must loop around a safely published condition
    // synchronized monitor acquisition is not interruptible while lockInterruptibly explicitly supports cancellation
}
