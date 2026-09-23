import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

// concurrent collections coordinate access but multi-step application invariants still need protection
// copy-on-write iterators are snapshots while concurrent-map iterators are weakly consistent

public class M16_ConcurrentCollections {
    public static void main(String[] args) throws InterruptedException {
        ConcurrentHashMap<String, Integer> counts = new ConcurrentHashMap<>();
        Runnable update = () -> {
            for (int index = 0; index < 1_000; index++) counts.merge("Java", 1, Integer::sum);
        };
        Thread first = new Thread(update);
        Thread second = new Thread(update);
        first.start();
        second.start();
        first.join();
        second.join();
        System.out.println(counts.get("Java"));	// 2000
        counts.computeIfAbsent("SQL", key -> 1);
        System.out.println(counts.putIfAbsent("SQL", 9));	// 1

        // weakly consistent traversal tolerates concurrent changes but is not an atomic snapshot
        Iterator<Map.Entry<String, Integer>> weak = counts.entrySet().iterator();
        counts.put("Git", 1);
        weak.forEachRemaining(entry -> System.out.println(entry.getKey()));	// Java, SQL and possibly Git in unspecified order

        CopyOnWriteArrayList<String> words = new CopyOnWriteArrayList<>(List.of("Java"));
        Iterator<String> snapshot = words.iterator();
        words.add("SQL");
        snapshot.forEachRemaining(System.out::println);	// Java
        System.out.println(words);	// [Java, SQL]

        // snapshot iterators cannot remove elements from the live copy-on-write list
        Iterator<String> readOnly = words.iterator();
        readOnly.next();
        try {
            readOnly.remove();
        } catch (UnsupportedOperationException exception) {
            System.out.println(exception.getClass().getSimpleName());	// UnsupportedOperationException
        }

        // compound operations and traversal use the synchronized wrapper itself as the monitor
        List<String> wrapped = Collections.synchronizedList(new ArrayList<>());
        synchronized (wrapped) {
            if (!wrapped.contains("Java")) wrapped.add("Java");
            wrapped.forEach(System.out::println);	// Java
        }

        ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<>();
        queue.offer(1);
        System.out.println(queue.poll());	// 1
        System.out.println(queue.poll());	// null
    }

    // ConcurrentHashMap rejects null and atomic key operations do not create transactions across keys
    // keep compute callbacks short and avoid recursive map updates from them
    // copy-on-write duplicates storage on mutation and suits frequent reads with rare writes
    // ConcurrentLinkedQueue is nonblocking and has no capacity bound or waiting take operation
}
