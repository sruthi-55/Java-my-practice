import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class C05_CollectionImplementations {
    enum Level { LOW, HIGH }

    public static void main(String[] args) {
        // used for frequent indexed reads and mostly append-based updates
        List<String> arrayList = new ArrayList<>(List.of("B", "A", "A"));

        // used when frequent end insertions, removals or deque operations are required
        List<String> linkedList = new LinkedList<>(arrayList);

        // used when a legacy API specifically requires a synchronized list
        List<String> vector = new Vector<>(arrayList);

        // used when unique elements must retain insertion order
        Set<String> linkedSet = new LinkedHashSet<>(arrayList);

        // used for unique sorted elements and range or nearest-value queries
        NavigableSet<String> treeSet = new TreeSet<>(arrayList);

        // used for compact and fast sets containing values from one enum type
        Set<Level> enumSet = EnumSet.allOf(Level.class);

        // used when a thread-safe set has many reads and very few writes
        Set<String> copySet = new CopyOnWriteArraySet<>(arrayList);

        // used when elements must be processed by priority instead of insertion order
        PriorityQueue<String> priorityQueue = new PriorityQueue<>(arrayList);

        // used for key-value lookup with predictable insertion or access order
        Map<String, Integer> linkedMap = new LinkedHashMap<>();

        // used for sorted keys and range-based key queries
        Map<String, Integer> treeMap = new TreeMap<>();

        // used for fast and compact mappings whose keys belong to one enum type
        Map<Level, String> enumMap = new EnumMap<>(Level.class);

        // used when metadata or cache entries should disappear after keys become unreferenced
        Map<Object, String> weakMap = new WeakHashMap<>();

        // used when keys must be compared by reference identity instead of equals()
        Map<Object, String> identityMap = new IdentityHashMap<>();

        // used for highly concurrent key-value access without locking the entire map
        Map<String, Integer> concurrentMap = new ConcurrentHashMap<>();

        // used when simple thread-safe wrapping is sufficient and compound operations are externally locked
        Map<String, Integer> synchronizedMap = Collections.synchronizedMap(linkedMap);

        System.out.println(linkedList + " " + vector + " " + linkedSet + " " + enumSet + " " + copySet);	// each implementation's elements; set order may vary
        System.out.println(treeSet.lower("B") + " " + treeSet.floor("B") + " " + treeSet.ceiling("A") + " " + treeSet.higher("A"));	// a B A B
        System.out.println(priorityQueue.poll() + " " + treeMap + enumMap + weakMap + identityMap + concurrentMap + synchronizedMap);	// a followed by empty maps
    }

    // list implementations
    // list keeps insertion order, allows duplicates and nulls, and provides index-based access
    // ArrayList provides fast indexed reads; LinkedList is useful for deque operations but has slow indexed access
    // Vector is a legacy synchronized list; prefer ArrayList unless an old API specifically requires Vector

    // Set implementations
    // Set stores unique elements and offers membership-based operations instead of index-based access
    // HashSet has no guaranteed order; LinkedHashSet preserves insertion order; TreeSet keeps elements sorted
    // EnumSet is optimized for enum values; CopyOnWriteArraySet suits concurrent reads with very infrequent writes

    // Queue and Deque implementations
    // Queue processes elements from the head; PriorityQueue chooses the head by priority rather than insertion order
    // Deque supports insertion and removal at both ends and ArrayDeque is preferred for stack and queue usage

    // Map implementations
    // Map stores unique keys mapped to values and is separate from the Collection interface hierarchy
    // HashMap has no guaranteed order; LinkedHashMap preserves encounter order; TreeMap keeps keys sorted
    // EnumMap is optimized for enum keys; WeakHashMap can discard entries whose keys are no longer strongly referenced
    // IdentityHashMap compares keys with == instead of equals() and should be used only for identity-based logic
    // ConcurrentHashMap supports scalable concurrent access; synchronizedMap serializes access through one wrapper lock
    // ConcurrentHashMap rejects null keys and values, while HashMap permits one null key and multiple null values
}
