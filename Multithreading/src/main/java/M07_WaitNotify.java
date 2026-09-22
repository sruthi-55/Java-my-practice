import java.util.concurrent.CountDownLatch;

// wait releases the owned monitor and suspends the thread until notification or interruption
// notify wakes one waiting thread while notifyAll wakes every thread waiting on the same monitor
// wait conditions belong in loops because wakeups may be spurious or state may change before reacquisition

public class M07_WaitNotify {
    public static void main(String[] args) throws InterruptedException {
        MessageBox box = new MessageBox();
        Thread consumer = new Thread(() -> System.out.println(box.take()));	// ready
        consumer.start();
        box.put("ready");
        consumer.join();
        notifyOne();

        // a timed wait releases and reacquires the monitor even if no notification arrives
        Object monitor = new Object();
        synchronized (monitor) {
            monitor.wait(1);
            monitor.notify();
            System.out.println(Thread.holdsLock(monitor));	// true
        }
    }

    // one waiter receives notify but cannot continue until the notifier releases the monitor
    static void notifyOne() throws InterruptedException {
        Object monitor = new Object();
        boolean[] ready = {false};
        CountDownLatch entered = new CountDownLatch(1);
        Thread waiter = new Thread(() -> {
            synchronized (monitor) {
                entered.countDown();
                try {
                    while (!ready[0]) monitor.wait();
                    System.out.println("waiter resumed");	// waiter resumed
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        waiter.start();
        try {
            entered.await();
            synchronized (monitor) {
                ready[0] = true;
                monitor.notify();
                System.out.println("notifier still owns monitor");	// notifier still owns monitor
            }
            waiter.join();
        } finally {
            waiter.interrupt();
            waiter.join();
        }
    }

    // wait, notify and notifyAll must be called while owning the same object's monitor
    // notification does not transfer ownership and the awakened thread must reacquire the monitor
    // notify selects one arbitrary waiter while notifyAll lets every waiter recheck its condition
    // a notification is not saved for later so check the guarded condition before waiting
    // use notifyAll when different conditions share a monitor to avoid waking only the wrong waiter
}

class MessageBox {
    private String message;

    synchronized void put(String value) {
        message = value;
        notifyAll();
    }

    synchronized String take() {
        while (message == null) {
            try {
                wait();
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                return "interrupted";
            }
        }
        return message;
    }
}
