import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

// thread dumps show stacks and lock ownership while ThreadMXBean can detect platform-thread deadlock cycles
// deterministic tests coordinate events explicitly and bound waits to avoid hanging the test process

public class M22_ConcurrencyDiagnostics {
    public static void main(String[] args) throws InterruptedException {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        System.out.println(bean.getThreadInfo(Thread.currentThread().threadId()).getThreadName());	// main
        if (!bean.isSynchronizerUsageSupported()) {
            System.out.println("lock diagnostics unsupported");	// only on JVMs without synchronizer diagnostics
            return;
        }

        // opposite lock order deliberately creates a deadlock that interruptible acquisition can unwind
        ReentrantLock first = new ReentrantLock();
        ReentrantLock second = new ReentrantLock();
        CountDownLatch holding = new CountDownLatch(2);
        Thread left = new Thread(() -> acquireOpposite(first, second, holding), "left");
        Thread right = new Thread(() -> acquireOpposite(second, first, holding), "right");
        left.start();
        right.start();
        try {
            if (!holding.await(5, TimeUnit.SECONDS)) throw new AssertionError("locks not acquired");
            long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
            long[] deadlocked;
            do {
                deadlocked = bean.findDeadlockedThreads();
                if (deadlocked != null) break;
                Thread.sleep(1);
            } while (System.nanoTime() < deadline);
            if (deadlocked == null) throw new AssertionError("deadlock not detected");
            boolean detected = Arrays.stream(deadlocked).anyMatch(id -> id == left.threadId());
            System.out.println(detected);	// true
            if (!detected) throw new AssertionError("wrong threads detected");
            ThreadInfo info = bean.getThreadInfo(left.threadId());
            System.out.println(info.getLockOwnerName());	// right
        } finally {
            left.interrupt();
            right.interrupt();
            left.join(5_000);
            right.join(5_000);
            if (left.isAlive() || right.isAlive()) throw new AssertionError("workers leaked");
        }
        System.out.println(first.isLocked() || second.isLocked());	// false
    }

    static void acquireOpposite(ReentrantLock own, ReentrantLock other, CountDownLatch holding) {
        own.lock();
        try {
            holding.countDown();
            holding.await();
            other.lockInterruptibly();
            try {
                // acquisition can succeed after the other participant is interrupted and releases its lock
            } finally {
                other.unlock();
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        } finally {
            own.unlock();
        }
    }

    // jcmd <pid> Thread.print shows platform-thread stacks and monitor or synchronizer ownership
    // jcmd <pid> Thread.dump_to_file -format=json <file> can include Java 21 virtual threads
    // BLOCKED suggests monitor contention while WAITING often means intentional coordination
    // RUNNABLE alone does not prove CPU saturation; compare repeated stacks and CPU measurements
    // JFR reveals contention and Java 21 virtual-thread pinning without relying only on one dump
    // repeated successful stress tests cannot establish correctness without a happens-before argument
}
