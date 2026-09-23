import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

// reachability determines garbage-collection eligibility rather than reference variables leaving scope alone
// strong references retain objects while soft, weak and phantom references support different lifecycle policies
// garbage collection reclaims memory while explicit close releases resources such as files and sockets

public class OB06_ObjectLifetime {
    public static void main(String[] args) {
        // another strong reference keeps an object reachable after one variable is cleared
        Object first = new Object();
        Object retained = first;
        first = null;
        System.out.println(retained != null);	// true

        // explicit clearing and enqueueing demonstrate reference APIs without assuming a GC schedule
        ReferenceQueue<Object> queue = new ReferenceQueue<>();
        WeakReference<Object> weak = new WeakReference<>(retained, queue);
        SoftReference<Object> soft = new SoftReference<>(retained);
        PhantomReference<Object> phantom = new PhantomReference<>(retained, queue);
        System.out.println(weak.get() == retained);	// true
        System.out.println(soft.get() == retained);	// true
        System.out.println(phantom.get() == null);	// true
        weak.clear();
        System.out.println(weak.get() == null);	// true
        System.out.println(weak.enqueue());	// true
        System.out.println(queue.poll() == weak);	// true
        Reference.reachabilityFence(retained);

        // try-with-resources closes deterministically instead of relying on finalization or collection
        Resource resource = new Resource();
        try (resource) {
            System.out.println(resource.closed);	// false
        }
        System.out.println(resource.closed);	// true
    }

    static final class Resource implements AutoCloseable {
        boolean closed;
        @Override
        public void close() { closed = true; }
    }

    // unreachable cycles are collectible because Java tracing collectors do not depend on simple reference counts
    // reachable caches, static collections and listeners can retain unwanted objects and cause memory leaks
    // System.gc is only a request and collection timing must never be required for program correctness
    // soft references may be cleared under memory pressure and weak references do not retain their referents
    // phantom references expose no referent and use a queue for post-mortem cleanup coordination
    // finalize is deprecated for removal and is neither reliable cleanup nor a destructor
    // Cleaner is a fallback whose cleanup action must not retain the registered object
    // see Java_Basics/B03_MemoryFinalAndGarbageCollection for explicit Cleaner cleanup
    // final restricts reassignment or inheritance while finally handles control flow and finalize is legacy cleanup
}
