// process is an executing program while thread is a lightweight execution unit inside a process
// threads have separate call stacks but share process memory and can run concurrently
// synchronization coordinates shared state to prevent races and inconsistent results
// concurrency overlaps task progress while parallelism executes tasks simultaneously on different cores
// context switching saves and restores execution state and adds scheduling overhead
// CPU-bound work spends time computing while I/O-bound work spends time waiting for external operations

public class M00_MultithreadingIntroduction {
    public static void main(String[] args) throws InterruptedException {
        Thread worker = new Thread(() -> System.out.println("worker"), "worker");	// worker
        worker.start();
        worker.join();
        System.out.println(Thread.currentThread().getName());	// main
    }

    // start creates a new thread and call stack while direct run executes like a normal method on the current thread
    // join waits for another thread to finish
    // extra threads can improve responsiveness but add stack memory, coordination and contention costs
    // multitasking schedules multiple activities and does not require simultaneous execution
    // a local reference is thread-confined but its referenced object may still be shared
}
