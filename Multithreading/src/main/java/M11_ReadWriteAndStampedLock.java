import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

// read-write locks permit concurrent readers while writers require exclusive access
// StampedLock offers optimistic reads whose copied values must be validated before use

public class M11_ReadWriteAndStampedLock {
    public static void main(String[] args) throws InterruptedException {
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        int[] value = {0};
        lock.writeLock().lock();
        try {
            value[0] = 42;
            lock.readLock().lock();
        } finally {
            lock.writeLock().unlock();
        }
        // acquiring read before releasing write safely downgrades ownership
        try {
            System.out.println(value[0]);	// 42
        } finally {
            lock.readLock().unlock();
        }

        Point point = new Point();
        point.move(3, 4);
        System.out.println(point.sum());	// 7

        // an intervening write invalidates an optimistic stamp and requires a locked retry
        long optimistic = point.lock.tryOptimisticRead();
        point.move(5, 6);
        System.out.println(point.lock.validate(optimistic));	// false
        System.out.println(point.sum());	// 11

        // conversion can upgrade a sole read stamp without releasing it but zero means conversion failed
        long stamp = point.lock.readLock();
        try {
            long writeStamp = point.lock.tryConvertToWriteLock(stamp);
            System.out.println(writeStamp != 0);	// true
            if (writeStamp != 0) stamp = writeStamp;
        } finally {
            point.lock.unlock(stamp);
        }

        // another reader can enter while a read lock is held but a different writer cannot
        lock.readLock().lock();
        try {
            Thread peer = new Thread(() -> {
                boolean read = lock.readLock().tryLock();
                try {
                    System.out.println(read);	// true
                } finally {
                    if (read) lock.readLock().unlock();
                }
                boolean write = lock.writeLock().tryLock();
                try {
                    System.out.println(write);	// false
                } finally {
                    if (write) lock.writeLock().unlock();
                }
            });
            peer.start();
            peer.join();
        } finally {
            lock.readLock().unlock();
        }
    }

    static class Point {
        private final StampedLock lock = new StampedLock();
        private int x;
        private int y;

        void move(int x, int y) {
            long stamp = lock.writeLock();
            try {
                this.x = x;
                this.y = y;
            } finally {
                lock.unlockWrite(stamp);
            }
        }

        int sum() {
            long stamp = lock.tryOptimisticRead();
            int first = x;
            int second = y;
            if (!lock.validate(stamp)) {
                stamp = lock.readLock();
                try {
                    first = x;
                    second = y;
                } finally {
                    lock.unlockRead(stamp);
                }
            }
            return first + second;
        }
    }

    // read-to-write upgrading can deadlock with ReentrantReadWriteLock; release and recheck instead
    // StampedLock is not reentrant and a stamp is not a thread-owned monitor
    // read-write locking helps only when concurrent read work outweighs coordination overhead
}
