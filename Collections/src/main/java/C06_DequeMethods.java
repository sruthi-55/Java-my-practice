import java.util.ArrayDeque;
import java.util.Deque;

// Deque is a double-ended queue that supports insertion, inspection and removal at both ends
// ArrayDeque is a resizable-array Deque implementation that rejects null elements
// Deque can work as both a FIFO queue and a LIFO stack

public class C06_DequeMethods {
    public static void main(String[] args) {
        Deque<String> deque = new ArrayDeque<>();

        deque.addFirst("middle");
        deque.offerFirst("first");
        deque.addLast("last");
        deque.offerLast("tail");
        System.out.println(deque);	// [first, middle, last, tail]

        System.out.println(deque.getFirst());	// first
        System.out.println(deque.peekFirst());	// first
        System.out.println(deque.getLast());	// tail
        System.out.println(deque.peekLast());	// tail

        System.out.println(deque.removeFirst());	// first
        System.out.println(deque.pollFirst());	// middle
        System.out.println(deque.removeLast());	// tail
        System.out.println(deque.pollLast());	// last

        deque.offerLast("Java");
        deque.offerLast("SQL");
        deque.offerLast("Java");
        System.out.println(deque.removeFirstOccurrence("Java"));	// true
        System.out.println(deque.removeLastOccurrence("Java"));	// true
        System.out.println(deque);	// [SQL]

        deque.push("Spring");
        System.out.println(deque.peek());	// Spring
        System.out.println(deque.pop());	// Spring
        System.out.println(deque.poll());	// SQL
        System.out.println(deque.isEmpty());	// true
    }

    // addFirst, addLast, removeFirst, removeLast, getFirst and getLast throw error when the operation cannot complete
    // offerFirst and offerLast return false on insertion failure
    // pollFirst, pollLast, peekFirst and peekLast return null when the deque is empty
    // push, pop and peek provide stack behavior at the front of the deque
}
