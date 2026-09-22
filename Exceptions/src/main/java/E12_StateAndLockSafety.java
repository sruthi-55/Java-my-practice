import java.util.concurrent.locks.ReentrantLock;

// exception safety means preserving valid state and releasing resources when an operation fails
// throwing an exception does not roll back mutations already performed

public class E12_StateAndLockSafety {
    public static void main(String[] args) {
        // an unsafe operation mutates state before validation and leaves a partial update
        int[] balance = {100};
        try {
            balance[0] -= 150;
            if (balance[0] < 0) throw new IllegalArgumentException("insufficient balance");
        } catch (IllegalArgumentException exception) {
            System.out.println(balance[0]);	// -50
        }

        // validate before mutation so failure leaves the original state unchanged
        balance[0] = 100;
        try {
            withdraw(balance, 150);
        } catch (IllegalArgumentException exception) {
            System.out.println(balance[0]);	// 100
        }

        // synchronized automatically releases the monitor when an exception escapes its block
        Object monitor = new Object();
        try {
            synchronized (monitor) {
                throw new IllegalStateException("critical section failed");
            }
        } catch (IllegalStateException exception) {
            System.out.println(Thread.holdsLock(monitor));	// false
        }

        // explicit locks require unlock in finally after successful acquisition
        ReentrantLock lock = new ReentrantLock();
        try {
            lock.lock();
            try {
                throw new IllegalStateException("critical section failed");
            } finally {
                lock.unlock();
            }
        } catch (IllegalStateException exception) {
            System.out.println(lock.isLocked());	// false
        }
    }

    static void withdraw(int[] balance, int amount) {
        if (amount <= 0) throw new IllegalArgumentException("amount must be positive");
        if (amount > balance[0]) throw new IllegalArgumentException("insufficient balance");
        balance[0] -= amount;
    }

    // concurrent validation and mutation must occur under the same synchronization boundary
    // compute a replacement first and publish it only after every required validation succeeds
}
