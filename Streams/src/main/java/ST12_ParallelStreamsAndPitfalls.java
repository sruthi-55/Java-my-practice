import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

// parallel streams partition work and combine results while retaining operation-specific ordering guarantees
// stream callbacks must not interfere with the source or depend on changing external state

public class ST12_ParallelStreamsAndPitfalls {
    public static void main(String[] args) {
        System.out.println(List.of(1, 2).parallelStream().isParallel());	// true
        System.out.println(Stream.of(1, 2).parallel().sequential().isParallel());	// false
        System.out.println(IntStream.rangeClosed(1, 100).parallel().sum());	// 5050
        System.out.println(List.of(1, 2, 3).parallelStream().map(value -> value * 2).toList());	// [2, 4, 6]
        List.of(1, 2, 3).parallelStream().forEach(System.out::println);	// 1, 2 and 3 in unspecified order
        List.of(1, 2, 3).parallelStream().forEachOrdered(System.out::println);	// 1, then 2, then 3
        System.out.println(List.of(1, 2, 3).parallelStream().unordered().limit(2).count());	// 2

        // peek is useful for observation but an implementation may skip it when count is known
        AtomicInteger visits = new AtomicInteger();
        System.out.println(Stream.of(1, 2, 3).peek(value -> visits.incrementAndGet()).count());	// 3
        System.out.println(visits.get());	// 0 on this JDK; peek invocation is not guaranteed

        // ordered short-circuit evaluation visits only enough input to find the answer
        int found = Stream.of(1, 2, 3).peek(value -> {
            System.out.println("visited " + value);	// visited 1, then visited 2
        }).filter(value -> value == 2).findFirst().orElseThrow();
        System.out.println(found);	// 2
    }

    // sequential and parallel select the execution mode of the whole pipeline rather than individual stages
    // ordered parallel limit, distinct and sorting can require expensive coordination or buffering
    // parallel streams commonly use the shared fork/join pool; blocking I/O can occupy its workers
    // use collect rather than adding into a shared ArrayList from parallel forEach
    // thread-safe side effects still add contention and do not make a stateful algorithm correct
    // small inputs and cheap operations can make parallel traversal slower; benchmark real workloads
}
