import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

// Queue processes elements from the head and commonly follows FIFO order
// PriorityQueue selects the head by natural order or a supplied Comparator instead of insertion order
// BlockingQueue can wait while inserting into a full queue or removing from an empty queue

public class C11_QueueMethods {
    public static void main(String[] args) throws InterruptedException {
        Queue<String> queue = new ArrayDeque<>();
        System.out.println(queue.add("Java"));	// true, exception if unable to add
        System.out.println(queue.offer("SQL"));	// true
        System.out.println(queue.offer("Spring"));	// true

        System.out.println(queue.element());	// Java - returns top ele, exception when q is empty
        System.out.println(queue.peek());	// Java

        System.out.println(queue.remove());	// Java, exception if q empty
        System.out.println(queue.poll());	// SQL

        System.out.println(queue.size());	// 1
        System.out.println(queue.contains("Spring"));	// true
        System.out.println(queue.remove("Spring"));	// true
        System.out.println(queue.poll());	// null
        System.out.println(queue.peek());	// null

        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        minHeap.addAll(java.util.List.of(30, 10, 20));
        System.out.println(minHeap.peek());	// 10
        System.out.println(minHeap.comparator());	// null, natural ordering
        System.out.println(minHeap.poll());	// 10

        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
        maxHeap.addAll(java.util.List.of(30, 10, 20));
        System.out.println(maxHeap.peek());	// 30
        System.out.println(maxHeap.poll());	// 30

        Queue<String> linkedQueue = new LinkedList<>();
        linkedQueue.offer("first");
        linkedQueue.offer("second");
        System.out.println(linkedQueue.poll());	// first

        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(1);  // q with max capacity - 1
        blockingQueue.put("task");      // blocks if q is full
        System.out.println(blockingQueue.remainingCapacity());	// 0
        // blockingQueue.put("another task");
        // instead of immediately throwing an exception or returning false,
        // the thread waits until space becomes available.
        System.out.println(blockingQueue.take());	// task. removes and returns ele
        // waits if queue is empty

        System.out.println(blockingQueue.remainingCapacity());	// 1

        // bounded offer reports capacity failure while add throws for the same full queue
        blockingQueue.add("full");
        System.out.println(blockingQueue.offer("extra"));	// false
        try {
            blockingQueue.add("extra");
        } catch (IllegalStateException exception) {
            System.out.println(exception.getClass().getSimpleName());	// IllegalStateException
        }

        // removing from an empty queue throws while the earlier poll returned null
        try {
            queue.remove();
        } catch (java.util.NoSuchElementException exception) {
            System.out.println(exception.getClass().getSimpleName());	// NoSuchElementException
        }

        // repeated priority-queue polling is ordered but ordinary iteration is not sorted
        java.util.List<Integer> ordered = new java.util.ArrayList<>();
        while (!minHeap.isEmpty()) ordered.add(minHeap.poll());
        System.out.println(ordered);	// [20, 30]
    }

    // add and remove throw on failure while offer and poll return false or null
    // element and peek inspect the head, but element throws when empty while peek returns null
    // PriorityQueue allows duplicates, rejects null and exposes its ordering through comparator()
    // BlockingQueue put and take wait when the queue is full or empty and support producer-consumer workflows
}
