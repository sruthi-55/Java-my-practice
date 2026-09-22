// Thread represents an independent path of execution inside a Java process
// a thread can be created by extending Thread or by supplying a Runnable task
// Runnable is preferred because it separates the task from the thread executing it

public class M01_ThreadAndRunnable {
    public static void main(String[] args) throws InterruptedException {
        // extending Thread combines the task and thread in one class
        Thread extendedThread = new MessageThread();

        // implementing Runnable keeps the task separate and supplies it to a Thread
        Runnable task = new MessageTask();
        Thread runnableThread = new Thread(task);

        extendedThread.start();
        runnableThread.start();
        extendedThread.join();
        runnableThread.join();

        // a method reference supplies a Runnable task without a separate implementation class
        Thread referenced = new Thread(M01_ThreadAndRunnable::printTask, "reference-worker");
        referenced.start();
        referenced.join();
    }

    static void printTask() {
        System.out.println(Thread.currentThread().getName());	// reference-worker
    }
}

// extending Thread requires overriding run with the work to execute
class MessageThread extends Thread {
    @Override
    public void run() {
        System.out.println("extended Thread");	// extended Thread
    }
}

// implementing Runnable requires defining run without using the single class inheritance
class MessageTask implements Runnable {
    @Override
    public void run() {
        System.out.println("implemented Runnable");	// implemented Runnable
    }
}
