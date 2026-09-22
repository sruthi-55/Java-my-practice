import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

// interview exercises combine guarded state, ordering and explicit worker lifecycle management
// verify results after completion rather than treating print order or sleeps as synchronization

public class M23_InterviewExercises {
    public static void main(String[] args) throws Exception {
        alternatingNumbers();
        boundedBuffer();
        orderedTasks();
        deadlineCancellation();
    }

    // odd and even workers use one guarded turn counter and notify each other after every number
    static void alternatingNumbers() throws Exception {
        Object monitor = new Object();
        int[] next = {1};
        List<Integer> output = new ArrayList<>();
        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            List<Future<?>> tasks = new ArrayList<>();
            for (int parity = 0; parity < 2; parity++) {
                int turn = parity;
                tasks.add(executor.submit(() -> {
                    synchronized (monitor) {
                        try {
                            while (next[0] <= 6) {
                                while (next[0] <= 6 && next[0] % 2 != turn) monitor.wait();
                                if (next[0] > 6) break;
                                output.add(next[0]++);
                                monitor.notifyAll();
                            }
                        } catch (InterruptedException exception) {
                            next[0] = 7;
                            monitor.notifyAll();
                            Thread.currentThread().interrupt();
                        }
                    }
                }));
            }
            try {
                for (Future<?> task : tasks) task.get(5, TimeUnit.SECONDS);
            } finally {
                tasks.forEach(task -> task.cancel(true));
            }
        }
        if (!output.equals(List.of(1, 2, 3, 4, 5, 6))) throw new AssertionError("wrong alternation");
        System.out.println(output);	// [1, 2, 3, 4, 5, 6]
    }

    // a manual bounded buffer waits for capacity or data using the same guarded queue
    static void boundedBuffer() throws Exception {
        Buffer buffer = new Buffer(2);
        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            Future<?> producer = executor.submit(() -> {
                for (int value = 1; value <= 3; value++) buffer.put(value);
                return null;
            });
            Future<List<Integer>> consumer = executor.submit(() -> List.of(buffer.take(), buffer.take(), buffer.take()));
            try {
                List<Integer> values = consumer.get(5, TimeUnit.SECONDS);
                producer.get(5, TimeUnit.SECONDS);
                if (!values.equals(List.of(1, 2, 3))) throw new AssertionError("wrong buffer contents");
                System.out.println(values);	// [1, 2, 3]
            } finally {
                producer.cancel(true);
                consumer.cancel(true);
            }
        }
    }

    static class Buffer {
        private final int capacity;
        private final ArrayDeque<Integer> queue = new ArrayDeque<>();

        Buffer(int capacity) {
            if (capacity <= 0) throw new IllegalArgumentException("capacity must be positive");
            this.capacity = capacity;
        }

        synchronized void put(int value) throws InterruptedException {
            while (queue.size() == capacity) wait();
            queue.addLast(value);
            notifyAll();
        }

        synchronized int take() throws InterruptedException {
            while (queue.isEmpty()) wait();
            int value = queue.removeFirst();
            notifyAll();
            return value;
        }
    }

    // the second task starts first but cannot pass the gate until the first task records completion
    static void orderedTasks() throws Exception {
        CountDownLatch firstDone = new CountDownLatch(1);
        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            Future<?> second = executor.submit(() -> {
                if (!firstDone.await(5, TimeUnit.SECONDS)) throw new AssertionError("first task missing");
                System.out.println("second");	// second
                return null;
            });
            Future<?> first = executor.submit(() -> {
                try {
                    System.out.println("first");	// first
                } finally {
                    firstDone.countDown();
                }
            });
            first.get();
            second.get();
        }
    }

    // enforce a waiting deadline and request interruption before closing the executor
    static void deadlineCancellation() throws Exception {
        CountDownLatch started = new CountDownLatch(1);
        CountDownLatch stopped = new CountDownLatch(1);
        try (ExecutorService executor = Executors.newSingleThreadExecutor()) {
            Future<?> pending = executor.submit(() -> {
                started.countDown();
                try {
                    new CountDownLatch(1).await();
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                } finally {
                    stopped.countDown();
                }
            });
            try {
                if (!started.await(5, TimeUnit.SECONDS)) throw new AssertionError("task not started");
                pending.get(1, TimeUnit.MILLISECONDS);
            } catch (TimeoutException exception) {
                pending.cancel(true);
            } finally {
                pending.cancel(true);
            }
            if (!stopped.await(5, TimeUnit.SECONDS)) throw new AssertionError("cancellation ignored");
            System.out.println(pending.isCancelled());	// true
        }
    }

    // counters, singleton publication, resource limits and result aggregation are demonstrated in M02, M12, M18 and M15
}
