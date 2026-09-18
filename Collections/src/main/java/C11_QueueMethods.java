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
        System.out.println(queue.add("Java"));	// true
        System.out.println(queue.offer("SQL"));	// true
        System.out.println(queue.offer("Spring"));	// true
        System.out.println(queue.element());	// Java
        System.out.println(queue.peek());	// Java
        System.out.println(queue.remove());	// Java
        System.out.println(queue.poll());	// SQL
        System.out.println(queue.size());	// 1
        System.out.println(queue.contains("Spring"));	// true
        System.out.println(queue.remove("Spring"));	// true
        System.out.println(queue.poll());	// null
        System.out.println(queue.peek());	// null

        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        minHeap.addAll(java.util.List.of(30, 10, 20));
        System.out.println(minHeap.peek());	// 10
        System.out.println(minHeap.comparator());	// null
        System.out.println(minHeap.poll());	// 10

        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
        maxHeap.addAll(java.util.List.of(30, 10, 20));
        System.out.println(maxHeap.peek());	// 30
        System.out.println(maxHeap.poll());	// 30

        Queue<String> linkedQueue = new LinkedList<>();
        linkedQueue.offer("first");
        linkedQueue.offer("second");
        System.out.println(linkedQueue.poll());	// first

        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(1);
        blockingQueue.put("task");
        System.out.println(blockingQueue.remainingCapacity());	// 0
        System.out.println(blockingQueue.take());	// task
    }

    // add and remove throw on failure while offer and poll return false or null
    // element and peek inspect the head, but element throws when empty while peek returns null
    // PriorityQueue allows duplicates, rejects null and exposes its ordering through comparator()
    // BlockingQueue put and take wait when the queue is full or empty and support producer-consumer workflows
}
