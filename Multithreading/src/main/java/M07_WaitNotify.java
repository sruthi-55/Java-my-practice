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
    }

    // wait, notify and notifyAll must be called while owning the same object's monitor
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
